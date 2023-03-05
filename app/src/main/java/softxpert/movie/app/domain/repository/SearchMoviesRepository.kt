package softxpert.movie.app.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import softxpert.movie.app.domain.model.Movie

interface SearchMoviesRepository {
    fun searchMovies(query:String?):Flow<PagingData<Movie>>
}