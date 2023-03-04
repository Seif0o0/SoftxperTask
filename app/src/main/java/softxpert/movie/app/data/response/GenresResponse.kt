package softxpert.movie.app.data.response

import com.squareup.moshi.JsonClass
import softxpert.movie.app.data.model.GenreDto

@JsonClass(generateAdapter = true)
class GenresResponse(
    val genres: List<GenreDto>
)