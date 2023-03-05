package softxpert.movie.app.domain.repository

import kotlinx.coroutines.flow.Flow
import softxpert.movie.app.domain.model.Cast
import softxpert.movie.app.domain.model.Image
import softxpert.movie.app.domain.model.MovieDetails
import softxpert.movie.app.utils.DataState

interface MovieDetailsRepository {
    suspend fun fetchMovieImages(
        movieId: Int
    ): Flow<DataState<MutableList<Image>>>

    suspend fun fetchMovieDetails(
        movieId: Int
    ): Flow<DataState<MovieDetails>>

    suspend fun fetchMovieCast(
        movieId: Int
    ): Flow<DataState<List<Cast>>>
}