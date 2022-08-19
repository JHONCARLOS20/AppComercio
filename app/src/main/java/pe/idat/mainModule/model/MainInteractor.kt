package pe.idat.mainModule.model

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pe.idat.ComercioApplication
import pe.idat.common.entities.ComercioEntity

class MainInteractor
{
    //definimos un interfaz
    interface ComerciosCallback {
        fun getComerciosCallback(comercios:MutableList<ComercioEntity>)
    }

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
    }
}