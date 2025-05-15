package com.example.moviedatabase.domain

import com.example.moviedatabase.data.model.MoviesDto

interface MovieDatabaseRepository {

    suspend fun getDiscoverMovies(): MoviesDto
}