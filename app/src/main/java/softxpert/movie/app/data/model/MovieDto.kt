package softxpert.movie.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDto (
    val id:Int,
    @Json(name = "poster_path")
    val poster: String?,
    val title: String,
    @Json(name = "release_date")
    val releaseDate: String?,
)