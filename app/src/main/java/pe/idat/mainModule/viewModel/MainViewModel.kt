package pe.idat.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pe.idat.ComercioApplication
import pe.idat.common.entities.ComercioEntity
import pe.idat.mainModule.model.MainInteractor

//ViewModel
class MainViewModel: ViewModel()
{
    //reflejar datos de la vista
    //private var comercios:MutableLiveData<List<ComercioEntity>>

    //reflejar datos del model
    private var interactor:MainInteractor

    //bloque de inicializacion
    init {
        interactor= MainInteractor()

        //comercios= MutableLiveData() //inicializar
        loadcomercios() //cargar
    }

    //inicializacion por lazy
    private val comercios:MutableLiveData<List<ComercioEntity>> by lazy {
        MutableLiveData<List<ComercioEntity>>().also {
            loadcomercios()
        }
    }

    //encapsulando
    fun getComercios(): LiveData<List<ComercioEntity>>
    {
        return comercios
    }

    private fun loadcomercios()
    {
        /*
        //ejecutar hilo (cargar colección)
        doAsync {
            val comercioDB= ComercioApplication.database.ComercioDao().findAllDB()

            uiThread {
                comercios.value=comercioDB
            }
        } */

        interactor.getComerciosCallback(object:MainInteractor.ComerciosCallback{
            override fun getComerciosCallback(comercios: MutableList<ComercioEntity>) {
                this@MainViewModel.comercios.value=comercios
            }
        })
    }
}