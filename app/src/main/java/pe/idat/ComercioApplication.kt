package pe.idat

import android.app.Application
import androidx.room.Room

class ComercioApplication: Application()
{
    //nos permite acceder al database desde cualquier punto (patrón singleton)
    companion object {
        lateinit var database: ComercioDatabase
    }

    override fun onCreate() {
        super.onCreate()

        //cargar database
        database=Room
            .databaseBuilder(this,ComercioDatabase::class.java,"ComercioDatabase")
            .build()
    }
}