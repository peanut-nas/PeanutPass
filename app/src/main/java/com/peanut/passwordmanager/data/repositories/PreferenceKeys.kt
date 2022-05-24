package com.peanut.passwordmanager.data.repositories

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys{
    val HomeSortAllAccount = stringPreferencesKey("HomeSortAllAccount")
    val PasswordGeneratorLength = intPreferencesKey("PasswordGeneratorLength")
    val PasswordGeneratorUseLetter = booleanPreferencesKey("PasswordGeneratorUseLetter")
    val PasswordGeneratorUseUpperLetter = booleanPreferencesKey("PasswordGeneratorUseUpperLetter")
    val PasswordGeneratorUseNumber = booleanPreferencesKey("PasswordGeneratorUseNumber")
    val PasswordGeneratorUseSymbol = booleanPreferencesKey("PasswordGeneratorUseSymbol")
}