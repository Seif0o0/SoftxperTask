package softxpert.movie.app.presentation.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import softxpert.movie.app.data.utils.handleStatusCode
import softxpert.movie.app.domain.model.Movie
import softxpert.movie.app.domain.repository.MoviesRepository
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val application: Application, private val repo: MoviesRepository
) : ViewModel() {

    val filterFlow = MutableStateFlow<String?>(null)

    var movies: Flow<PagingData<Movie>> = filterFlow.flatMapLatest { genreId ->
        repo.fetchMovies(genreId = genreId).cachedIn(viewModelScope)
    }
//    var movies: Flow<PagingData<Movie>> = combine(filterFlow) { genre, query ->
//        var finalGenreId: String? = if (genre == null)
//            null
//        else {
//            if (genre == "0")
//                null
//            else
//                genre
//        }
//        Pair(finalGenreId, query)
//    }.flatMapLatest { (genre, query) ->
//        repo.fetchMovies(genre, query).cachedIn(viewModelScope)
//    }


    private val _moviesLoadingState = MutableStateFlow(false)
    val moviesLoadingState: StateFlow<Boolean> get() = _moviesLoadingState

    private val _moviesErrorState = MutableStateFlow("")
    val moviesErrorState: StateFlow<String> get() = _moviesErrorState

    private val _moviesEmptyState = MutableStateFlow(View.GONE)
    val moviesEmptyState: StateFlow<Int> get() = _moviesEmptyState

    fun handleLoadStateListener(combinedLoadStates: CombinedLoadStates, itemCount: Int) {
        // Handle empty list state
        if (combinedLoadStates.refresh is LoadState.NotLoading) {
            _moviesEmptyState.value = if (itemCount == 0) View.VISIBLE
            else View.GONE
        }

        // Handle loading state
        _moviesLoadingState.value = combinedLoadStates.refresh is LoadState.Loading

        //  Handel error state
        if (combinedLoadStates.refresh is LoadState.Error) {
            val error = (combinedLoadStates.refresh as LoadState.Error).error.localizedMessage!!
            _moviesErrorState.value = handleStatusCode(application = application, errorCode = error)
        } else {
            _moviesErrorState.value = ""
        }
    }
}