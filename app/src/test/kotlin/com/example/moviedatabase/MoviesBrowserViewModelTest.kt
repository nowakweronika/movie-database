package com.example.moviedatabase

import com.example.moviedatabase.domain.model.Movie
import com.example.moviedatabase.domain.usecase.GetMoviesUseCase
import com.example.moviedatabase.ui.screens.MoviesBrowserViewModel
import com.example.moviedatabase.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesBrowserViewModelTest {

    private val getMoviesUseCase = mockk<GetMoviesUseCase>()
    private lateinit var viewModel: MoviesBrowserViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `given movie, when get movies, then set movies`() = runTest {
        // Given
        val movies = listOf(
            Movie(
                id = 1,
                title = "Test Movie",
                genres = listOf("Action", "Adventure"),
                image = "image_url"
            )
        )
        val flow = flowOf(
            Resource.Loading(),
            Resource.Success(movies)
        )
        coEvery { getMoviesUseCase() } returns flow

        // When
        viewModel = MoviesBrowserViewModel(getMoviesUseCase)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(movies, state.movies)
        assertNull(state.error)
    }

    @Test
    fun `given error, when get movies, then set error`() = runTest {
        // Given
        val flow: Flow<Resource<List<Movie>>> = flowOf(
            Resource.Loading(),
            Resource.Error("Network error")
        )
        coEvery { getMoviesUseCase() } returns flow

        // When
        viewModel = MoviesBrowserViewModel(getMoviesUseCase)
        advanceUntilIdle()

        // Then
        viewModel.state.value.apply {
            assertFalse(isLoading)
            assertEquals("Network error", error)
            assertTrue(movies.isEmpty())
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}