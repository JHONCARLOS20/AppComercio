package pe.idat

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import pe.idat.databinding.FragmentComercioBinding

//Escenario para el diseño de la vista Registrar y Editar
class ComercioFragment : Fragment()
{
    private lateinit var mBinding: FragmentComercioBinding
    private var mActivity:MainActivity?=null

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

        mActivity = activity as? MainActivity

        //mostrar flecha de retroceso
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //mostrar titulo
        mActivity?.supportActionBar?.title=getString(R.string.comercio_title_add)

        //acceso al menu
        setHasOptionsMenu(true)
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
                Snackbar.make(mBinding.root,getString(R.string.comercio_save),Snackbar.LENGTH_SHORT).show()
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}