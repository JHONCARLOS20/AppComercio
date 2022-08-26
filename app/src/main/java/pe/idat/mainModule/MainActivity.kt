package pe.idat.mainModule

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pe.idat.ComercioApplication
import pe.idat.R
import pe.idat.common.entities.ComercioEntity
import pe.idat.common.utils.MainAux
import pe.idat.databinding.ActivityMainBinding
import pe.idat.editModule.ComercioFragment
import pe.idat.editModule.viewModel.ComercioViewModel
import pe.idat.mainModule.adapter.ComercioAdapter
import pe.idat.mainModule.adapter.OnClickListener
import pe.idat.mainModule.viewModel.MainViewModel

//View
class MainActivity : AppCompatActivity(), OnClickListener //, MainAux
{
    lateinit var mBinding:ActivityMainBinding
    private lateinit var mAdapter: ComercioAdapter
    private lateinit var mGridLayout:GridLayoutManager

    //usando patron MVVM
    private lateinit var mMainViewModel:MainViewModel
    private lateinit var mComercioViewModel:ComercioViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //nuevo sistema de vinculación de vistas
        mBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        /*
        //evento del Button
        mBinding.btnSave.setOnClickListener {
            val comercio=ComercioEntity(nombre  =mBinding.etName.text.toString().trim())

            Thread {
                val productoId=ComercioApplication.database.ComercioDao().insertDB(comercio)
                comercio.productoId=productoId
            }.start()

            mAdapter.insertMemory(comercio)
        }
         */

        //evento del boton flotante
        mBinding.fabComercio.setOnClickListener {
            launchFragment()
        }

        //inicializar ViewModel
        setupVideModel()

        //configurar el RecyclerView
        mAdapter= ComercioAdapter(mutableListOf(),this)
        mGridLayout=GridLayoutManager(this,2)

        /*
        //ejecutar hilo (cargar colección)
        doAsync {
            val comercioDB= ComercioApplication.database.ComercioDao().findAllDB()

            uiThread {
                mAdapter.setCollection(comercioDB)
            }
        }*/

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            adapter=mAdapter
            layoutManager=mGridLayout
        }
    }

    override fun onClick(comercioEntity: ComercioEntity)
    {
        launchFragment(comercioEntity)
    }

    override fun onClickFavorite(comercioEntity: ComercioEntity)
    {
        //comercioEntity.isFavorite=!comercioEntity.isFavorite

        /*
        doAsync {
            ComercioApplication.database.ComercioDao().updateDB(comercioEntity)

            uiThread {
                mAdapter.updateMemory(comercioEntity)
            }
        } */

        mMainViewModel.updateComercio(comercioEntity)
    }

    override fun onClickDelete(comercioEntity: ComercioEntity)
    {
        //opciones
        val items= arrayOf("Eliminar","Llamar","Ir al sitio web")

        //ventana de dialogo como menu
        MaterialAlertDialogBuilder(this)
            .setTitle("¿Que deseas Hacer?")
            .setItems(items,DialogInterface.OnClickListener { dialogInterface, i ->

                when(i) {
                    0 -> confirmarDelete(comercioEntity)
                    1 -> callPhone(comercioEntity.telefono)
                    2 -> goToWebsite(comercioEntity.cantidad)
                }
            }).show()
    }

    //Lanzar Fragmento MVVM
    private fun launchFragment(comercioEntity: ComercioEntity= ComercioEntity())
    {
        val fragment= ComercioFragment()

        mComercioViewModel.setComercioSelected(comercioEntity)

        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()

        //transaccion para el fragmentos
        fragmentTransaction.add(R.id.containerMain,fragment)

        //volver fragmento principal
        fragmentTransaction.addToBackStack(null)

        //transaccion que aplique los cambios
        fragmentTransaction.commit()

        //ocultar boton flotante
        mComercioViewModel.setShowFab(false)
    }

    /*override fun insertMemory(comercioEntity: ComercioEntity) {
        mAdapter.insertMemory(comercioEntity)
    }*/

    /*override fun updateMemory(comercioEntity: ComercioEntity) {
        mAdapter.updateMemory(comercioEntity)
    }*/

    private fun confirmarDelete(comercioEntity: ComercioEntity)
    {
        //ventana de dialogo
        MaterialAlertDialogBuilder(this)
            .setTitle("¿Eliminar Plato?")
            .setPositiveButton("Eliminar",
                DialogInterface.OnClickListener { dialogInterface, i ->

                    /* //Yes
                    //procede con la eliminacion
                    doAsync {
                        ComercioApplication.database.ComercioDao().deleteDB(comercioEntity)

                        uiThread {
                            mAdapter.deleteMemory(comercioEntity)
                        }
                    } */

                    mMainViewModel.deleteComercio(comercioEntity)
                })
            .setNegativeButton("Cancelar",null).show()
    }

    private fun callPhone(phone:String)
    {
        val call=Intent().apply {
            action=Intent.ACTION_DIAL //accion de llamar a telefonos
            data=Uri.parse("tel:$phone") //data del numero que se quiere llamar
        }

        //realizar la actividad de llamadas
        startActivity(call)
    }

    private fun goToWebsite(website:String)
    {
        val call=Intent().apply {
            action=Intent.ACTION_VIEW //accion de llamar a vistas
            data=Uri.parse(website) //data website que se quiere marcar
        }

        //realizar la actividad de llamadas
        startActivity(call)
    }

    //inicializar ViewModel
    private fun setupVideModel()
    {
        //cargamos con la informacion
        mMainViewModel=ViewModelProvider(this).get(MainViewModel::class.java)

        mMainViewModel.getComercios().observe(this,{comercios ->
            mAdapter.setCollection(comercios) //cargamos al adaptador

            //uso de ProgressBar
            mBinding.progressBar.visibility=
                if(comercios.size==0)
                {
                    View.VISIBLE
                }
                else
                {
                    View.GONE
                }

        })

        //inicializado
        mComercioViewModel=ViewModelProvider(this).get(ComercioViewModel::class.java)

        //visualizacion
        mComercioViewModel.getShowFab().observe(this,{isVisible->
            if(isVisible)
            {
                //mostrar fab
                mBinding.fabComercio.show()
            }
            else
            {
                //ocultar fab
                mBinding.fabComercio.hide()
            }
        })

        //funcionamiento en memoria
        mComercioViewModel.getComercioSelected().observe(this,{comercioEntity->
            mAdapter.saveMemory(comercioEntity)
        })
    }
}
