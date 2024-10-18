package ru.andreypoltev.moviescompose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import ru.andreypoltev.moviescompose.ui.composables.CustomTopBar
import ru.andreypoltev.moviescompose.ui.theme.MoviesComposeTheme
import ru.andreypoltev.moviescompose.ui.theme.Yellow
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
            val snackbarHostState = remember { SnackbarHostState() }
            val message = (apiStatus as ApiStatus.Error).message

            ErrorScreen(message = message, snackbarHostState = snackbarHostState) {
                viewModel.retry()
            }

        }

        ApiStatus.Idle, ApiStatus.Loading -> {

            LoadingIndicator()
        }

        is ApiStatus.Success -> {

            MainScreen(viewModel)
        }
    }


}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Film>()
    val lazyGridScrollState = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState()
    }

    HandleBackGesture(navigator)

    ListDetailPaneScaffold(modifier = Modifier.statusBarsPadding(),
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                ListPane(viewModel = viewModel, lazyGridScrollState = lazyGridScrollState) { film ->
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

@Composable
fun ErrorScreen(
    message: String, snackbarHostState: SnackbarHostState, onRetry: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val res = stringResource(Res.string.retry)

    Scaffold(topBar = { CustomTopBar() }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) { snackbarData ->
            Snackbar(actionColor = Yellow, snackbarData = snackbarData)
        }
    }) {
        LaunchedEffect(message) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(message = message, actionLabel = res)
                if (result == SnackbarResult.ActionPerformed) {
                    onRetry()
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
    }
}





