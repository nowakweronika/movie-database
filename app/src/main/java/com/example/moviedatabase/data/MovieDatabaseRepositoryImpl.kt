package com.example.moviedatabase.data

import com.example.moviedatabase.data.model.MoviesDto
import com.example.moviedatabase.domain.MovieDatabaseRepository
import javax.inject.Inject

class MovieDatabaseRepositoryImpl @Inject constructor(
    private val movieDatabaseApi: MovieDatabaseApi
) : MovieDatabaseRepository {

    override suspend fun getDiscoverMovies(): MoviesDto = movieDatabaseApi.getDiscoverMovies()
}