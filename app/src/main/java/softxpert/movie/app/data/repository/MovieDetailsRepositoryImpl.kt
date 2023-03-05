package softxpert.movie.app.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import softxpert.movie.app.data.mapper.toCast
import softxpert.movie.app.data.mapper.toImage
import softxpert.movie.app.data.mapper.toMovieDetails
import softxpert.movie.app.data.services.MoviesService
import softxpert.movie.app.domain.model.Cast
import softxpert.movie.app.domain.model.Image
import softxpert.movie.app.domain.model.MovieDetails
import softxpert.movie.app.domain.repository.MovieDetailsRepository
import softxpert.movie.app.utils.Constants
import softxpert.movie.app.utils.DataState
import java.io.IOException
import javax.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(
    private val service: MoviesService
) : MovieDetailsRepository {
    override suspend fun fetchMovieImages(movieId: Int): Flow<DataState<MutableList<Image>>> {
        return flow {
            try {
                emit(DataState.Loading())
                val response = service.fetchMovieImages(movieId = movieId)
                if (response.isSuccessful) {

                    val body = response.body()!!

                    emit(DataState.Success(data = body.posters.map { it.toImage() }
                        .toMutableList()))
                } else {
                    emit(DataState.Error(message = response.code().toString()))
                }
            } catch (e: IOException) {
                emit(DataState.Error(message = Constants.CONNECTION_ERROR_KEY))
            } catch (e: Exception) {
                emit(DataState.Error(e.message!!))
            }
        }
    }

    override suspend fun fetchMovieDetails(movieId: Int): Flow<DataState<MovieDetails>> {
        return flow {
            try {
                emit(DataState.Loading())
                val response = service.fetchMovieDetails(movieId = movieId)
                if (response.isSuccessful) {
                    emit(DataState.Success(data = response.body()!!.toMovieDetails()))
                } else {
                    emit(DataState.Error(message = response.code().toString()))
                }
            } catch (e: IOException) {
                emit(DataState.Error(message = Constants.CONNECTION_ERROR_KEY))
            } catch (e: Exception) {
                emit(DataState.Error(e.message!!))
            }
        }
    }

    override suspend fun fetchMovieCast(movieId: Int): Flow<DataState<List<Cast>>> {
        return flow {
            try {
                emit(DataState.Loading())
                val response = service.fetchMovieCast(movieId = movieId)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    emit(DataState.Success(data = body.cast.map { it.toCast() }))
                } else {
                    emit(DataState.Error(message = response.code().toString()))
                }
            } catch (e: IOException) {
                emit(DataState.Error(message = Constants.CONNECTION_ERROR_KEY))
            } catch (e: Exception) {
                emit(DataState.Error(e.message!!))
            }
        }
    }
}