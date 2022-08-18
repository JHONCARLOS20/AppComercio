package pe.idat

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities=arrayOf(ComercioEntity::class),version=2)
abstract class ComercioDatabase: RoomDatabase()
{
    abstract fun ComercioDao(): ComercioDao
}