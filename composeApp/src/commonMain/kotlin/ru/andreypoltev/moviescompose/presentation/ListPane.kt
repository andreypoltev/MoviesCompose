package ru.andreypoltev.moviescompose.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import moviescompose.composeapp.generated.resources.Res
import moviescompose.composeapp.generated.resources.genres
import moviescompose.composeapp.generated.resources.movie_placeholder
import moviescompose.composeapp.generated.resources.movies
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.andreypoltev.moviescompose.MainViewModel
import ru.andreypoltev.moviescompose.model.Film
import ru.andreypoltev.moviescompose.ui.composables.CustomTopBar
import ru.andreypoltev.moviescompose.ui.composables.MovieCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ListPane(
    viewModel: MainViewModel,
    lazyGridScrollState: LazyGridState,
    onFilmClicked: (Film) -> Unit,
) {

    val movies by viewModel.filteredList.collectAsState()

    val genres by viewModel.genres.collectAsState()

    Scaffold(topBar = {

        CustomTopBar()


    }) { paddingValues ->

        LazyVerticalGrid(
            state = lazyGridScrollState,
            modifier = Modifier.padding(paddingValues),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            item(span = {
                GridItemSpan(maxLineSpan)
            }) {


                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(Res.string.genres), fontSize = 22.sp,
//                            modifier = Modifier.padding(vertical = 8.dp),
                            fontWeight = FontWeight.SemiBold
                        )

                    }

                    Spacer(Modifier.size(8.dp))


                    var selectedGenre by rememberSaveable { mutableStateOf<String?>(null) }

                    genres.forEach { genre ->
                        val isSelected = selectedGenre == genre
                        Box(modifier = Modifier.fillMaxWidth().background(
                            if (isSelected) Color(
                                255, 201, 103
                            ) else Color.Transparent
                        ).clickable {
                            selectedGenre = if (isSelected) null else genre
                            viewModel.filterMovies(selectedGenre ?: "")
                        }) {
                            Text(
                                text = genre.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = Color.Black
                            )
                        }
                    }
                }


            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {

                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(Res.string.movies), fontSize = 22.sp,
//                            modifier = Modifier.padding(horizontal = 16.dp),
                            fontWeight = FontWeight.SemiBold
                        )

                    }
                }

            }




            items(movies) { movie ->
                MovieCard(movie, onFilmClicked)
            }
        }


    }
}




