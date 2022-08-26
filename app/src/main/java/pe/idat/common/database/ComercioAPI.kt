package pe.idat.common.database

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class ComercioAPI constructor(context:Context)
{
    //instancia primera vez
    companion object
    {
        private var INSTANCE:ComercioAPI?=null

        //instancia despues de la primera vez
        fun getInstance(context:Context) = INSTANCE?: synchronized(this)
        {
            INSTANCE?:ComercioAPI(context).also {
                INSTANCE=it
            }
        }
    }

    //variables que administra las operaciones de red
    val requestQueve:RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    //funcion tipo generico
    fun <T> addToRequestQueve(req:Request<T>)
    {
        requestQueve.add(req)
    }
}