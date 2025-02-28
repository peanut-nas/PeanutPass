package com.peanut.passwordmanager.service

import android.app.assist.AssistStructure
import android.content.Context
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.net.Uri
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.Dataset
import android.service.autofill.FillCallback
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.SaveCallback
import android.service.autofill.SaveRequest
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.data.repositories.ReadOnlyAccountRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MyAutofillService: AutofillService() {
    @Inject
    lateinit var repository: ReadOnlyAccountRepository

    override fun onFillRequest(request: FillRequest, p1: CancellationSignal, p2: FillCallback) {
        val structure = request.fillContexts.lastOrNull()?.structure
        if (structure == null) {
            p2.onFailure("No structure")
            return
        }
        val j = MainScope().launch {
            val r = fetchDataAndGenerateResult(structure, applicationContext)
            p2.onSuccess(r)
        }
        p1.setOnCancelListener {
            j.cancel()
        }
    }

    private suspend fun fetchDataAndGenerateResult(structure: AssistStructure, context: Context): FillResponse {
        val matchesAccounts = repository.getAllAccounts.first()
        val response = FillResponse.Builder()
        val finder = WindowNodeFinder()
        val uid = finder.find("username", structure)?:return response.build()
        val pid = finder.find("password", structure)?:return response.build()
        matchesAccounts.forEach {
            val b = Dataset.Builder()
            val uv = RemoteViews(packageName, R.layout.fill_surgest_item).apply {
                this.setTextViewText(R.id.text, it.title)
                if (it.icon.isNotEmpty()) {
                    val iconFolder = context.filesDir.absolutePath + "/"
                    val iconUri = Uri.fromFile(File(iconFolder + it.icon))
                    val source = ImageDecoder.createSource(context.contentResolver, iconUri)
                    val drawable = ImageDecoder.decodeBitmap(source){ decoder, _, _ ->
                        decoder.setPostProcessor { canvas ->
                            val path = Path().apply {
                                fillType = Path.FillType.INVERSE_EVEN_ODD
                                addCircle(canvas.width.toFloat()/2, canvas.height.toFloat()/2, canvas.width.toFloat()/2, Path.Direction.CW)
                            }
                            Paint().apply {
                                isAntiAlias = true
                                color = Color.TRANSPARENT
                                xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
                            }.let { paint ->
                                canvas.drawPath(path, paint)
                            }
                            PixelFormat.TRANSLUCENT
                        }
                    }
                    this.setImageViewBitmap(R.id.icon, drawable)
                }
            }
            b.setValue(uid, AutofillValue.forText(it.account), uv)
            b.setValue(pid, AutofillValue.forText(it.password))
            response.addDataset(b.build())
        }
        return response.build()
    }

    override fun onSaveRequest(p0: SaveRequest, p1: SaveCallback) {

    }
}