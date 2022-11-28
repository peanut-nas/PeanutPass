package com.peanut.passwordmanager.data.repositories

import android.content.Context
import com.peanut.passwordmanager.data.BackupDao
import com.peanut.passwordmanager.data.BackupDatabase
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.data.models.AccountBackup
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

@ViewModelScoped
class BackupRepository constructor(private val backDao: BackupDao, private val database: BackupDatabase) {

    suspend fun addAccount(account: Account, context: Context){
        val b:ByteArray
        if (account.icon.isNotEmpty()){
            val iconFolder = context.filesDir.absolutePath+"/"
            b = withContext(Dispatchers.IO){
                val fip = FileInputStream(File(iconFolder+account.icon).apply { println(this.path) })
                val c = ByteArray(fip.available())
                fip.read(c)
                c
            }
        }else{
            b = byteArrayOf()
        }
        backDao.addAccount(AccountBackup(
            id = account.id,
            title = account.title,
            icon = account.icon,
            account = account.account,
            password = account.password,
            accountType = account.accountType,
            accessTime = account.accessTime,
            accessTimes = account.accessTimes,
            createTime = account.createTime,
            image = b)
        )
    }

    suspend fun deleteTable(){
        backDao.deleteTable()
    }

    fun close(){
        database.close()
    }

}