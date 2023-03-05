package softxpert.movie.app.presentation.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import softxpert.movie.app.data.utils.handleStatusCode
import softxpert.movie.app.domain.model.Movie
import softxpert.movie.app.domain.repository.SearchMoviesRepository
import javax.inject.Inject

@HiltViewModel
class SearchMoviesViewModel @Inject constructor(
    private val application: Application,
    private val repo: SearchMoviesRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    val queryFlow = MutableStateFlow(state.get<String>("query")!!)

    @OptIn(ExperimentalCoroutinesApi::class)
    var movies: Flow<PagingData<Movie>> = queryFlow.flatMapLatest { query ->
        repo.searchMovies(query.ifEmpty { null }).cachedIn(viewModelScope)
    }

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> get() = _loadingState

    private val _errorState = MutableStateFlow("")
    val errorState: StateFlow<String> get() = _errorState

    private val _emptyState = MutableStateFlow(View.GONE)
    val emptyState: StateFlow<Int> get() = _emptyState

    fun handleLoadStateListener(combinedLoadStates: CombinedLoadStates, itemCount: Int) {
        // Handle empty list state
        if (combinedLoadStates.refresh is LoadState.NotLoading) {
            _emptyState.value = if (itemCount == 0) View.VISIBLE
            else View.GONE
        }

        // Handle loading state
        _loadingState.value = combinedLoadStates.refresh is LoadState.Loading

        //  Handel error state
        if (combinedLoadStates.refresh is LoadState.Error) {
            val error = (combinedLoadStates.refresh as LoadState.Error).error.localizedMessage!!
            _errorState.value = handleStatusCode(application = application, errorCode = error)
        } else {
            _errorState.value = ""
        }
    }
}