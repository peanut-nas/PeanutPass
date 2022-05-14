package com.peanut.passwordmanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peanut.passwordmanager.data.models.Account

@Database(entities = [Account::class], version = 1, exportSchema = false)
abstract class AccountDatabase: RoomDatabase(){

    abstract fun accountDao(): AccountDao

}