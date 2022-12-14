package pe.idat

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import pe.idat.common.database.ComercioAPI
import pe.idat.common.database.ComercioDatabase

class ComercioApplication: Application()
{
    //nos permite acceder al database desde cualquier punto (patrón singleton)
    companion object {
        lateinit var database: ComercioDatabase
        lateinit var comercioAPI: ComercioAPI
    }

    override fun onCreate() {
        super.onCreate()

        //modificar database, agregando nueva comuna
        val MIGRATION_1_2=object:Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table ComercioTable add column photoUrl text not null default ''")
            }
        }

        //cargar database
        database=Room
            .databaseBuilder(this, ComercioDatabase::class.java,"ComercioDatabase")
            .addMigrations(MIGRATION_1_2)
            .build()

        //inicializando Volley
        comercioAPI=ComercioAPI.getInstance(this)
    }
}