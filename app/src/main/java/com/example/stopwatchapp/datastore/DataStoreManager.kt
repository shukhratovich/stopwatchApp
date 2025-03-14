package com.example.stopwatchapp.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "timer_prefs")

class DataStoreManager(private val context: Context) {
    companion object {
        val TIME_KEY = longPreferencesKey("saved_times")
    }

    suspend fun saveData(time: Long) {
        context.dataStore.edit { preferences ->
            preferences[TIME_KEY] = time
        }
    }
//
    val timerFlow = context.dataStore.data.map { performance ->
        performance[TIME_KEY] ?: 0L
    }

//    fun getTimeSync(): Long = runBlocking {
//        context.dataStore.data.map { preferences ->
//            preferences[TIME_KEY] ?: 0
//        }.first()
//    }

}