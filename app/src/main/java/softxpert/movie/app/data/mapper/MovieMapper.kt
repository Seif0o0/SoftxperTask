package softxpert.movie.app.data.mapper

import softxpert.movie.app.data.model.MovieDto
import softxpert.movie.app.domain.model.Movie

fun MovieDto.toMovie() = Movie(
    id = id,
    poster = poster ?: "",
    title = title,
    releaseDate = releaseDate ?: "",
)