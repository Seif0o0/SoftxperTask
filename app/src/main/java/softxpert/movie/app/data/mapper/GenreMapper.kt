package softxpert.movie.app.data.mapper

import softxpert.movie.app.data.model.GenreDto
import softxpert.movie.app.domain.model.Genre

fun GenreDto.toGenre() = Genre(
    id = id,
    name = name
)