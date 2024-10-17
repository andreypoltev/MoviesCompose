package ru.andreypoltev.moviescompose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import moviescompose.composeapp.generated.resources.Res
import moviescompose.composeapp.generated.resources.genres
import moviescompose.composeapp.generated.resources.movie_placeholder
import moviescompose.composeapp.generated.resources.movies
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import ru.andreypoltev.moviescompose.model.Film
import ru.andreypoltev.moviescompose.ui.theme.MoviesComposeTheme

@Composable
@Preview
fun App() {

    MoviesComposeTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            AppContent()
        }
    }

}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppContent(

    viewModel: MainViewModel = viewModel {
        MainViewModel()
    }

) {

    val apiStatus by viewModel.apiStatus.collectAsState()

    when (apiStatus) {
        is ApiStatus.Error -> {

            val message = (apiStatus as ApiStatus.Error).message
            Text(
                text = message, color = Color.Red, modifier = Modifier.padding(16.dp)
            )

        }

        ApiStatus.Idle -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        }

        ApiStatus.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ApiStatus.Success -> {

            val navigator = rememberListDetailPaneScaffoldNavigator<Film>()

            ListDetailPaneScaffold(modifier = Modifier.statusBarsPadding(),
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {

                    AnimatedPane {
                        ListPane(viewModel = viewModel) { film ->
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, film)
                        }
                    }


                },
                detailPane = {

                    AnimatedPane {
                        navigator.currentDestination?.content?.let { movie ->
                            DetailsPane(movie) {
                                if (navigator.canNavigateBack()) {
                                    navigator.navigateBack()
                                }
                            }
                        }
                    }
                })
        }
    }


}



