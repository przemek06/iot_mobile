package edu.pwr.iotmobile.androidimcs.model.datasource.local.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import edu.pwr.iotmobile.androidimcs.model.datasource.local.UserSessionLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSessionLocalDataSourceImpl(
    private val context: Context
) : UserSessionLocalDataSource {

    private val Context.preferences: DataStore<Preferences>
        by preferencesDataStore(name = "userSessionPreferences")

    private val USER_SESSION_COOKIE = stringPreferencesKey("userSessionCookie")

    override val userSessionCookie: Flow<String?>
        get() = context.preferences.data.map {
            it[USER_SESSION_COOKIE]
        }

    override suspend fun saveUserSessionCookie(cookie: String) {
        context.preferences.edit {
            it[USER_SESSION_COOKIE] = cookie
        }
    }
}