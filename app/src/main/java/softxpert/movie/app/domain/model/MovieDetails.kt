package softxpert.movie.app.domain.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val rate: Float,
    val releaseDate: String,
    val overview: String,
)