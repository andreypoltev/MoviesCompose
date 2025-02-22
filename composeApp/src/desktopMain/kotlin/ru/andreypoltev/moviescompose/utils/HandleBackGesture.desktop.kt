package ru.andreypoltev.moviescompose.utils

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import ru.andreypoltev.moviescompose.model.Film

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
actual fun HandleBackGesture(navigator: ThreePaneScaffoldNavigator<Film>) {
}