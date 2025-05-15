package com.example.moviedatabase.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedatabase.domain.model.Movie
import com.example.moviedatabase.domain.usecase.GetMoviesUseCase
import com.example.moviedatabase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesBrowserViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
): ViewModel() {

    private val _state = MutableStateFlow(MoviesBrowserState())
    val state: StateFlow<MoviesBrowserState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getMoviesUseCase().collect { resource ->
                _state.value = when (resource) {
                    is Resource.Success -> _state.value.copy(
                            movies = resource.data ?: emptyList(),
                            isLoading = false
                        )
                    is Resource.Error -> _state.value.copy(
                        isLoading = false,
                        error = resource.message ?: "An unexpected error."
                    )
                    is Resource.Loading -> _state.value.copy(isLoading = true)
                }
            }
        }
    }
}

data class MoviesBrowserState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)