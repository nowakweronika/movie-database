package com.example.moviedatabase.domain.usecase

import com.example.moviedatabase.data.mapper.toMovie
import com.example.moviedatabase.domain.MovieDatabaseRepository
import com.example.moviedatabase.domain.model.Movie
import com.example.moviedatabase.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val movieDatabaseRepository: MovieDatabaseRepository
) {
    operator fun invoke(): Flow<Resource<List<Movie>>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(movieDatabaseRepository.getDiscoverMovies().results.map { it.toMovie() }))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}