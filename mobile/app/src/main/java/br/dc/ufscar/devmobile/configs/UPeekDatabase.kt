package br.dc.ufscar.devmobile.configs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.dc.ufscar.devmobile.daos.UserDao
import br.dc.ufscar.devmobile.entities.User

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class UPeekDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UPeekDatabase? = null

        fun getInstance(context: Context): UPeekDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UPeekDatabase::class.java,
                    "upeek_database"
                ).build().also { INSTANCE = it }
            }
    }
}