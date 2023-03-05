package softxpert.movie.app.data.response

import com.squareup.moshi.JsonClass
import softxpert.movie.app.data.model.ImageDto

@JsonClass(generateAdapter = true)
class ImagesResponse (
    val posters:List<ImageDto>
)