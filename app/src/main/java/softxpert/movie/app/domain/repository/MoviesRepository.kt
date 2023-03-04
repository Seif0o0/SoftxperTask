package softxpert.movie.app.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import softxpert.movie.app.data.response.GenresResponse
import softxpert.movie.app.domain.model.Genre
import softxpert.movie.app.domain.model.Movie
import softxpert.movie.app.utils.DataState

interface MoviesRepository {
    fun fetchGenres(): Flow<DataState<List<Genre>>>
    fun fetchMovies(genreId:String?): Flow<PagingData<Movie>>
}