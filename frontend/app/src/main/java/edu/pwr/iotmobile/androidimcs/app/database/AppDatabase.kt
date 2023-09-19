package edu.pwr.iotmobile.androidimcs.app.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [ /*TODO: list entities*/],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    /*TODO: list DAOs*/

    companion object {
        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "translate.app.database"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}
