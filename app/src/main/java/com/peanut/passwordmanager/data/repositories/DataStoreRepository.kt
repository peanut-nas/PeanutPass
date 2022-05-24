package com.peanut.passwordmanager.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.peanut.passwordmanager.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCE_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val dataStore = context.datastore

    suspend fun<T> store(key: Preferences.Key<T>, value: T){
        dataStore.edit {
            it[key] = value
        }
    }

    fun<T> read(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            }else throw exception
        }.map { preference ->
            preference[key]
        }
    }
}