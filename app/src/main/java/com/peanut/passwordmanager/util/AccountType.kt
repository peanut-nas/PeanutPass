package com.peanut.passwordmanager.util

import com.peanut.passwordmanager.R

enum class AccountType(val typeNameStringResourceId: Int) {
    Email(R.string.account_type_email),
    CardNumber(R.string.account_type_card_number),
    PhoneNumber(R.string.account_type_phone_number),
    NickName(R.string.account_type_nickname),
    Reference(R.string.account_type_reference)
}