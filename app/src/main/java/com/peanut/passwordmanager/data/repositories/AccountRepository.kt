package com.peanut.passwordmanager.data.repositories

import com.peanut.passwordmanager.data.AccountDao
import com.peanut.passwordmanager.data.models.Account
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class AccountRepository @Inject constructor(private val accountDao: AccountDao) {

    val getAllAccounts: Flow<List<Account>> = accountDao.getAllAccounts()
    val sortByLastRecentUsed: Flow<List<Account>> = accountDao.sortByLastRecentUsed()
    val sortByLastFrequentUsed: Flow<List<Account>> = accountDao.sortByLastFrequentUsed()

    fun getSelectedAccount(accountId: Int): Flow<Account?> {
        return accountDao.getSelectedAccounts(accountId = accountId)
    }

    suspend fun addAccount(account: Account){
        accountDao.addAccount(account = account)
    }

    suspend fun updateAccount(account: Account){
        accountDao.updateAccount(account = account)
    }

    suspend fun deleteAccount(account: Account){
        accountDao.deleteAccount(account = account)
    }

    fun searchAccounts(searchQuery: String): Flow<List<Account>>{
        return accountDao.searchAccounts(searchQuery = searchQuery)
    }

    suspend fun increaseAccessTimes(account: Account){
        account.accessTimes += 1
        account.accessTime = System.currentTimeMillis()
        updateAccount(account)
    }

}