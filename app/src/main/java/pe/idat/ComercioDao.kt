package pe.idat

import androidx.room.*

@Dao
interface ComercioDao
{
    @Insert
    fun insertDB(comercioEntity:ComercioEntity): Long

    @Update
    fun updateDB(comercioEntity:ComercioEntity)

    @Delete
    fun deleteDB(comercioEntity:ComercioEntity)

    @Query("SELECT * FROM ComercioTable WHERE productoId IN (:productoId)")
    fun findByIdDB(productoId:Int): ComercioEntity

    @Query("SELECT * FROM ComercioTable")
    fun findAllDB(): MutableList<ComercioEntity>
}