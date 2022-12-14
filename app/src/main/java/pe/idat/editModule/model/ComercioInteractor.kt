package pe.idat.editModule.model

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pe.idat.ComercioApplication
import pe.idat.common.entities.ComercioEntity

//model
class ComercioInteracto
{
    fun saveComercio(comercioEntity: ComercioEntity, callback:(Long) -> Unit)
    {
        doAsync {
            val newID=ComercioApplication.database.ComercioDao().insertDB(comercioEntity)

            uiThread {
                callback(newID)
            }
        }
    }

    fun updateComercio(comercioEntity: ComercioEntity, callback: (ComercioEntity) -> Unit)
    {
        doAsync {
            ComercioApplication.database.ComercioDao().updateDB(comercioEntity)

            uiThread {
                callback(comercioEntity)
            }
        }
    }
}