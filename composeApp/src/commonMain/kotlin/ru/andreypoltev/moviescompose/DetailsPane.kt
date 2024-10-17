package ru.andreypoltev.moviescompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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

            Box(modifier = Modifier.heightIn(max = 200.dp), contentAlignment = Alignment.Center) {

                Box(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
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

            Text(text = "${movie.genres?.joinToString()}, ${movie.year}")
            Spacer(modifier = Modifier.size(8.dp))

            Text(text = movie.rating.toString())

            Spacer(modifier = Modifier.size(16.dp))

            Text(text = movie.description.toString(), modifier = Modifier.fillMaxWidth())


        }


    }

}