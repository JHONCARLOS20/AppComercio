package pe.idat

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pe.idat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener, MainAux
{
    lateinit var mBinding:ActivityMainBinding
    private lateinit var mAdapter:ComercioAdapter
    private lateinit var mGridLayout:GridLayoutManager

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

        //configurar el RecyclerView
        mAdapter=ComercioAdapter(mutableListOf(),this)
        mGridLayout=GridLayoutManager(this,2)

        //ejecutar hilo (cargar colección)
        doAsync {
            val comercioDB=ComercioApplication.database.ComercioDao().findAllDB()

            uiThread {
                mAdapter.setCollection(comercioDB)
            }
        }

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            adapter=mAdapter
            layoutManager=mGridLayout
        }
    }

    override fun onClick(comercioEntity: ComercioEntity)
    {
        val bundle=Bundle()
        bundle.putLong("keyId",comercioEntity.productoId)

        launchFragment(bundle)
    }

    override fun onClickFavorite(comercioEntity: ComercioEntity)
    {
        comercioEntity.isFavorite=!comercioEntity.isFavorite

        doAsync {
            ComercioApplication.database.ComercioDao().updateDB(comercioEntity)

            uiThread {
                mAdapter.updateMemory(comercioEntity)
            }
        }
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
                    1 -> Toast.makeText(this,"Llamar...",Toast.LENGTH_LONG).show()
                    2 -> Toast.makeText(this,"Sitio web...",Toast.LENGTH_LONG).show()
                }
            }).show()
    }

    //Lanzar Fragmento
    private fun launchFragment(bundle: Bundle?=null)
    {
        val fragment=ComercioFragment()

        if(bundle!=null){
            fragment.arguments=bundle
        }

        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()

        //transaccion para el fragmentos
        fragmentTransaction.add(R.id.containerMain,fragment)

        //volver fragmento principal
        fragmentTransaction.addToBackStack(null)

        //transaccion que aplique los cambios
        fragmentTransaction.commit()

        //ocultar boton flotante
        mBinding.fabComercio.hide()
    }

    override fun insertMemory(comercioEntity: ComercioEntity) {
        mAdapter.insertMemory(comercioEntity)
    }

    override fun updateMemory(comercioEntity: ComercioEntity) {
        mAdapter.updateMemory(comercioEntity)
    }
    private fun confirmarDelete(comercioEntity: ComercioEntity)
    {
        //ventana de dialogo
        MaterialAlertDialogBuilder(this)
            .setTitle("¿Eliminar Plato?")
            .setPositiveButton("Eliminar",
                DialogInterface.OnClickListener { dialogInterface, i ->

                    //Yes
                    //procede con la eliminacion
                    doAsync {
                        ComercioApplication.database.ComercioDao().deleteDB(comercioEntity)

                        uiThread {
                            mAdapter.deleteMemory(comercioEntity)
                        }
                    }
                })
            .setNegativeButton("Cancelar",null).show()
    }
}
