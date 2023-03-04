package softxpert.movie.app.presentation.utils

open class RetryClickListener(val clickListener: () -> Unit) {
    fun onRetry() = clickListener()
}