package softxpert.movie.app.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import softxpert.movie.app.databinding.FragmentMoviesBinding
import softxpert.movie.app.presentation.adapter.ListLoadStateAdapter
import softxpert.movie.app.presentation.adapter.MoviesAdapter
import softxpert.movie.app.presentation.utils.AutoFitGridLayoutManager
import softxpert.movie.app.presentation.utils.ListItemClickListener
import softxpert.movie.app.presentation.utils.RetryClickListener
import softxpert.movie.app.presentation.viewmodel.MoviesViewModel
import softxpert.movie.app.utils.getScreenSize
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var binding: FragmentMoviesBinding

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    @Inject
    lateinit var loadStateAdapter: ListLoadStateAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        val genreId = requireArguments().getInt("genreId")
        viewModel.filterFlow.value = if (genreId == 0) null else "$genreId"
        binding.viewModel = viewModel
        binding.retryListener = RetryClickListener {
            moviesAdapter.retry()
        }
        binding.lifecycleOwner = viewLifecycleOwner

        moviesAdapter.setOnClickListener(ListItemClickListener { })
        moviesAdapter.addLoadStateListener { combinedLoadStates ->
            viewModel.handleLoadStateListener(combinedLoadStates, moviesAdapter.itemCount)
        }
        loadStateAdapter.setOnRetryClickListener(RetryClickListener {
            moviesAdapter.retry()
        })

        binding.movies.apply {
            var screenSize = if (Build.VERSION.SDK_INT < 30) getScreenSize(resources = resources)
            else getScreenSize(
                resources = resources,
                windowMetrics = requireContext().applicationContext.getSystemService(
                    WindowManager::class.java
                ).maximumWindowMetrics
            )
            layoutManager = AutoFitGridLayoutManager(requireContext(), screenSize.first)
//                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = moviesAdapter.withLoadStateFooter(
                footer = loadStateAdapter
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.movies.collectLatest {
                moviesAdapter.submitData(it)
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(genreId: Int) = MoviesFragment().apply {
            arguments = Bundle().apply {
                putInt("genreId", genreId)
            }
        }
    }
}