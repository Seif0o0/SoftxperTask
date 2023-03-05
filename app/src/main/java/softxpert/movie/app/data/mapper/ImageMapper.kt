package softxpert.movie.app.data.mapper

import softxpert.movie.app.data.model.ImageDto
import softxpert.movie.app.domain.model.Image

fun ImageDto.toImage() = Image(
    url = url,
    width = width,
    height = height
)