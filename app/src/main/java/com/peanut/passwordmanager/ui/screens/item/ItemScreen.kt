package com.peanut.passwordmanager.ui.screens.item

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.AccountType
import com.peanut.passwordmanager.util.Action

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(navigateToHomeScreen: (Action) -> Unit, selectedAccount: Account?, sharedViewModel: SharedViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val title by sharedViewModel.title
    val account by sharedViewModel.account
    val icon by sharedViewModel.icon
    val accountType by sharedViewModel.accountType
    val password by sharedViewModel.password

    val context = LocalContext.current
    val validateErrorText = stringResource(id = R.string.account_input_not_validate)
    val blurRadius = remember { Animatable(0f) }
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(sheetState.targetValue) {
        val targetBlur = if (sheetState.targetValue == SheetValue.Expanded) 16f else 0f
        blurRadius.animateTo(
            targetValue = targetBlur,
            animationSpec = tween(durationMillis = 300)
        )
    }

    BackHandler {
        navigateToHomeScreen(Action.NO_ACTION)
    }

    Scaffold(
        topBar = {
            ItemTopAppBar(scrollBehavior = scrollBehavior, navigateToHomeScreen = { action: Action ->
                if (action == Action.NO_ACTION) {
                    navigateToHomeScreen(action)
                } else {
                    if (sharedViewModel.validateFields()) {
                        navigateToHomeScreen(action)
                    } else {
                        Toast.makeText(context, validateErrorText, Toast.LENGTH_SHORT).show()
                    }
                }
            }, selectedAccount = selectedAccount)
        },
        content = {
            Surface(modifier = Modifier.padding(it)) {
                ItemScreenContent(
                    title = title,
                    account = account,
                    icon = icon,
                    accountType = accountType,
                    password = password,
                    onTitleChanged = { title -> sharedViewModel.title.value = title },
                    onAccountChanged = { account -> sharedViewModel.account.value = account },
                    onIconChanged = { icon -> sharedViewModel.icon.value = icon },
                    onAccountTypeChanged = { accountType: AccountType -> sharedViewModel.accountType.value = accountType },
                    onPasswordChanged = { password -> sharedViewModel.password.value = password }
                ) { openBottomSheet = true }
            }
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .blur(blurRadius.value.dp),
    )

    if (openBottomSheet) {
        BottomSheetPasswordGenerator(sheetState, { openBottomSheet = false;println("openBottomSheet = false;") }) { generated -> sharedViewModel.password.value = generated }
    }


}



