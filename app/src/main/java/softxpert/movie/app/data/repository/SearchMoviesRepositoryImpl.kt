package softxpert.movie.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import softxpert.movie.app.data.paging.SearchMoviesPagingSource
import softxpert.movie.app.data.services.MoviesService
import softxpert.movie.app.domain.model.Movie
import softxpert.movie.app.domain.repository.SearchMoviesRepository
import softxpert.movie.app.utils.Constants
import javax.inject.Inject

class SearchMoviesRepositoryImpl @Inject constructor(
    private val service: MoviesService
) : SearchMoviesRepository {

    @Inject
    lateinit var pagingSource: SearchMoviesPagingSource

    override fun searchMovies(query: String?): Flow<PagingData<Movie>> {
        query?.let {
            pagingSource.setQuery(it)
        }
        return Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)) {
            pagingSource
        }.flow
    }
}