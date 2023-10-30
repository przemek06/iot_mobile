package edu.pwr.iotmobile.androidimcs.model.datasource.local.impl

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import edu.pwr.iotmobile.androidimcs.UserStore
import edu.pwr.iotmobile.androidimcs.model.datasource.local.UserLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

private const val FILENAME = "user_store_file"

class UserLocalDataSourceImpl(context: Context) : UserLocalDataSource {
    private val userData = DataStoreFactory.create(
        serializer = UserStoreSerializer,
        produceFile = { context.dataStoreFile(FILENAME) }
    )
    override fun getData(): Flow<UserStore> {
        return userData.data
    }

    override suspend fun updateData(transform: suspend (t: UserStore) -> UserStore): UserStore {
        return userData.updateData(transform)
    }
}

object UserStoreSerializer : Serializer<UserStore> {
    override val defaultValue: UserStore
        get() = UserStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserStore {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching { UserStore.parseFrom(input) }
        }.getOrDefault(defaultValue)
    }

    override suspend fun writeTo(t: UserStore, output: OutputStream) {
        withContext(Dispatchers.IO) {
            kotlin.runCatching { t.writeTo(output) }
        }
    }

}