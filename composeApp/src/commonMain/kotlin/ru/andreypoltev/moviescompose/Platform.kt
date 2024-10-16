package ru.andreypoltev.moviescompose

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform