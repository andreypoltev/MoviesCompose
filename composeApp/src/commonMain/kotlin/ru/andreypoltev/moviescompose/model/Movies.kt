package ru.andreypoltev.moviescompose.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movies(
    @SerialName("films")
    val films: List<Film?>? = listOf()
)