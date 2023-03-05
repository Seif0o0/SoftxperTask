package softxpert.movie.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import softxpert.movie.app.data.repository.MovieDetailsRepositoryImpl
import softxpert.movie.app.data.repository.MoviesRepositoryImpl
import softxpert.movie.app.domain.repository.MovieDetailsRepository
import softxpert.movie.app.domain.repository.MoviesRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun provideMoviesRepo(repoImpl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    abstract fun provideMovieDetailsRepo(repoImpl: MovieDetailsRepositoryImpl): MovieDetailsRepository
}