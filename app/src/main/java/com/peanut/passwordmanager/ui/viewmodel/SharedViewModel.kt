package com.peanut.passwordmanager.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.autofill.AutofillManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.peanut.passwordmanager.data.BackupDatabase
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.data.repositories.AccountRepository
import com.peanut.passwordmanager.data.repositories.BackupRepository
import com.peanut.passwordmanager.data.repositories.DataStoreRepository
import com.peanut.passwordmanager.data.repositories.PreferenceKeys
import com.peanut.passwordmanager.util.AccountSortStrategy
import com.peanut.passwordmanager.util.AccountType
import com.peanut.passwordmanager.util.Action
import com.peanut.passwordmanager.util.Constants
import com.peanut.passwordmanager.util.RequestState
import com.peanut.passwordmanager.util.TopAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: AccountRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    // 所有账号，按创建顺序降序，给主页顶部使用
    private val _topAccounts = MutableStateFlow<RequestState<List<Account>>>(RequestState.Idle)
    val topAccounts: StateFlow<RequestState<List<Account>>> = _topAccounts

    // 所有账号，排序不定。
    private val _mainAccounts = MutableStateFlow<RequestState<List<Account>>>(RequestState.Idle)
    val mainAccounts: StateFlow<RequestState<List<Account>>> = _mainAccounts

    // 当前选中的账号
    private val _selectedAccount = MutableStateFlow<Account?>(null)
    val selectedAccount: StateFlow<Account?> = _selectedAccount

    // 排序规则
    private val _sortState = MutableStateFlow(AccountSortStrategy.LastCreated)
    val sortState: StateFlow<AccountSortStrategy> = _sortState

    // 生物设备验证
    private val _authorized = MutableStateFlow(false)
    val authorized: StateFlow<Boolean> = _authorized

    fun feedData() {
        viewModelScope.launch {
            collectStateData(_topAccounts) { repository.sortByLastFrequentUsed }
            collectData(_sortState) {
                dataStoreRepository.read(PreferenceKeys.HomeSortAllAccount).map {
                    AccountSortStrategy.valueOf(it ?: AccountSortStrategy.LastCreated.name)
                }
            }
            launch {
                sortState.collect {
                    when (it) {
                        AccountSortStrategy.LastRecentUsed -> collectStateData(_mainAccounts) { repository.sortByLastRecentUsed }
                        AccountSortStrategy.LastFrequentUsed -> _mainAccounts.value = _topAccounts.value
                        AccountSortStrategy.LastCreated -> collectStateData(_mainAccounts) { repository.getAllAccounts }
                    }
                }
            }
        }
    }

    suspend fun notifySelectedAccountChange(selectedAccountID: Int) {
        innerNotifyDataChange(_selectedAccount) { repository.getSelectedAccount(accountId = selectedAccountID) }
    }

    private fun <T> CoroutineScope.collectStateData(data: MutableStateFlow<RequestState<T>>, provider: () -> Flow<T>) {
        data.value = RequestState.Loading
        launch {
            try {
                provider().collect {
                    data.value = RequestState.Success(it)
                }
            } catch (e: Exception) {
                data.value = RequestState.Error(e)
            }
        }
    }

    private fun <T> CoroutineScope.collectData(data: MutableStateFlow<T>, provider: () -> Flow<T>) {
        launch {
            try {
                provider().collect {
                    data.value = it
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun <T> innerNotifyDataChange(data: MutableStateFlow<T>, provider: () -> Flow<T>) {
        provider().collect {
            data.value = it
        }
    }

    fun getAccountById(id: Int) = repository.getSelectedAccount(accountId = id)


    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val account: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("点我生成密码")
    val icon: MutableState<String> = mutableStateOf("")
    val accountType: MutableState<AccountType> = mutableStateOf(AccountType.NickName)

    fun updateAccountFields(selectedAccount: Account?) {
        val sAccount = selectedAccount ?: Account()
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

    fun persistSortState(accountSortStrategy: AccountSortStrategy) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.store(key = PreferenceKeys.HomeSortAllAccount, value = accountSortStrategy.name)
        }
    }

    fun persistPasswordSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.store(key = PreferenceKeys.PasswordGeneratorLength, value = 0)
        }
    }


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

    fun increaseAccountAccessTimes(account: Account, delay: Long) {
        viewModelScope.launch {
            delay(delay)
            repository.increaseAccessTimes(account)
        }
    }

    private suspend fun shouldShow(key: String, consume: () -> Boolean) {
        val stored = dataStoreRepository.read(booleanPreferencesKey(key)).firstOrNull() ?: false
        if (stored) return
        if (consume()) {
            dataStoreRepository.store(booleanPreferencesKey(key), true)
        }
    }

    suspend fun backup(context: Context) {
        val backup = Room.databaseBuilder(
            context,
            BackupDatabase::class.java, Constants.DATABASE_NAME_BACKUP
        ).build()
        val r = BackupRepository(backup.backupDao(), backup)
        r.deleteTable()
        when (val it = topAccounts.value) {
            is RequestState.Success -> {
                it.data.forEach {
                    r.addAccount(it, context)
                }
            }

            else -> {}
        }
        backup.close()
    }

    fun enableAutofill(context: Context) {
        if (checkAutofillStatus(context)) return
        viewModelScope.launch {
            shouldShow(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE) {
                val intent = Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE).apply {
                    data = Uri.parse("package:${context.packageName}")
                }
                context.startActivity(intent)
                return@shouldShow true
            }
        }
    }

    private fun checkAutofillStatus(context: Context): Boolean {
        val autofillManager = getSystemService(context, AutofillManager::class.java) ?: return true
        if (!autofillManager.isAutofillSupported) return true
        return autofillManager.hasEnabledAutofillServices()
    }

    fun authorize(biometricPrompt: BiometricPrompt) {
        biometricPrompt.authenticate(BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Password Manager")
            .setSubtitle("Use your fingerprint to unlock")
            .setConfirmationRequired(false)
            .setNegativeButtonText("Cancel")
            .build())
    }

    fun login() {
        _authorized.value = true
    }
}