package com.example.moviedatabase.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val genres: List<String>,
    val image: String,
)
