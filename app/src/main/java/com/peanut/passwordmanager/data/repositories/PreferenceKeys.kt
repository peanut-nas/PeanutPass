package com.peanut.passwordmanager.data.repositories

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys{
    val homeSortTopAccount = stringPreferencesKey("homeSortTopAccount")
    val homeSortAllAccount = stringPreferencesKey("homeSortAllAccount")
}