package ru.andreypoltev.moviescompose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import coil3.compose.AsyncImage
import moviescompose.composeapp.generated.resources.Res
import moviescompose.composeapp.generated.resources.arrow_left_25
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

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = movie.imageUrl,
                    null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.width(132.dp).clip(RoundedCornerShape(4.dp)),
                )
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