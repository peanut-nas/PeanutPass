package com.peanut.passwordmanager.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.data.repositories.AccountRepository
import com.peanut.passwordmanager.util.AccountType
import com.peanut.passwordmanager.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: AccountRepository): ViewModel() {
    private val _allAccounts = MutableStateFlow<RequestState<List<Account>>>(RequestState.Idle)
    val allAccounts: StateFlow<RequestState<List<Account>>> = _allAccounts

    fun getAllAccounts(){
        _allAccounts.value = RequestState.Loading
        viewModelScope.launch {
            try {
                repository.getAllAccounts.collect{
                    _allAccounts.value = RequestState.Success(it)
                }
            }catch (e: Exception){
                _allAccounts.value = RequestState.Error(e)
            }
        }
    }

    fun getAccountById(id: Int) = repository.getSelectedAccount(accountId = id)

    private val _selectedAccount = MutableStateFlow<Account?>(null)
    val selectedAccount: StateFlow<Account?> = _selectedAccount
    
    fun getSelectedAccount(id: Int){
        viewModelScope.launch {
            repository.getSelectedAccount(id).collect{ account ->
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
}