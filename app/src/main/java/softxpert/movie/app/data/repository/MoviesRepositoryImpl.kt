package softxpert.movie.app.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import softxpert.movie.app.data.mapper.toGenre
import softxpert.movie.app.data.paging.MoviesPagingSource
import softxpert.movie.app.data.services.MoviesService
import softxpert.movie.app.domain.model.Genre
import softxpert.movie.app.domain.model.Movie
import softxpert.movie.app.domain.repository.MoviesRepository
import softxpert.movie.app.utils.Constants
import softxpert.movie.app.utils.DataState
import java.io.IOException
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val service: MoviesService
) : MoviesRepository {
    override fun fetchGenres(): Flow<DataState<List<Genre>>> {
        return flow {
            try {
                emit(DataState.Loading())
                val response = service.fetchGenres()
                if (response.isSuccessful) {
                    val genresDto = response.body()!!.genres
                    val genres = genresDto.map { it.toGenre() }
                    emit(DataState.Success(data = genres))
                } else {
                    emit(DataState.Error(message = response.code().toString()))
                }
            } catch (e: IOException) {
                emit(DataState.Error(message = Constants.CONNECTION_ERROR_KEY))
            } catch (e: Exception) {
                emit(DataState.Error(message = e.message!!))
            }
        }
    }

    @Inject
    lateinit var pagingResource: MoviesPagingSource
    override fun fetchMovies(genreId: String?): Flow<PagingData<Movie>> {
        genreId?.let {
            pagingResource.setGenreId(it)
        }
        return Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)) {
            pagingResource
        }.flow
    }

}