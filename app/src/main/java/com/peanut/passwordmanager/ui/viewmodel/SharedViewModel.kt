package com.peanut.passwordmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.data.repositories.AccountRepository
import com.peanut.passwordmanager.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
}