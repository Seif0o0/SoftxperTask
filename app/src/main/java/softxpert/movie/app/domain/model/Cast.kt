package softxpert.movie.app.domain.model

import com.squareup.moshi.Json

data class Cast (
    val id: Int,
    val name: String,
    val photo: String,
    val character: String,
)