package com.peanut.passwordmanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peanut.passwordmanager.util.AccountType
import com.peanut.passwordmanager.util.Constants

@Entity(tableName = Constants.DATABASE_TABLE)
data class AccountBackup(
    @PrimaryKey
    val id: Int,
    val title: String,
    val icon : String = "",
    val account: String,
    val password: String,
    val accountType: AccountType = AccountType.NickName,
    var accessTime: Long = System.currentTimeMillis(),
    val createTime: Long = System.currentTimeMillis(),
    var accessTimes: Int = 0,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) var image: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AccountBackup

        if (id != other.id) return false
        if (title != other.title) return false
        if (icon != other.icon) return false
        if (account != other.account) return false
        if (password != other.password) return false
        if (accountType != other.accountType) return false
        if (accessTime != other.accessTime) return false
        if (createTime != other.createTime) return false
        if (accessTimes != other.accessTimes) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + account.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + accountType.hashCode()
        result = 31 * result + accessTime.hashCode()
        result = 31 * result + createTime.hashCode()
        result = 31 * result + accessTimes
        return result
    }
}