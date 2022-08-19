package pe.idat.mainModule.model

import androidx.lifecycle.MutableLiveData
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pe.idat.ComercioApplication
import pe.idat.common.entities.ComercioEntity

class MainInteractor
{
    /*
    //definimos un interfaz
    interface ComerciosCallback {
        fun getComerciosCallback(comercios:MutableList<ComercioEntity>)
    } */

    /*
    //funcion para el ViewModel
    fun getComerciosCallback(callback:ComerciosCallback)
    {
        //ejecutar hilo (cargar colección)
        doAsync {
            val comercioDB= ComercioApplication.database.ComercioDao().findAllDB()

            uiThread {
                //respuesta para el ViewModel
                callback.getComerciosCallback(comercioDB)
            }
        }
    } */

    fun getComercios(callback:(MutableList<ComercioEntity>) -> Unit)
    {
        //ejecutar hilo (cargar colección)
        doAsync {
            val comercioDB= ComercioApplication.database.ComercioDao().findAllDB()

            uiThread {
                //respuesta para el ViewModel
                callback(comercioDB)
            }
        }
    }

    fun deleteComercio(comercioEntity:ComercioEntity, callback:(ComercioEntity) -> Unit)
    {
        //procede con la eliminacion
        doAsync {
            ComercioApplication.database.ComercioDao().deleteDB(comercioEntity)

            uiThread {
                callback(comercioEntity)
            }
        }
    }

    fun updateComercio(comercioEntity:ComercioEntity, callback:(ComercioEntity) -> Unit)
    {
        doAsync {
            ComercioApplication.database.ComercioDao().updateDB(comercioEntity)

            uiThread {
                callback(comercioEntity)
            }
        }
    }
}