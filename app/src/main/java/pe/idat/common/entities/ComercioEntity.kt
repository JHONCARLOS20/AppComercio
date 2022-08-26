package pe.idat.common.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="ComercioTable")
data class ComercioEntity(@PrimaryKey(autoGenerate = true) var productoId:Long=0,
                          var nombre:String,
                          var precio:String,
                          var cantidad:String,
                          var telefono:String,
                          var direccion:String,
                          var photoUrl:String,
                          var isFavorite:Boolean=false)
{
    //constructor vacio
    constructor():this(nombre = "", precio = "", photoUrl = "", cantidad = "", telefono = "", direccion = "")

    //metodos para evitar la duplicidad por ID
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComercioEntity

        if (productoId != other.productoId) return false

        return true
    }

    override fun hashCode(): Int {
        return productoId.hashCode()
    }
}
