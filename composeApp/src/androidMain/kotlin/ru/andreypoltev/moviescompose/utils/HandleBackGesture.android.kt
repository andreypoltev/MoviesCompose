package ru.andreypoltev.moviescompose.utils

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import ru.andreypoltev.moviescompose.model.Film

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
actual fun HandleBackGesture(navigator: ThreePaneScaffoldNavigator<Film>) {

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }
}