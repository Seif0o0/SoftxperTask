package softxpert.movie.app.data.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import softxpert.movie.app.data.model.MovieDetailsDto
import softxpert.movie.app.data.response.CastResponse
import softxpert.movie.app.data.response.GenresResponse
import softxpert.movie.app.data.response.ImagesResponse
import softxpert.movie.app.data.response.MoviesResponse
import softxpert.movie.app.utils.Constants

interface MoviesService {
    @GET(Constants.GENRES)
    suspend fun fetchGenres(
        @Query(Constants.API_KEY_) apiKey: String = Constants.API_KEY
    ): Response<GenresResponse>

    @GET(Constants.MOVIES)
    suspend fun fetchMovies(
        @QueryMap map: Map<String, String>
    ): Response<MoviesResponse>

    @GET("${Constants.MOVIE_ROOT}{${Constants.MOVIE_ID_KEY}}")
    suspend fun fetchMovieDetails(
        @Path(Constants.MOVIE_ID_KEY) movieId: Int,
        @Query(Constants.API_KEY_) apiKey: String = Constants.API_KEY
    ): Response<MovieDetailsDto>

    @GET("${Constants.MOVIE_ROOT}{${Constants.MOVIE_ID_KEY}}${Constants.IMAGES}")
    suspend fun fetchMovieImages(
        @Path(Constants.MOVIE_ID_KEY) movieId: Int,
        @Query(Constants.API_KEY_) apiKey: String = Constants.API_KEY
    ): Response<ImagesResponse>

    @GET("${Constants.MOVIE_ROOT}{${Constants.MOVIE_ID_KEY}}${Constants.CAST}")
    suspend fun fetchMovieCast(
        @Path(Constants.MOVIE_ID_KEY) movieId: Int,
        @Query(Constants.API_KEY_) apiKey: String = Constants.API_KEY
    ): Response<CastResponse>


}