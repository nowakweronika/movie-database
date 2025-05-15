package com.example.moviedatabase.data

import com.example.moviedatabase.data.model.MoviesDto
import retrofit2.http.GET

interface MovieDatabaseApi {

    @GET("/3/discover/movie")
    suspend fun getDiscoverMovies(): MoviesDto
}