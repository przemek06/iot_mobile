package edu.pwr.iotmobile.androidimcs.app.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.pwr.iotmobile.androidimcs.data.entity.DashboardEntity
import edu.pwr.iotmobile.androidimcs.model.datasource.local.dao.DashboardDao


@Database(
    entities = [
        DashboardEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dashboardDao(): DashboardDao

    companion object {
        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "imcs.app.database"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}
