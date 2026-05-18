package br.dc.ufscar.devmobile.configs

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.dc.ufscar.devmobile.daos.ReserveDao
import br.dc.ufscar.devmobile.daos.UserDao
import br.dc.ufscar.devmobile.entities.Reserve
import br.dc.ufscar.devmobile.entities.User

@Database(entities = [User::class, Reserve::class], version = 1)
@TypeConverters(Converters::class)
abstract class UPeekDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao
    abstract fun reserveDao() : ReserveDao
}
