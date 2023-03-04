package softxpert.movie.app.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import softxpert.movie.app.data.model.MovieDto


@JsonClass(generateAdapter = true)
class MoviesResponse(
    val page: Int,
    @Json(name = "results") val movies: List<MovieDto>,
    @Json(name = "total_pages") val totalPages: Int
)