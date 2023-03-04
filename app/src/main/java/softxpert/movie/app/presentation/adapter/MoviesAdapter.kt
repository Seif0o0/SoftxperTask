package softxpert.movie.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import softxpert.movie.app.databinding.MovieItemBinding
import softxpert.movie.app.domain.model.Movie
import softxpert.movie.app.presentation.utils.ListItemClickListener
import javax.inject.Inject

class MoviesAdapter @Inject constructor() :
    PagingDataAdapter<Movie, MoviesAdapter.ViewHolder>(MoviesComparator) {
    private lateinit var clickListener: ListItemClickListener<Movie>

    fun setOnClickListener(clickListener: ListItemClickListener<Movie>) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            character: Movie,
            clickListener: ListItemClickListener<Movie>
        ) {
            binding.movie = character
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(
                    MovieItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    object MoviesComparator : DiffUtil.ItemCallback<Movie>() {
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
            oldItem.id == newItem.id
    }

}