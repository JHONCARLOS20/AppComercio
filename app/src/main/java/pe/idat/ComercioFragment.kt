package pe.idat

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import pe.idat.databinding.FragmentComercioBinding

//Escenario para el diseño de la vista Registrar y Editar
class ComercioFragment : Fragment()
{
    private lateinit var mBinding: FragmentComercioBinding

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

        val activity = activity as? MainActivity

        //mostrar flecha de retroceso
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //mostrar titulo
        activity?.supportActionBar?.title=getString(R.string.comercio_title_add)

        //acceso al menu
        setHasOptionsMenu(true)
    }

    //lamar al menu al momento de empezar la actividad
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.menu_save,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}