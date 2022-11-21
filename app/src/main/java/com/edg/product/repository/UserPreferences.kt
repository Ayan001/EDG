package com.edg.product.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_data_store")
class UserPreferences @Inject constructor(@ApplicationContext context: Context){

    private val appContext = context.applicationContext

    val favoriteList: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[FAVORITE_LIST]
        }



    suspend fun saveFavoriteList(accessToken: String) {//var products : ArrayList<ProductList>
        appContext.dataStore.edit { preferences ->
            preferences[FAVORITE_LIST] = accessToken
        }
    }



    suspend fun clear() {
        appContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val FAVORITE_LIST = stringPreferencesKey("key_favorite_list")
    }
}