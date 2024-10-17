package ru.andreypoltev.moviescompose

import ru.andreypoltev.moviescompose.model.Film

sealed class ApiStatus {
    object Loading : ApiStatus()
    data class Success(val movies: List<Film>) : ApiStatus()
    data class Error(val message: String) : ApiStatus()
    object Idle : ApiStatus()
}