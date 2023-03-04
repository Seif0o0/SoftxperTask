package softxpert.movie.app.presentation.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import softxpert.movie.app.R
import softxpert.movie.app.data.utils.handleStatusCode
import softxpert.movie.app.domain.model.Genre
import softxpert.movie.app.domain.model.Movie
import softxpert.movie.app.domain.repository.MoviesRepository
import softxpert.movie.app.utils.DataState
import javax.inject.Inject

@HiltViewModel
class MoviesContainerViewModel @Inject constructor(
    private val application: Application, private val repo: MoviesRepository
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> get() = _loadingState

    private val _errorState = MutableStateFlow("")
    val errorState: StateFlow<String> get() = _errorState


    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> get() = _genres

    init {
        getGenres()
    }

    fun getGenres() {
        viewModelScope.launch {
            repo.fetchGenres().collectLatest { result ->
                when (result) {
                    is DataState.Loading -> {
                        _loadingState.emit(true)
                        _errorState.emit("")
                    }
                    is DataState.Success -> {
                        _loadingState.emit(false)
                        val genres = result.data!!.toMutableList()
                        val allOption = Genre(
                            id = 0,
                            name = application.getString(R.string.all_genres_option),
                            isClicked = true
                        )
                        genres.add(0, allOption)
                        _genres.emit(genres)
                    }
                    is DataState.Error -> {
                        _loadingState.emit(false)
                        _errorState.emit(
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