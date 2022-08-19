package pe.idat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
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

    private var mIsEditComercioMode:Boolean=false
    private var mComercioEntity:ComercioEntity?=null

    //lateinit var mAdapter:ComercioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        mBinding = FragmentComercioBinding.inflate(inflater,container, false)
        return mBinding.root
    }

    //representa el ciclo de vida del fragmento
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val productoId=arguments?.getLong("keyId",0)

        if(productoId!= null && productoId!=0L)
        {
            //modo registrar
            mIsEditComercioMode=true

            doAsync {
                mComercioEntity=ComercioApplication.database.ComercioDao().findByIdDB(productoId.toInt())
                uiThread {
                    with(mBinding)
                    {
                        /*
                        ietName.setText(mComercioEntity?.nombre)
                        ietprice.setText(mComercioEntity?.precio)
                        ietCantidad.setText(mComercioEntity?.cantidad)
                        ietPhone.setText(mComercioEntity?.telefono)
                        ietDireccion.setText(mComercioEntity?.direccion)
                        ietPhotoUrl.setText(mComercioEntity?.photoUrl)
                        */

                        //buenas practicas
                        ietName.text=mComercioEntity?.nombre?.editable()
                        ietprice.text=mComercioEntity?.precio?.editable()
                        ietCantidad.text=mComercioEntity?.cantidad?.editable()
                        ietPhone.text=mComercioEntity?.telefono?.editable()
                        ietDireccion.text=mComercioEntity?.direccion?.editable()
                        ietPhotoUrl.text=mComercioEntity?.photoUrl?.editable()

                        /*
                        Glide.with(activity!!)
                            .load(mComercioEntity?.photoUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .into(imgComercio)
                            */
                    }
                }
            }

            //Toast.makeText(activity,productoId.toString(),Toast.LENGTH_SHORT).show()
        }
        else
        {
            //modo registrar
            mIsEditComercioMode=false

            //inicializar
            mComercioEntity= ComercioEntity(nombre = "", precio = "", cantidad = "", telefono = "", direccion = "", photoUrl = "")

            //Toast.makeText(activity,productoId.toString(),Toast.LENGTH_SHORT).show()
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

        mBinding.ietName.addTextChangedListener {
            //code...
        }

        mBinding.ietprice.addTextChangedListener {
            //code...
        }

        mBinding.ietCantidad.addTextChangedListener {
            //code...
        }

        mBinding.ietPhone.addTextChangedListener {
            //code...
        }

        mBinding.ietDireccion.addTextChangedListener {
            //code...
        }

        mBinding.ietPhotoUrl.addTextChangedListener {
            //code...
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

                if(validate())
                {
                    //code para el boton save
                    val comercio=ComercioEntity(nombre = mBinding.ietName.text.toString().trim(),
                        precio = mBinding.ietprice.text.toString().trim(),
                        cantidad = mBinding.ietCantidad.text.toString().trim(),
                        telefono = mBinding.ietPhone.text.toString().trim(),
                        direccion = mBinding.ietDireccion.text.toString().trim(),
                        photoUrl = mBinding.ietPhotoUrl.text.toString().trim())

                    doAsync {

                        if(mIsEditComercioMode)
                        {
                            //editar
                            comercio.productoId=mComercioEntity!!.productoId

                            ComercioApplication.database.ComercioDao().updateDB(comercio)
                        }
                        else
                        {
                            //registrar
                            comercio.productoId=ComercioApplication.database.ComercioDao().insertDB(comercio)
                        }

                        uiThread {

                            hidekeyboard()

                            if(mIsEditComercioMode)
                            {
                                mActivity?.updateMemory(comercio)

                                Snackbar.make(mBinding.root,getString(R.string.comercio_update),Snackbar.LENGTH_SHORT).show()
                            }
                            else
                            {
                                //mAdapter?.insertMemory(comercio)
                                mActivity?.insertMemory(comercio)



                                Snackbar.make(mBinding.root,getString(R.string.comercio_save),Snackbar.LENGTH_SHORT).show()

                                //volver al principal
                                mActivity?.onBackPressed()
                            }
                        }
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

    //buenas practicas
    private fun String.editable(): Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }

    //validar formulario
    private fun validate():Boolean
    {
        var isValid=true

        with(mBinding)
        {
            if(ietPhotoUrl.text.toString().trim().isEmpty())
            {
                mBinding.tilPhotoUrl.error=getString(R.string.helper_required)
                mBinding.ietPhotoUrl.requestFocus()
                isValid=false
            }
            else
            {
                //quitar borde rojo
                mBinding.tilPhotoUrl.error=null
            }

            if(ietDireccion.text.toString().trim().isEmpty())
            {
                mBinding.tilDireccion.error=getString(R.string.helper_required)
                mBinding.ietDireccion.requestFocus()
                isValid=false
            }
            else
            {
                //quitar borde rojo
                mBinding.tilDireccion.error=null
            }

            if(ietPhone.text.toString().trim().isEmpty())
            {
                mBinding.tilPhone.error=getString(R.string.helper_required)
                mBinding.ietPhone.requestFocus()
                isValid=false
            }
            else
            {
                //quitar borde rojo
                mBinding.tilPhone.error=null
            }

            if(ietCantidad.text.toString().trim().isEmpty())
            {
                mBinding.tilCantidad.error=getString(R.string.helper_required)
                mBinding.ietCantidad.requestFocus()
                isValid=false
            }
            else
            {
                //quitar borde rojo
                mBinding.tilCantidad.error=null
            }

            if(ietprice.text.toString().trim().isEmpty())
            {
                mBinding.tilprice.error=getString(R.string.helper_required)
                mBinding.ietprice.requestFocus()
                isValid=false
            }
            else
            {
                //quitar borde rojo
                mBinding.tilprice.error=null
            }

            if(ietName.text.toString().trim().isEmpty())
            {
                mBinding.tilName.error=getString(R.string.helper_required)
                mBinding.ietName.requestFocus()
                isValid=false
            }
            else
            {
                //quitar borde rojo
                mBinding.tilName.error=null
            }
        }

        return isValid
    }


}