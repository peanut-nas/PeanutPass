package com.peanut.passwordmanager.data.repositories

import androidx.datastore.preferences.core.stringPreferencesKey
import com.peanut.passwordmanager.util.Constants

object PreferenceKeys{
    val homeSortTopAccount = stringPreferencesKey(name = Constants.HOME_SORT_TOP_ACCOUNT)

}