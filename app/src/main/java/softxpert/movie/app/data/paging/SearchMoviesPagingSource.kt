package softxpert.movie.app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import softxpert.movie.app.data.mapper.toMovie
import softxpert.movie.app.data.services.MoviesService
import softxpert.movie.app.domain.model.Movie
import softxpert.movie.app.utils.Constants
import java.io.IOException
import javax.inject.Inject

class SearchMoviesPagingSource @Inject constructor(
    private val service: MoviesService
) : PagingSource<Int, Movie>() {
    var query: String? = null
        private set

    fun setQuery(query: String) {
        this.query = query
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val currentPage = params.key ?: 1
        return try {
            val map = mutableMapOf<String, String>()
            map[Constants.API_KEY_] = Constants.API_KEY
            map[Constants.PAGE_KEY] = currentPage.toString()
            query?.let { map[Constants.QUERY_KEY] = it }

            val response = service.searchMovies(map)
            if (response.isSuccessful) {
                val body = response.body()!!
                val movies = body.movies.map { it.toMovie() }
                val page = body.page
                val totalPages = body.totalPages
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page == totalPages) null else page + 1
                )
            } else {
                LoadResult.Error(Exception(response.code().toString()))
            }

        } catch (e: IOException) {
            LoadResult.Error(Exception(Constants.CONNECTION_ERROR_KEY))
        } catch (e: Exception) {
            LoadResult.Error(Exception(e.message!!))
        }
    }
}