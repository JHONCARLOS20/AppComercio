package pe.idat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pe.idat.databinding.FragmentComercioBinding

//Escenario para el diseño de la vista Registrar y Editar
class ComercioFragment : Fragment()
{
    private lateinit var mBinding: FragmentComercioBinding
    private var mActivity:MainActivity?=null

    lateinit var mAdapter:ComercioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        mBinding = FragmentComercioBinding.inflate(inflater,container, false)
        return mBinding.root
    }

    //representa el ciclo de vida del fragmento
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val productoId=arguments?.getLong("keyId",0)

        if(productoId!= null && productoId!=0L){
            Toast.makeText(activity,productoId.toString(),Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(activity,productoId.toString(),Toast.LENGTH_SHORT).show()
        }

        mActivity = activity as? MainActivity

        //mostrar flecha de retroceso
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //mostrar titulo
        mActivity?.supportActionBar?.title=getString(R.string.comercio_title_add)

        //acceso al menu
        setHasOptionsMenu(true)

        //configurar para insertar imagenes
        mBinding.ietPhotoUrl.addTextChangedListener {
            Glide.with(this).load(mBinding.ietPhotoUrl.text.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(mBinding.imgComercio)
        }
    }

    //lamar al menu al momento de empezar la actividad
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.menu_save,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //ejecutar eventos dentro del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when(item.itemId)
        {
            android.R.id.home -> {
                //code de la fecha de retroceso
                mActivity?.onBackPressed()
                true
            }

            R.id.action_save -> {
                //code para el boton save

                val comercio=ComercioEntity(nombre = mBinding.ietName.text.toString().trim(),
                                            precio = mBinding.ietprice.text.toString().trim(),
                                            cantidad = mBinding.ietCantidad.text.toString().trim(),
                                            telefono = mBinding.ietPhone.text.toString().trim(),
                                            direccion = mBinding.ietDireccion.text.toString().trim(),
                                            photoUrl = mBinding.ietPhotoUrl.text.toString().trim())

                doAsync {
                    comercio.productoId=ComercioApplication.database.ComercioDao().insertDB(comercio)

                    uiThread {

                        //mAdapter?.insertMemory(comercio)
                        mActivity?.insertMemory(comercio)

                        hidekeyboard()

                        Snackbar.make(mBinding.root,getString(R.string.comercio_save),Snackbar.LENGTH_SHORT).show()

                        //volver al principal
                        mActivity?.onBackPressed()
                    }
                }
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    //no funciona
    //se ejecuta antes del onDestroy
    override fun onDestroyView() {
        hidekeyboard() //no hace el efecto
        super.onDestroyView()
    }

    //cerrar correctamente el fragmento
    override fun onDestroy()
    {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title=getString(R.string.app_name)

        mActivity?.mBinding?.fabComercio?.show()

        setHasOptionsMenu(false)

        super.onDestroy()
    }

    //metodos para ocultar teclado
    @SuppressLint("UseRequireInsteadOfGet")
    private fun hidekeyboard()
    {
        val imm=mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(view!=null)
        {
            imm.hideSoftInputFromWindow(view!!.windowToken,0)
        }
    }
}