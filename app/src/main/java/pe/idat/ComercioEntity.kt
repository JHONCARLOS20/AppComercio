package pe.idat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="ComercioTable")
data class ComercioEntity(@PrimaryKey(autoGenerate = true) var productoId:Long=0,
                          var nombre:String,
                          var precio:String="",
                          var cantidad:String="",
                          var telefono:String="",
                          var direccion:String="",
                          var isFavorite:Boolean=false)
