package pe.idat.mainModule.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pe.idat.ComercioApplication
import pe.idat.common.entities.ComercioEntity

class MainViewModel: ViewModel()
{
    //reflejar datos de la vista
    private var comercios:MutableLiveData<List<ComercioEntity>>

    init {
        comercios= MutableLiveData() //inicializar
        loadcomercios() //cargar
    }

    //encapsulando
    fun getComercios():MutableLiveData<List<ComercioEntity>>
    {
        return comercios
    }

    private fun loadcomercios()
    {
        //ejecutar hilo (cargar colección)
        doAsync {
            val comercioDB= ComercioApplication.database.ComercioDao().findAllDB()

            uiThread {
                comercios.value=comercioDB
            }
        }
    }
}