package softxpert.movie.app.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import softxpert.movie.app.databinding.FragmentMoviesContainerBinding
import softxpert.movie.app.presentation.adapter.GenresAdapter
import softxpert.movie.app.presentation.adapter.ListLoadStateAdapter
import softxpert.movie.app.presentation.adapter.MoviesAdapter
import softxpert.movie.app.presentation.adapter.MoviesContainerAdapter
import softxpert.movie.app.presentation.utils.ListItemClickListener
import softxpert.movie.app.presentation.utils.RetryClickListener
import softxpert.movie.app.presentation.viewmodel.MoviesContainerViewModel
import softxpert.movie.app.presentation.viewmodel.MoviesViewModel
import softxpert.movie.app.utils.Pager2_ZoomOutTransformer
import javax.inject.Inject

@AndroidEntryPoint
class MoviesContainerFragment : Fragment() {
    private val viewModel: MoviesContainerViewModel by viewModels()
    private lateinit var binding: FragmentMoviesContainerBinding
    private lateinit var switchBtn: ViewPager2.OnPageChangeCallback

    @Inject
    lateinit var genresAdapter: GenresAdapter

    private lateinit var moviesContainerAdapter: MoviesContainerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesContainerBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.retryListener = RetryClickListener {
            viewModel.getGenres()
        }
        binding.lifecycleOwner = viewLifecycleOwner



        genresAdapter.setOnClickListener(onItemClickListener = ListItemClickListener {
            genresAdapter.updateGenre(it.id)
            binding.viewPager.currentItem = genresAdapter.getGenrePosition(it)
        })

        binding.genres.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = genresAdapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.genres.collectLatest {
                genresAdapter.submitList(it)

                switchBtn = object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        genresAdapter.updateGenreByPosition(position)
                        binding.genres.smoothScrollToPosition(position)
                    }
                }

                moviesContainerAdapter = MoviesContainerAdapter(
                    fragment = this@MoviesContainerFragment, itemsCount = it.size, genres = it
                )
                binding.viewPager.apply {
                    adapter = moviesContainerAdapter
                    registerOnPageChangeCallback(switchBtn)
                    setPageTransformer(Pager2_ZoomOutTransformer())
                }

            }
        }

        binding.searchIcon.setOnClickListener {
            val query = binding.searchEdittext.text.toString()
            if (query.isNotEmpty())
                findNavController().navigate(
                    MoviesContainerFragmentDirections.actionMoviesFragmentToSearchMoviesFragment(
                        query = query
                    )
                )
        }

        binding.searchEdittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEdittext.text.toString()
                if (query.isNotEmpty())
                    findNavController().navigate(
                        MoviesContainerFragmentDirections.actionMoviesFragmentToSearchMoviesFragment(
                            query = query
                        )
                    )
                true
            } else
                false
        }


        return binding.root
    }
}