package com.peanut.passwordmanager.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.util.AccountType

@Composable
fun AccountTypeDropDown(accountType: AccountType, onAccountTypeSelected: (AccountType) -> Unit){
    var expanded by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { expanded = true }, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Account Type: ")
        Row(modifier = Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = accountType.typeNameStringResourceId))
            IconButton(onClick = { expanded = true }) {
                Icon(modifier = Modifier.rotate(angle), imageVector = Icons.Rounded.ArrowDropDown, contentDescription = stringResource(id = R.string.select_account_type))
            }
        }
        DropdownMenu(modifier = Modifier
            .fillMaxWidth(), expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text(text = stringResource(id = AccountType.Email.typeNameStringResourceId)) }, onClick = {
                expanded = false
                onAccountTypeSelected(AccountType.Email)
            })
            DropdownMenuItem(text = { Text(text = stringResource(id = AccountType.CardNumber.typeNameStringResourceId)) }, onClick = {
                expanded = false
                onAccountTypeSelected(AccountType.CardNumber)
            })
            DropdownMenuItem(text = { Text(text = stringResource(id = AccountType.PhoneNumber.typeNameStringResourceId)) }, onClick = {
                expanded = false
                onAccountTypeSelected(AccountType.PhoneNumber)
            })
            DropdownMenuItem(text = { Text(text = stringResource(id = AccountType.NickName.typeNameStringResourceId)) }, onClick = {
                expanded = false
                onAccountTypeSelected(AccountType.NickName)
            })
            DropdownMenuItem(text = { Text(text = stringResource(id = AccountType.Reference.typeNameStringResourceId)) }, onClick = {
                expanded = false
                onAccountTypeSelected(AccountType.Reference)
            })
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AccountTypeDropDownPreview(){
    AccountTypeDropDown(accountType = AccountType.Email, onAccountTypeSelected = {})
}
