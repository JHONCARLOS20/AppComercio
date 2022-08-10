package pe.idat

interface OnClickListener
{
    fun onClick(comercioEntity: ComercioEntity)
    fun onClickFavorite(comercioEntity: ComercioEntity)
    fun onClickDelete(comercioEntity: ComercioEntity)
}