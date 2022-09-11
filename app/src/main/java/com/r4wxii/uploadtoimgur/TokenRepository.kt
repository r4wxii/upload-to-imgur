package com.r4wxii.uploadtoimgur.com.r4wxii.uploadtoimgur

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        private object PreferencesKeys {
            val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
        }
    }

    val flow = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.REFRESH_TOKEN]?: ""
    }

    suspend fun saveToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class TokenRepositoryModule {
    @Provides
    fun providesTokenRepository(dataStore: DataStore<Preferences>) = TokenRepository(dataStore)
}