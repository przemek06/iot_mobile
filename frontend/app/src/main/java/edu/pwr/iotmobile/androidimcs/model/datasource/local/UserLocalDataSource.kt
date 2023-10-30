package edu.pwr.iotmobile.androidimcs.model.datasource.local

import edu.pwr.iotmobile.androidimcs.UserStore
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    fun getData(): Flow<UserStore>
    suspend fun updateData(transform: suspend (t: UserStore) -> UserStore): UserStore
}