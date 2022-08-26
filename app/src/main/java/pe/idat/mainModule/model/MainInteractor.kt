package pe.idat.mainModule.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pe.idat.ComercioApplication
import pe.idat.common.entities.ComercioEntity

//MODEL
class MainInteractor
{

    fun getComercios(callback:(MutableList<ComercioEntity>) -> Unit) {
        val url = "https://restaurantes.free.beeceptor.com/my/api/path"

        val jsonRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            Log.i("Response: ", response.toString())
        }, {
            it.printStackTrace()
        })

        ComercioApplication.comercioAPI.addToRequestQueve(jsonRequest)
    }

        /* //ejecutar hilo (cargar colección)
        doAsync {
            val comercioDB= ComercioApplication.database.ComercioDao().findAllDB()

            uiThread {

                val json =Gson().toJson(comercioDB)
                Log.i("Gson",json)

                //respuesta para el ViewModel
                callback(comercioDB)
            }
        }*/

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

    /* fun getComercios(callback:(MutableList<ComercioEntity>) -> Unit)
    {
        //ejecutar hilo (cargar colección)
        doAsync {
            val comercioDB= ComercioApplication.database.ComercioDao().findAllDB()

            uiThread {

                val json =Gson().toJson(comercioDB)
                Log.i("Gson",json)

                //respuesta para el ViewModel
                callback(comercioDB)
            }
        }
    } */

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