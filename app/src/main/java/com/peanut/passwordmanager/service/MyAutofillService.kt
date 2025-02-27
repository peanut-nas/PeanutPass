package com.peanut.passwordmanager.service

import android.app.assist.AssistStructure
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.Dataset
import android.service.autofill.FillCallback
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.SaveCallback
import android.service.autofill.SaveRequest
import android.view.autofill.AutofillValue
import com.peanut.passwordmanager.data.repositories.ReadOnlyAccountRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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
        println(request)
        val dataset = buildDataset(structure)
        println(dataset)
        val response = FillResponse.Builder()
            .addDataset(dataset)
            .build()
        p2.onSuccess(response)
    }

    private fun buildDataset(structure: AssistStructure): Dataset {
        val finder = WindowNodeFinder()
        val b = Dataset.Builder()
        val uid = finder.find("username", structure)?:return b.build()
        val pid = finder.find("password", structure)?:return b.build()
        runBlocking { repository.getAllAccounts.first().forEach {
            b.setValue(uid, AutofillValue.forText(it.account))
            b.setValue(pid, AutofillValue.forText(it.password))
        }}
        return b.build()
    }

    override fun onSaveRequest(p0: SaveRequest, p1: SaveCallback) {

    }
}