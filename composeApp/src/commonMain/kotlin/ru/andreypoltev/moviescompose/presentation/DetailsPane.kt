package ru.andreypoltev.moviescompose.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import moviescompose.composeapp.generated.resources.Res
import moviescompose.composeapp.generated.resources.arrow_left_25
import moviescompose.composeapp.generated.resources.movie_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import ru.andreypoltev.moviescompose.model.Film
import ru.andreypoltev.moviescompose.ui.theme.Blue
import ru.andreypoltev.moviescompose.ui.theme.Grey
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsPane(movie: Film, onBackClicked: () -> Unit) {

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = movie.name.toString()) }, navigationIcon = {
            IconButton(
                onClick = onBackClicked,
                content = {
                    Icon(vectorResource(Res.drawable.arrow_left_25), null)
                },

                )
        })
    }) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

                Box(
                    modifier = Modifier.heightIn(max = 200.dp).widthIn(max = 132.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {

                    val painterResource =
                        asyncPainterResource(data = Url(movie.imageUrl.toString()))

                    KamelImage(
                        resource = painterResource,
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        onLoading = { progress ->

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
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
            }

            Spacer(modifier = Modifier.size(24.dp))

            Text(
                text = movie.localizedName.toString(),
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = "${movie.genres?.joinToString()}, ${movie.year} год",
                fontSize = 16.sp,
                color = Grey
            )
            Spacer(modifier = Modifier.size(8.dp))


            val v = if (movie.rating != null) {
                round(movie.rating * 10) / 10
            } else {
                0.0
            }



            Row(verticalAlignment = Alignment.Bottom) {

                Text(
                    text = v.toString(),
                    fontSize = 24.sp,
                    color = Blue,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "КиноПоиск",
                    fontSize = 16.sp,
                    color = Blue,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            Text(text = movie.description.toString(), modifier = Modifier.fillMaxWidth())


        }


    }

}