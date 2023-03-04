package softxpert.movie.app.presentation.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import softxpert.movie.app.data.utils.handleStatusCode
import softxpert.movie.app.databinding.LoadMoreProgressViewBinding
import softxpert.movie.app.presentation.utils.RetryClickListener
import javax.inject.Inject

class ListLoadStateAdapter @Inject constructor(private val application: Application) :
    LoadStateAdapter<ListLoadStateAdapter.ViewHolder>() {
    private lateinit var retryListener: RetryClickListener

    fun setOnRetryClickListener(retryClickListener: RetryClickListener) {
        retryListener = retryClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        var errorMessage = ""
        val loadingStatus: Boolean
        val errorVisibility: Int

        if (loadState is LoadState.Loading) {
            loadingStatus = true
            errorVisibility = View.GONE
        } else {
            loadingStatus = false
            errorVisibility = View.VISIBLE

            if (loadState is LoadState.Error) {
                errorMessage = handleStatusCode(application = application, errorCode = loadState.error.localizedMessage!!)
            }
        }
        holder.bind(errorMessage, errorVisibility, loadingStatus, retryListener)
    }

    class ViewHolder private constructor(private val binding: LoadMoreProgressViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            errorMessage: String,
            errorVisibility: Int,
            loadingStatus: Boolean,
            retryListener: RetryClickListener
        ) {
            binding.errorMessage = errorMessage
            binding.errorVisibility = errorVisibility
            binding.loadingStatus = loadingStatus
            binding.retryListener = retryListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(
                    LoadMoreProgressViewBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }
}
