package softxpert.movie.app.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import softxpert.movie.app.databinding.GenreItemBinding
import softxpert.movie.app.domain.model.Genre
import softxpert.movie.app.presentation.utils.ListItemClickListener
import javax.inject.Inject

class GenresAdapter @Inject constructor() :
    ListAdapter<Genre, GenresAdapter.ViewHolder>(GenresComparator) {

    private lateinit var clickListener: ListItemClickListener<Genre>

    fun setOnClickListener(onItemClickListener: ListItemClickListener<Genre>) {
        this.clickListener = onItemClickListener
    }

    fun getGenrePosition(genre: Genre): Int {
        return currentList.indexOf(genre)
    }
    fun updateGenreByPosition(position: Int) {
        val currentMutableList = currentList.toMutableList()
        val prevClickedGenreIndex = currentMutableList.indexOfFirst { item ->
            item.isClicked
        }

        if (prevClickedGenreIndex == -1)
            return
        currentMutableList[prevClickedGenreIndex].isClicked = false
        currentMutableList[position].isClicked = true

        submitList(currentMutableList)
        notifyItemChanged(position, Unit)
        notifyItemChanged(prevClickedGenreIndex, Unit)
    }

    fun updateGenre(genreId: Int) {
        val currentMutableList = currentList.toMutableList()
        val prevClickedGenreIndex = currentMutableList.indexOfFirst { item ->
            item.isClicked
        }
        val clickedGenreIndex = currentMutableList.indexOfFirst { item ->
            genreId == item.id
        }

        if (prevClickedGenreIndex == clickedGenreIndex)
            return

        currentMutableList[prevClickedGenreIndex].isClicked = false
        currentMutableList[clickedGenreIndex].isClicked = true

        submitList(currentMutableList)
        notifyItemChanged(clickedGenreIndex, Unit)
        notifyItemChanged(prevClickedGenreIndex, Unit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder private constructor(private val binding: GenreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Genre, clickListener: ListItemClickListener<Genre>) {
            binding.genre = genre
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(
                    GenreItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    object GenresComparator : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre) =
            oldItem == newItem
    }
}