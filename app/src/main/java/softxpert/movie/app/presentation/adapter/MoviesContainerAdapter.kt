package softxpert.movie.app.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import softxpert.movie.app.domain.model.Genre
import softxpert.movie.app.presentation.fragment.MoviesFragment

class MoviesContainerAdapter(
    fragment: Fragment,
    private val itemsCount: Int,
    private val genres: List<Genre>
) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = itemsCount

    override fun createFragment(position: Int) = MoviesFragment.newInstance(genres[position].id)
}