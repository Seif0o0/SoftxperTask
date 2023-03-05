package softxpert.movie.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ImageDto(
    @Json(name = "file_path") val url: String,
    val width: Int,
    val height: Int

)