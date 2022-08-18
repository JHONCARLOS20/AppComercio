package pe.idat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import pe.idat.databinding.ItemComercioBinding

class ComercioAdapter(private var comercios:MutableList<ComercioEntity>,
                      private var listener:OnClickListener): RecyclerView.Adapter<ComercioAdapter.ViewHolder>()
{
    private lateinit var mContext:Context

    //clase interna que recibe una vista
    inner class ViewHolder(view:View): RecyclerView.ViewHolder(view)
    {
        //referencia a la vista item_comercio
        val binding=ItemComercioBinding.bind(view)

        //función que recibe un comercio
        fun setListener(comercioEntity:ComercioEntity)
        {
            //clik normal -> evento onClick
            binding.root.setOnClickListener {
                listener.onClick(comercioEntity)
            }

            //click largo -> evento onClickDelete
            binding.root.setOnLongClickListener {
                listener.onClickDelete(comercioEntity)
                true
            }

            //click favorite -> evento onClickFavorite
            binding.cbFavorite.setOnClickListener {
                listener.onClickFavorite(comercioEntity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        mContext=parent.context
        val view=LayoutInflater.from(mContext).inflate(R.layout.item_comercio,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val comercio=comercios.get(position)

        with(holder)
        {
            //escuchar el comercio
            setListener(comercio)

            //pintar nombre
            binding.tvNameComercio.text=comercio.nombre

            //pintar favorite
            binding.cbFavorite.isChecked=comercio.isFavorite

            //pintar imagen
            Glide.with(mContext)
                 .load(comercio.photoUrl)
                 .diskCacheStrategy(DiskCacheStrategy.ALL)
                 .centerCrop()
                 .into(binding.imgPhoto)
        }
    }

    override fun getItemCount(): Int {
        return comercios.size
    }

    fun setCollection(comerciosDB:MutableList<ComercioEntity>)
    {
        this.comercios=comerciosDB
        notifyDataSetChanged() //refrescar los cambios
    }

    fun insertMemory(comercioEntity:ComercioEntity)
    {
        comercios.add(comercioEntity)
        notifyDataSetChanged()
    }

    fun updateMemory(comercioEntity: ComercioEntity)
    {
        val index=comercios.indexOf(comercioEntity)

        if(index!=-1)
        {
            comercios.set(index,comercioEntity)
            notifyDataSetChanged()
        }
    }

    fun deleteMemory(comercioEntity: ComercioEntity)
    {
        val index=comercios.indexOf(comercioEntity)

        if(index!=-1)
        {
            comercios.removeAt(index)
            notifyItemRemoved(index) //refrescar los cambios
        }
    }
}