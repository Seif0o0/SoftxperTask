package softxpert.movie.app.data.mapper

import softxpert.movie.app.data.model.MovieDetailsDto
import softxpert.movie.app.data.model.MovieDto
import softxpert.movie.app.domain.model.Movie
import softxpert.movie.app.domain.model.MovieDetails

fun MovieDto.toMovie() = Movie(
    id = id,
    poster = poster ?: "",
    title = title,
    releaseDate = releaseDate ?: "",
)

fun MovieDetailsDto.toMovieDetails() = MovieDetails(
    id = id,
    title = title,
    rate = rate,
    releaseDate = releaseDate,
    overview = overview ?: ""
)