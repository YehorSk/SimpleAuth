package com.example.simpleauth.data.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.simpleauth.model.HttpResponse
import com.example.simpleauth.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
){
    private companion object {
        val USER_NAME = stringPreferencesKey("USER_NAME")
        val USER_EMAIL = stringPreferencesKey("USER_EMAIL")
        val JWT_TOKEN = stringPreferencesKey("JWT_TOKEN")
    }

    val userNameFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[USER_NAME] }

    val userEmailFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[USER_EMAIL] }

    val jwtTokenFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[JWT_TOKEN] }

    suspend fun saveUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
        }
    }

    suspend fun saveUserEmail(userEmail: String) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL] = userEmail
        }
    }

    suspend fun saveJwtToken(jwtToken: String) {
        dataStore.edit { preferences ->
            preferences[JWT_TOKEN] = jwtToken
        }
    }

    suspend fun saveUser(httpResponse: HttpResponse){
        saveUserName(httpResponse.data?.user?.name.toString())
        saveUserEmail(httpResponse.data?.user?.email.toString())
        saveJwtToken(httpResponse.data?.token.toString())
    }

    suspend fun clearAllTokens(){
        dataStore.edit { preferences ->
            preferences.remove(USER_NAME)
            preferences.remove(USER_EMAIL)
            preferences.remove(JWT_TOKEN)
        }
    }
}