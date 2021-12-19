package com.yashk9.noterr.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.prefDatastore by preferencesDataStore("ui_mode")

class AppDatastore @Inject constructor(context: Context) {

    companion object {
        private val UI_MODE_KEY = booleanPreferencesKey("ui_mode")
    }

    private val datastore = context.prefDatastore

    val uiMode: Flow<Boolean>
        get() = datastore.data.map { pref ->
            val uiMode = pref[UI_MODE_KEY] ?: false
            uiMode
        }

    suspend fun saveUiToDatastore(isNightMode: Boolean){
        datastore.edit { pref ->
            pref[UI_MODE_KEY] = isNightMode
        }
    }
}