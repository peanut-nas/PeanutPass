package com.peanut.passwordmanager.data.repositories

import com.peanut.passwordmanager.data.AccountDao
import com.peanut.passwordmanager.data.models.Account
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ReadOnlyAccountRepository @Inject constructor(accountDao: AccountDao) {

    val getAllAccounts: Flow<List<Account>> = accountDao.getAllAccounts()

}