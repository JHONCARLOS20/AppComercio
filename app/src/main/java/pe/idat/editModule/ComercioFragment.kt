package pe.idat.editModule

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import pe.idat.ComercioApplication
import pe.idat.mainModule.MainActivity
import pe.idat.R
import pe.idat.common.entities.ComercioEntity
import pe.idat.databinding.FragmentComercioBinding
import pe.idat.editModule.viewModel.ComercioViewModel

//Escenario para el diseño de la vista Registrar y Editar
class ComercioFragment : Fragment()
{
    private lateinit var mBinding: FragmentComercioBinding
    private var mActivity: MainActivity?=null

    private var mIsEditComercioMode:Boolean=false
    private lateinit var mComercioEntity:ComercioEntity

    //MVVM
    private lateinit var mComercioViewModel:ComercioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inicializando
        mComercioViewModel=ViewModelProvider(requireActivity()).get(ComercioViewModel::class.java)
    }

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

        //val productoId=arguments?.getLong("keyId",0)

        /*
        if(productoId!= null && productoId!=0L)
        {
            //modo registrar
            mIsEditComercioMode=true

            doAsync {
                mComercioEntity= ComercioApplication.database.ComercioDao().findByIdDB(productoId.toInt())
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
        } */

        //actionBar()

        //configurar para insertar imagenes
        /*mBinding.ietPhotoUrl.addTextChangedListener {
            Glide.with(this).load(mBinding.ietPhotoUrl.text.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(mBinding.imgComercio)
        }*/

        //llamar
        setupViewModel()

        mBinding.ietName.addTextChangedListener {
            validateOther(mBinding.tilName)
        }

        mBinding.ietprice.addTextChangedListener {
            validateOther(mBinding.tilprice)
        }

        mBinding.ietCantidad.addTextChangedListener {
            validateOther(mBinding.tilCantidad)
        }

        mBinding.ietPhone.addTextChangedListener {
            validateOther(mBinding.tilPhone)
        }

        mBinding.ietDireccion.addTextChangedListener {
            validateOther(mBinding.tilDireccion)
        }

        mBinding.ietPhotoUrl.addTextChangedListener {
            //validateOther(mBinding.tilPhotoUrl)

            //codigo para mostrar imagen por defecto si no hay contenido
            //code...

            if(validateOther(mBinding.tilPhotoUrl))
            {
                Glide.with(this)
                    .load(mBinding.ietPhotoUrl.text.toString())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(mBinding.imgComercio)
            }
            else
            {
                mBinding.imgComercio.setImageResource(R.drawable.ic_imagen)
            }
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

                //si no hay nada de que validar
                if(validateOther(mBinding.tilPhotoUrl, mBinding.tilDireccion, mBinding.tilPhone, mBinding.tilCantidad, mBinding.tilprice, mBinding.tilName))
                {
                    with(mComercioEntity)
                    {
                        nombre = mBinding.ietName.text.toString().trim()
                        precio = mBinding.ietprice.text.toString().trim()
                        cantidad = mBinding.ietCantidad.text.toString().trim()
                        telefono = mBinding.ietPhone.text.toString().trim()
                        direccion = mBinding.ietDireccion.text.toString().trim()
                        photoUrl = mBinding.ietPhotoUrl.text.toString().trim()
                    }

                    /* //code para el boton save
                    val comercio= ComercioEntity(nombre = mBinding.ietName.text.toString().trim(),
                        precio = mBinding.ietprice.text.toString().trim(),
                        cantidad = mBinding.ietCantidad.text.toString().trim(),
                        telefono = mBinding.ietPhone.text.toString().trim(),
                        direccion = mBinding.ietDireccion.text.toString().trim(),
                        photoUrl = mBinding.ietPhotoUrl.text.toString().trim()) */

                    if(mIsEditComercioMode)
                    {
                        //editar

                        mComercioViewModel.updateComercio(mComercioEntity)
                    }
                    else
                    {
                        //registrar
                        mComercioViewModel.saveComercio(mComercioEntity)
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

        //mActivity?.mBinding?.fabComercio?.show()
        mComercioViewModel.setShowFab(true) //visualizar

        mComercioViewModel.setResult(Any())

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

    //otra forma de validar
    private fun validateOther(vararg txtArray:TextInputLayout):Boolean
    {
        var isValid=true

        for(txt in txtArray)
        {
            if(txt.editText?.text.toString().trim().isEmpty())
            {
                txt.error=getString(R.string.helper_required)
                txt.editText?.requestFocus()
                isValid=false
            }
            else
            {
                txt.error=null
            }
        }

        return isValid
    }

    private fun actionBar()
    {
        mActivity=activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //titulo segun la accion
        mActivity?.supportActionBar?.title=
            if(mIsEditComercioMode)
            {
                getString(R.string.title_edit)
            }
            else
            {
                getString(R.string.comercio_title_add)
            }

        setHasOptionsMenu(true)

    }

    private fun setupViewModel()
    {
        mComercioViewModel.getComercioSelected().observe(viewLifecycleOwner,{
            mComercioEntity=it

            if(it.productoId!=0L)
            {
                mIsEditComercioMode=true //modo Editar

                with(mBinding)
                {
                    ietName.text=it?.nombre?.editable()
                    ietprice.text=it?.precio?.editable()
                    ietCantidad.text=it?.cantidad?.editable()
                    ietPhone.text=it?.telefono?.editable()
                    ietDireccion.text=it?.direccion?.editable()
                    ietPhotoUrl.text=it?.photoUrl?.editable()
                }
            }
            else
            {
                mIsEditComercioMode=false //modo Registrar
            }

            mActivity = activity as? MainActivity

            //mostrar flecha de retroceso
            mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

            //mostrar titulo
            mActivity?.supportActionBar?.title=getString(R.string.comercio_title_add)

            //acceso al menu
            setHasOptionsMenu(true)

            //titutlo segun la accion
            mActivity?.supportActionBar?.title=
                if(mIsEditComercioMode)
                {
                    getString(R.string.title_edit)
                }
                else
                {
                    getString(R.string.comercio_title_add)
                }
        })

        mComercioViewModel.getResult().observe(viewLifecycleOwner,{result->
            hidekeyboard()

            when(result)
            {
                //registrar
                is Long -> {
                    mComercioEntity.productoId=result
                    mComercioViewModel.setComercioSelected(mComercioEntity)

                    Toast.makeText(mActivity,R.string.comercio_save,Toast.LENGTH_SHORT).show()
                    mActivity?.onBackPressed()
                }

                //editar
                is ComercioEntity -> {
                    mComercioViewModel.setComercioSelected(mComercioEntity)
                    Toast.makeText(mActivity,R.string.comercio_update,Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}