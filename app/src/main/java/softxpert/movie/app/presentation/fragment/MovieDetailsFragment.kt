package softxpert.movie.app.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import softxpert.movie.app.R
import softxpert.movie.app.databinding.FragmentMovieDetailsBinding
import softxpert.movie.app.presentation.adapter.CastAdapter
import softxpert.movie.app.presentation.utils.RetryClickListener
import softxpert.movie.app.presentation.viewmodel.ImagesAdapter
import softxpert.movie.app.presentation.viewmodel.MovieDetailsViewModel
import softxpert.movie.app.utils.Constants
import softxpert.movie.app.utils.getScreenSize
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private val viewModel: MovieDetailsViewModel by viewModels()
    private lateinit var binding: FragmentMovieDetailsBinding

    @Inject
    lateinit var imagesAdapter: ImagesAdapter

    @Inject
    lateinit var castAdapter: CastAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.imagesRetryClickListener = RetryClickListener { viewModel.getMovieImages() }
        binding.detailsRetryClickListener = RetryClickListener { viewModel.getMovieDetails() }
        binding.castRetryClickListener = RetryClickListener { viewModel.getMovieCast() }
        binding.lifecycleOwner = viewLifecycleOwner

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })

        lifecycleScope.launchWhenStarted {
            viewModel.movieDetails.collectLatest {
                it?.let { movieDetails ->
                    binding.movieDetails = movieDetails
                }
            }
        }

        var screenSize = if (Build.VERSION.SDK_INT < 30) getScreenSize(resources = resources).first
        else getScreenSize(
            resources = resources,
            windowMetrics = requireContext().applicationContext.getSystemService(
                WindowManager::class.java
            ).maximumWindowMetrics
        ).first

        if (screenSize < 600) {
            imagesAdapter.setImageWidth(Constants.POSTER_w500)
        } else if (screenSize < 840) {
            imagesAdapter.setImageWidth(Constants.POSTER_w780)
        } else {
            imagesAdapter.setImageWidth(Constants.POSTER_ORIGINAL)
        }
        binding.viewPager.adapter = imagesAdapter
        lifecycleScope.launchWhenStarted {
            viewModel.images.collectLatest {
                imagesAdapter.submitList(it)
            }
        }

        binding.castList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.cast.collectLatest {
                castAdapter.submitList(it)
            }
        }


        return binding.root
    }
}