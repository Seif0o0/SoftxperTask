package softxpert.movie.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CastDto(
    val id: Int,
    val name: String,
    @Json(name = "profile_path") val photo: String?,
    val character: String,
)