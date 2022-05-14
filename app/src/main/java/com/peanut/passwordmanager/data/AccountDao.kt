package com.peanut.passwordmanager.data

import androidx.room.*
import com.peanut.passwordmanager.data.models.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM accounts ORDER BY id ASC")
    fun getAllAccounts(): Flow<List<Account>>

    @Query("SELECT * FROM accounts WHERE id=:accountId")
    fun getSelectedAccounts(accountId: Int): Flow<Account>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccount(account: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("SELECT * FROM accounts WHERE title LIKE :searchQuery OR account LIKE :searchQuery")
    fun searchAccounts(searchQuery: String): Flow<List<Account>>

    @Query("SELECT * FROM accounts ORDER BY accessTime DESC")
    fun sortByLastRecentUsed(): Flow<List<Account>>

    @Query("SELECT * FROM accounts ORDER BY accessTimes DESC")
    fun sortByLastFrequentUsed(): Flow<List<Account>>
}