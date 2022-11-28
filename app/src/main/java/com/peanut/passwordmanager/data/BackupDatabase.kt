package com.peanut.passwordmanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peanut.passwordmanager.data.models.AccountBackup

@Database(entities = [AccountBackup::class], version = 1, exportSchema = false)
abstract class BackupDatabase: RoomDatabase(){

    abstract fun backupDao(): BackupDao

}