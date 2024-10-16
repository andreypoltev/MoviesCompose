package ru.andreypoltev.moviescompose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import moviescompose.composeapp.generated.resources.Res
import moviescompose.composeapp.generated.resources.movies
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import ru.andreypoltev.moviescompose.model.Film
import ru.andreypoltev.moviescompose.ui.theme.MoviesComposeTheme

@Composable
@Preview
fun App() {

    MoviesComposeTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AppContent()
        }
    }

}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppContent() {

    val navigator = rememberListDetailPaneScaffoldNavigator<Film>()

    ListDetailPaneScaffold(directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            ListPane() { film ->
                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, film)
            }
        },
        detailPane = {
            DetailsPane() {
                if (navigator.canNavigateBack()) {
                    navigator.navigateBack()
                }
            }
        })


}

@Composable
fun DetailsPane(onBackClicked: () -> Unit) {
//    TODO("Not yet implemented")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPane(onFilmClicked: (Film) -> Unit) {

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(stringResource(Res.string.movies)) })
    }) {

    }
//    TODO("Not yet implemented")
}
