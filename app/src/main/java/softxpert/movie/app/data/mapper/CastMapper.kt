package softxpert.movie.app.data.mapper

import softxpert.movie.app.data.model.CastDto
import softxpert.movie.app.domain.model.Cast

fun CastDto.toCast() = Cast(
    id = id,
    name = name,
    photo = photo ?: "",
    character = character
)