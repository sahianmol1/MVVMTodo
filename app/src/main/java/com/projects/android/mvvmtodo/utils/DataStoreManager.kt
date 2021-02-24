package com.projects.android.mvvmtodo.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val SORT_BY_DATE_KEY = "sort_by_data"
const val SORT_BY_NAME_KEY = "sort_by_name"
const val HIDE_COMPLETED_KEY = "hide_completed"
@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context) {
    val dataStore : DataStore<Preferences> = context.createDataStore("tasks_store")

    suspend fun saveSortByDate(saveSortByDateCreatedFlag: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(SORT_BY_DATE_KEY)] = saveSortByDateCreatedFlag
        }
    }

    suspend fun saveSortByName(saveSortByNameFlag: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(SORT_BY_NAME_KEY)] = saveSortByNameFlag
        }
    }

    suspend fun saveHideCompleted(hideCompletedFlag: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(HIDE_COMPLETED_KEY)] = hideCompletedFlag
        }
    }

    fun getSortByDateState() = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(SORT_BY_DATE_KEY)]
    }

    fun getSortByNameState() = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(SORT_BY_NAME_KEY)]
    }

    fun getHideCompletedState() = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(HIDE_COMPLETED_KEY)]
    }




}