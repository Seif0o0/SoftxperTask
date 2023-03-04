package softxpert.movie.app.presentation.utils

open class ListItemClickListener<T>(val clickListener: (data: T) -> Unit) {
    fun onItemClickListener(data: T) = clickListener(data)
}