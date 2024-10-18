package ru.andreypoltev.moviescompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.rootCause
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.andreypoltev.moviescompose.model.ApiStatus
import ru.andreypoltev.moviescompose.model.Film
import ru.andreypoltev.moviescompose.model.Movies
import ru.andreypoltev.moviescompose.utils.Constants

class MainViewModel : ViewModel() {

    private val _apiStatus = MutableStateFlow<ApiStatus>(ApiStatus.Idle)
    val apiStatus = _apiStatus.asStateFlow()

    private val _listOfMovies = MutableStateFlow(emptyList<Film>())
    val listOfMovies = _listOfMovies.asStateFlow()

    private val _filteredList = MutableStateFlow(emptyList<Film>())
    val filteredList = _filteredList.asStateFlow()

    private val _genres = MutableStateFlow(emptyList<String>())
    val genres = _genres.asStateFlow()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    init {
        fetchMovies()
    }

    fun retry() {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _apiStatus.value = ApiStatus.Loading
            when (val apiResponse = fetchMoviesFromApi()) {
                is Result.Success -> {
                    _listOfMovies.value = apiResponse.data
                    _filteredList.value = apiResponse.data

                    val genres = apiResponse.data.mapNotNull { film ->
                        film.genres
                    }.flatten().filterNotNull().distinct().sortedBy { it }

                    _genres.value = genres
                    _apiStatus.value = ApiStatus.Success(apiResponse.data)
                }

                is Result.Failure -> {
                    _apiStatus.value =
//                        ApiStatus.Error("Failed to load movies: ${apiResponse.exception}")
                        ApiStatus.Error("Failed to load movies.")
                }
            }
        }
    }

    fun filterMovies(genre: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _filteredList.value = if (genre.isNotEmpty()) {
                _listOfMovies.value.filter { it.genres?.contains(genre) == true }
            } else {
                _listOfMovies.value
            }
        }
    }

    private suspend fun fetchMoviesFromApi(): Result<List<Film>> {
        return try {
            val response: Movies = httpClient.get(Constants.API_LINK).body()
            Result.Success(response.films ?: emptyList())
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override fun onCleared() {
        super.onCleared()
        httpClient.close() // Clean up HttpClient
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()
}
