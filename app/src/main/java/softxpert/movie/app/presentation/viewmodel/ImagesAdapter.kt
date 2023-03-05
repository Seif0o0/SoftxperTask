package softxpert.movie.app.presentation.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import softxpert.movie.app.databinding.ImageItemBinding
import softxpert.movie.app.domain.model.Image
import softxpert.movie.app.utils.Constants
import javax.inject.Inject

class ImagesAdapter @Inject constructor() :
    ListAdapter<Image, ImagesAdapter.ViewHolder>(ImagesComparator) {

    private var imageWidth = Constants.POSTER_ORIGINAL
    fun setImageWidth(width: String) {
        imageWidth = width
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), imageWidth)
    }

    class ViewHolder private constructor(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Image, width: String) {
            binding.image = image
            binding.width = width
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(
                    ImageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    object ImagesComparator : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image) =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Image, newItem: Image) =
            oldItem == newItem
    }
}