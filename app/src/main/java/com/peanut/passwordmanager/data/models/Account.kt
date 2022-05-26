package com.peanut.passwordmanager.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peanut.passwordmanager.util.AccountType
import com.peanut.passwordmanager.util.Constants

@Entity(tableName = Constants.DATABASE_TABLE)
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val icon : String = "",
    val account: String,
    val password: String,
    val accountType: AccountType = AccountType.NickName,
    var accessTime: Long = System.currentTimeMillis(),
    val createTime: Long = System.currentTimeMillis(),
    var accessTimes: Int = 0
)