package softxpert.movie.app.data.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import softxpert.movie.app.data.response.GenresResponse
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
}