package softxpert.movie.app.data.response

import com.squareup.moshi.JsonClass
import softxpert.movie.app.data.model.CastDto

@JsonClass(generateAdapter = true)
class CastResponse(
    val cast: List<CastDto>
)