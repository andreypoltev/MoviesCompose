package ru.andreypoltev.moviescompose.ui.composables

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import moviescompose.composeapp.generated.resources.Res
import moviescompose.composeapp.generated.resources.movies
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.movies),
            )
        },
//            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(14, 49, 101))
    )
}