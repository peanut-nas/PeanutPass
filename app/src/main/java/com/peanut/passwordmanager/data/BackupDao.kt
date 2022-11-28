package com.peanut.passwordmanager.data

import androidx.room.*
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.data.models.AccountBackup
import kotlinx.coroutines.flow.Flow

@Dao
interface BackupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccount(account: AccountBackup)

    @Query("DELETE FROM accounts")
    suspend fun deleteTable()
}