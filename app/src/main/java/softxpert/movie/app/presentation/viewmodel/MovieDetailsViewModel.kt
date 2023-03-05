package softxpert.movie.app.presentation.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import softxpert.movie.app.data.utils.handleStatusCode
import softxpert.movie.app.domain.model.Cast
import softxpert.movie.app.domain.model.Image
import softxpert.movie.app.domain.model.MovieDetails
import softxpert.movie.app.domain.repository.MovieDetailsRepository
import softxpert.movie.app.utils.DataState
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val application: Application,
    private val repo: MovieDetailsRepository,
    private val state: SavedStateHandle
) : ViewModel() {
    private val movieId = state.get<Int>("movieId")!!

    private val _images = MutableStateFlow<List<Image>>(emptyList())
    val images: StateFlow<List<Image>> get() = _images

    private val _imagesLoadingState = MutableStateFlow(false)
    val imagesLoadingState: StateFlow<Boolean> get() = _imagesLoadingState

    private val _imagesErrorState = MutableStateFlow("")
    val imagesErrorState: StateFlow<String> get() = _imagesErrorState

    private val _imagesEmptyState = MutableStateFlow(View.INVISIBLE)
    val imagesEmptyState: StateFlow<Int> get() = _imagesEmptyState

    private val _cast = MutableStateFlow<List<Cast>>(emptyList())
    val cast: StateFlow<List<Cast>> get() = _cast

    private val _castLoadingState = MutableStateFlow(false)
    val castLoadingState: StateFlow<Boolean> get() = _castLoadingState

    private val _castErrorState = MutableStateFlow("")
    val castErrorState: StateFlow<String> get() = _castErrorState

    private val _castEmptyState = MutableStateFlow(View.INVISIBLE)
    val castEmptyState: StateFlow<Int> get() = _castEmptyState


    private val _movieDetails = MutableStateFlow<MovieDetails?>(null)
    val movieDetails: StateFlow<MovieDetails?> get() = _movieDetails

    private val _movieDetailsLoadingState = MutableStateFlow(false)
    val movieDetailsLoadingState: StateFlow<Boolean> get() = _movieDetailsLoadingState

    private val _movieDetailsErrorState = MutableStateFlow("")
    val movieDetailsErrorState: StateFlow<String> get() = _movieDetailsErrorState

    init {
        getMovieImages()
        getMovieDetails()
        getMovieCast()
    }

    fun getMovieDetails() {
        viewModelScope.launch {
            repo.fetchMovieDetails(movieId = movieId).collectLatest { result ->
                when (result) {
                    is DataState.Loading -> {
                        _movieDetailsLoadingState.emit(true)
                        _movieDetailsErrorState.emit("")
                    }
                    is DataState.Success -> {
                        _movieDetailsLoadingState.emit(false)
                        _movieDetails.emit(result.data!!)
                    }
                    is DataState.Error -> {
                        _movieDetailsLoadingState.emit(false)
                        _movieDetailsErrorState.emit(
                            handleStatusCode(
                                application = application,
                                errorCode = result.message!!
                            )
                        )
                    }
                }
            }
        }
    }

    fun getMovieImages() {
        viewModelScope.launch {
            repo.fetchMovieImages(movieId = movieId).collectLatest { result ->
                when (result) {
                    is DataState.Loading -> {
                        _imagesLoadingState.emit(true)
                        _imagesErrorState.emit("")
                    }
                    is DataState.Success -> {
                        _imagesLoadingState.emit(false)
                        val images = result.data!!
                        if (images.isEmpty()) {
                            _imagesEmptyState.emit(View.GONE)
                        } else {
                            _imagesEmptyState.emit(View.VISIBLE)
                            _images.emit(images)
                        }
                    }
                    is DataState.Error -> {
                        _imagesLoadingState.emit(false)
                        _imagesErrorState.emit(
                            handleStatusCode(
                                application = application,
                                errorCode = result.message!!
                            )
                        )
                    }
                }
            }
        }
    }

    fun getMovieCast() {
        viewModelScope.launch {
            repo.fetchMovieCast(movieId = movieId).collectLatest { result ->
                when (result) {
                    is DataState.Loading -> {
                        _castLoadingState.emit(true)
                        _castErrorState.emit("")
                    }
                    is DataState.Success -> {
                        _castLoadingState.emit(false)
                        val cast = result.data!!
                        if (cast.isEmpty()) {
                            _castEmptyState.emit(View.GONE)
                        } else {
                            _castEmptyState.emit(View.VISIBLE)
                            _cast.emit(cast)
                        }
                    }
                    is DataState.Error -> {
                        _castLoadingState.emit(false)
                        _castErrorState.emit(
                            handleStatusCode(
                                application = application,
                                errorCode = result.message!!
                            )
                        )
                    }
                }
            }
        }
    }
}