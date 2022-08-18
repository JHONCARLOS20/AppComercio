package pe.idat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pe.idat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener
{
    private lateinit var mBinding:ActivityMainBinding
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

    override fun onClick(comercioEntity: ComercioEntity) {
        //code...
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
        doAsync {
            ComercioApplication.database.ComercioDao().deleteDB(comercioEntity)

            uiThread {
                mAdapter.deleteMemory(comercioEntity)
            }
        }
    }

    //Lanzar Fragmento
    private fun launchFragment()
    {
        val fragment=ComercioFragment()
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()

        //transaccion para el fragmentos
        fragmentTransaction.add(R.id.containerMain,fragment)

        //transaccion que aplique los cambios
        fragmentTransaction.commit()

        //ocultar boton flotante
        mBinding.fabComercio.hide()
    }
}















