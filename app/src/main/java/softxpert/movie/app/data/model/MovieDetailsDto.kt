package softxpert.movie.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailsDto(
    val id: Int,
    val title: String,
    @Json(name = "vote_average")
    val rate: Float,
    @Json(name = "release_date")
    val releaseDate: String,
    val overview: String?,
)