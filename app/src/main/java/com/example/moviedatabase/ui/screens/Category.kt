package com.example.moviedatabase.ui.screens

data class Category(
    val title: String,
    val size: CategorySize
)

enum class CategorySize {
    Small,
    Medium,
    Large,
    ExtraLarge
}