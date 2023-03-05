package softxpert.movie.app.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import softxpert.movie.app.R
import softxpert.movie.app.databinding.FragmentSearchMoviesBinding
import softxpert.movie.app.presentation.adapter.ListLoadStateAdapter
import softxpert.movie.app.presentation.adapter.MoviesAdapter
import softxpert.movie.app.presentation.utils.AutoFitGridLayoutManager
import softxpert.movie.app.presentation.utils.ListItemClickListener
import softxpert.movie.app.presentation.utils.RetryClickListener
import softxpert.movie.app.presentation.viewmodel.SearchMoviesViewModel
import softxpert.movie.app.utils.getScreenSize
import javax.inject.Inject

@AndroidEntryPoint
class SearchMoviesFragment : Fragment() {
    private val viewModel: SearchMoviesViewModel by viewModels()
    private lateinit var binding: FragmentSearchMoviesBinding

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    @Inject
    lateinit var loadStateAdapter: ListLoadStateAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchMoviesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.retryListener = RetryClickListener { moviesAdapter.retry() }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.searchEdittext.setText(SearchMoviesFragmentArgs.fromBundle(requireArguments()).query)

        moviesAdapter.setOnClickListener(ListItemClickListener {
            if (findNavController().currentDestination?.id == R.id.searchMoviesFragment) {
                findNavController().navigate(
                    SearchMoviesFragmentDirections.actionSearchMoviesFragmentToMovieDetailsFragment(
                        it.id
                    )
                )
            }
        })

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

            adapter = moviesAdapter.withLoadStateFooter(
                footer = loadStateAdapter
            )
        }

        binding.searchIcon.setOnClickListener {
            if (!viewModel.loadingState.value)
                viewModel.queryFlow.value = binding.searchEdittext.text.toString()
        }

        binding.searchEdittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!viewModel.loadingState.value)
                    viewModel.queryFlow.value = binding.searchEdittext.text.toString()
                true
            } else
                false
        }

        lifecycleScope.launchWhenStarted {
            viewModel.movies.collectLatest {
                moviesAdapter.submitData(it)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
        return binding.root
    }
}