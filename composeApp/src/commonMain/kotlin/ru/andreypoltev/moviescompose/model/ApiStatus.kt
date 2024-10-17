package ru.andreypoltev.moviescompose.model

sealed class ApiStatus {
    object Loading : ApiStatus()
    data class Success(val movies: List<Film>) : ApiStatus()
    data class Error(val message: String) : ApiStatus()
    object Idle : ApiStatus()
}