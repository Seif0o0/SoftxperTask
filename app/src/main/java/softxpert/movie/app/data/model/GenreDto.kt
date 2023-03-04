package softxpert.movie.app.data.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class GenreDto(val id: Int, val name: String)