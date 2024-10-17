package ru.andreypoltev.moviescompose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import moviescompose.composeapp.generated.resources.Res
import moviescompose.composeapp.generated.resources.retry
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.andreypoltev.moviescompose.model.ApiStatus

import ru.andreypoltev.moviescompose.model.Film
import ru.andreypoltev.moviescompose.presentation.DetailsPane
import ru.andreypoltev.moviescompose.presentation.ListPane
import ru.andreypoltev.moviescompose.ui.composables.LoadingIndicator
import ru.andreypoltev.moviescompose.ui.theme.MoviesComposeTheme
import ru.andreypoltev.moviescompose.utils.HandleBackGesture

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
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            val message = (apiStatus as ApiStatus.Error).message

            Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {

                val res = stringResource(
                    Res.string.retry
                )

                LaunchedEffect(message) {
                    scope.launch {

                        val result = snackbarHostState.showSnackbar(
                            message = message, actionLabel = res
                        )


                        when (result) {

                            SnackbarResult.Dismissed ->  {

                            }
                            SnackbarResult.ActionPerformed ->  {
                                viewModel.retry()
                            }
                        }

                    }
                }
            }
        }

        ApiStatus.Idle, ApiStatus.Loading -> {
            LoadingIndicator()
        }

        is ApiStatus.Success -> {

            val navigator = rememberListDetailPaneScaffoldNavigator<Film>()

            val lazyGridScrollState = rememberLazyGridState()

            HandleBackGesture(navigator)

            ListDetailPaneScaffold(modifier = Modifier.statusBarsPadding(),
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {

                    AnimatedPane {
                        ListPane(
                            viewModel = viewModel, lazyGridScrollState = lazyGridScrollState
                        ) { film ->
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





