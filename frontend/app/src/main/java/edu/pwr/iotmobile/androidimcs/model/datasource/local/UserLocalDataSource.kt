package edu.pwr.iotmobile.androidimcs.model.datasource.local

import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    val userSessionCookie: Flow<String?>
    suspend fun saveUserSessionCookie(cookie: String)
}