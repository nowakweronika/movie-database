package com.example.moviedatabase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Card
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.moviedatabase.R
import com.example.moviedatabase.domain.model.Movie

@Composable
fun MoviesBrowserScreen(viewModel: MoviesBrowserViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        LoadingScreen(modifier = Modifier.fillMaxSize())
    } else if (state.error != null) {
        ErrorScreen(modifier = Modifier.fillMaxSize())
    } else {
        MoviesList(movies = state.movies, modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(text = "Loading...", style = MaterialTheme.typography.displayMedium)
    }
}

@Composable
private fun ErrorScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(text = "Something went wrong...", style = MaterialTheme.typography.displayMedium)
    }
}

@Composable
private fun MoviesList(movies: List<Movie>, modifier: Modifier = Modifier) {
    val categories = listOf(
        Category("List photo M", CategorySize.Medium),
        Category("List photo S", CategorySize.Small),
        Category("List photo L", CategorySize.Large),
        Category("List photo XL", CategorySize.ExtraLarge)
    )
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 48.dp, vertical = 32.dp)
    ) {
        items(categories) { category ->
            Text(text = category.title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(movies) { movie ->
                    MovieCard(movie = movie, categorySize = category.size)
                }
            }
        }
    }
}

@Composable
private fun MovieCard(
    movie: Movie,
    categorySize: CategorySize,
    modifier: Modifier = Modifier,
) {
    val maxWidth = when (categorySize) {
        CategorySize.Small -> 124.dp
        CategorySize.Medium -> 196.dp
        CategorySize.Large -> 268.dp
        CategorySize.ExtraLarge -> 412.dp
    }

    Column {
        Box {
            Card(
                onClick = { },
                modifier = modifier
                    .widthIn(max = maxWidth)
                    .aspectRatio(16f / 9f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = movie.image,
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.placeholder),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }

        Column(
            modifier = Modifier
                .width(maxWidth)
                .offset(y = (-12).dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = movie.genres.joinToString(" • "),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
