package com.example.moviedatabase.data.model

import com.google.gson.annotations.SerializedName

data class MoviesDto(
    val results: List<MovieDto>
)

data class MovieDto(
    val id: Int,
    val title: String,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
)