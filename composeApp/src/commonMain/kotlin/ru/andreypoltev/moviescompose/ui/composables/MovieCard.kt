package ru.andreypoltev.moviescompose.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import moviescompose.composeapp.generated.resources.movie_placeholder
import org.jetbrains.compose.resources.painterResource
import ru.andreypoltev.moviescompose.model.Film

@Composable
fun MovieCard(movie: Film, onFilmClicked: (Film) -> Unit) {
    Card(
        shape = RoundedCornerShape(4.dp),
        onClick = { onFilmClicked(movie) },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp))) {

            Box(
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp))
            ) {

                val painterResource = asyncPainterResource(data = Url(movie.imageUrl.toString()))

                KamelImage(modifier = Modifier.fillMaxSize(),
                    resource = painterResource,
                    contentDescription = "Profile",
                    contentScale = ContentScale.FillWidth,
                    onLoading = { progress ->

                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(Res.drawable.movie_placeholder),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth
                            )

                            CircularProgressIndicator()
                        }


                    },
                    onFailure = { exception ->


                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(Res.drawable.movie_placeholder),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth
                        )

//                                    coroutineScope.launch {
//                                        snackbarHostState.showSnackbar(
//                                            message = exception.message.toString(),
//                                            actionLabel = "Hide",
//                                            duration = SnackbarDuration.Short
//                                        )
//                                    }
                    })


            }

            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = movie.localizedName.toString(),
                maxLines = 2,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }

    }
}