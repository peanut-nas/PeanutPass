package com.peanut.passwordmanager.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.data.repositories.AccountRepository
import com.peanut.passwordmanager.data.repositories.DataStoreRepository
import com.peanut.passwordmanager.util.AccountType
import com.peanut.passwordmanager.util.Action
import com.peanut.passwordmanager.util.RequestState
import com.peanut.passwordmanager.util.TopAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: AccountRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _allAccounts = MutableStateFlow<RequestState<List<Account>>>(RequestState.Idle)
    val allAccounts: StateFlow<RequestState<List<Account>>> = _allAccounts

    fun getAllAccounts() {
        _allAccounts.value = RequestState.Loading
        viewModelScope.launch {
            try {
                repository.getAllAccounts.collect {
                    _allAccounts.value = RequestState.Success(it)
                }
            } catch (e: Exception) {
                _allAccounts.value = RequestState.Error(e)
            }
        }
    }

    fun getAccountById(id: Int) = repository.getSelectedAccount(accountId = id)

    private val _selectedAccount = MutableStateFlow<Account?>(null)
    val selectedAccount: StateFlow<Account?> = _selectedAccount

    fun getSelectedAccount(id: Int) {
        viewModelScope.launch {
            repository.getSelectedAccount(id).collect { account ->
                _selectedAccount.value = account
            }
        }
    }

    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val account: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("点我生成密码")
    val icon: MutableState<String> = mutableStateOf("")
    val accountType: MutableState<AccountType> = mutableStateOf(AccountType.NickName)

    fun updateAccountFields(selectedAccount: Account?) {
        if (selectedAccount != null) {
            id.value = selectedAccount.id
            title.value = selectedAccount.title
            account.value = selectedAccount.account
            accountType.value = selectedAccount.accountType
            password.value = selectedAccount.password
            icon.value = selectedAccount.icon
        }
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() &&
                account.value.isNotEmpty() &&
                password.value.isNotEmpty() && password.value != "点我生成密码"
    }

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> addAccount()
            Action.UPDATE -> updateAccount()
            Action.DELETE -> deleteAccount()
            Action.UNDO -> addAccount()
            else -> {}
        }
    }

    private fun addAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            val account = Account(
                title = title.value,
                icon = icon.value,
                account = account.value,
                password = password.value,
                accountType = accountType.value
            )
            repository.addAccount(account)
        }
        topAppBarState.value = TopAppBarState.DEFAULT
    }

    private fun updateAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            val account = Account(
                id = id.value,
                title = title.value,
                icon = icon.value,
                account = account.value,
                password = password.value,
                accountType = accountType.value
            )
            repository.updateAccount(account)
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            val account = Account(
                id = id.value,
                title = title.value,
                icon = icon.value,
                account = account.value,
                password = password.value,
                accountType = accountType.value
            )
            repository.deleteAccount(account)
        }
    }

    private val _searchedAccounts = MutableStateFlow<RequestState<List<Account>>>(RequestState.Idle)
    val searchedAccounts: StateFlow<RequestState<List<Account>>> = _searchedAccounts

    fun searchAccounts(searchQuery: String) {
        _searchedAccounts.value = RequestState.Loading
        viewModelScope.launch {
            try {
                repository.searchAccounts("%$searchQuery%").collect {
                    _searchedAccounts.value = RequestState.Success(it)
                }
            } catch (e: Exception) {
                _searchedAccounts.value = RequestState.Error(e)
            }
            topAppBarState.value = TopAppBarState.TRIGGERED
        }
    }

    val topAppBarState = mutableStateOf(TopAppBarState.DEFAULT)
}