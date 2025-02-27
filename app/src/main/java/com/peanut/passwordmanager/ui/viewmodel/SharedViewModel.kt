package com.peanut.passwordmanager.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.peanut.passwordmanager.data.BackupDatabase
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.data.repositories.AccountRepository
import com.peanut.passwordmanager.data.repositories.BackupRepository
import com.peanut.passwordmanager.data.repositories.DataStoreRepository
import com.peanut.passwordmanager.data.repositories.PreferenceKeys
import com.peanut.passwordmanager.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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
        val sAccount = selectedAccount?:Account()
        id.value = sAccount.id
        title.value = sAccount.title
        account.value = sAccount.account
        accountType.value = sAccount.accountType
        password.value = sAccount.password
        icon.value = sAccount.icon
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
        }
        topAppBarState.value = TopAppBarState.TRIGGERED
    }

    val topAppBarState = mutableStateOf(TopAppBarState.DEFAULT)

    fun persistSortState(accountSortStrategy: AccountSortStrategy){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.store(key = PreferenceKeys.HomeSortAllAccount, value = accountSortStrategy.name)
        }
    }

    fun persistPasswordSettings(){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.store(key = PreferenceKeys.PasswordGeneratorLength, value = 0)
        }
    }

    private val _sortState = MutableStateFlow<RequestState<AccountSortStrategy>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<AccountSortStrategy>> = _sortState

    val lastRecentUsedAccounts: StateFlow<List<Account>> = repository.sortByLastRecentUsed.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val lastFrequentUsedAccounts: StateFlow<List<Account>> = repository.sortByLastFrequentUsed.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        emptyList()
    )

    fun readSortState(){
        _sortState.value = RequestState.Loading
        viewModelScope.launch {
            try {
                dataStoreRepository.read(PreferenceKeys.HomeSortAllAccount).map {
                    AccountSortStrategy.valueOf(it?:AccountSortStrategy.LastCreated.name)
                }.collect{
                    _sortState.value = RequestState.Success(it)
                }
            } catch (e: Exception) {
                _sortState.value = RequestState.Error(e)
            }
        }
    }

    fun increaseAccountAccessTimes(account: Account, delay: Long){
        viewModelScope.launch {
            delay(delay)
            repository.increaseAccessTimes(account)
        }
    }

    suspend fun shouldShow(key: String, consume: () -> Boolean) {
        val stored = dataStoreRepository.read(booleanPreferencesKey(key)).firstOrNull()?:false
        if (stored) return
        if (consume()) {
            dataStoreRepository.store(booleanPreferencesKey(key), true)
        }
    }

    suspend fun backup(context: Context){
        val backup = Room.databaseBuilder(
            context,
            BackupDatabase::class.java, Constants.DATABASE_NAME_BACKUP
        ).build()
        val r = BackupRepository(backup.backupDao(), backup)
        r.deleteTable()
        when(val it = allAccounts.value){
            is RequestState.Success -> {
                it.data.forEach {
                    r.addAccount(it, context)
                }
            }
            else -> {}
        }
        backup.close()
    }
}