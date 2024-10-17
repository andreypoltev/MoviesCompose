package ru.andreypoltev.moviescompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.andreypoltev.moviescompose.model.Film
import ru.andreypoltev.moviescompose.model.Movies

class MainViewModel : ViewModel() {

    private val _apiStatus = MutableStateFlow<ApiStatus>(ApiStatus.Idle)
    val apiStatus = _apiStatus.asStateFlow()

    private val _listOfMovies = MutableStateFlow(listOf<Film>())
    val listOfMovies = _listOfMovies.asStateFlow()

    private val _filteredList = MutableStateFlow(listOf<Film>())
    val filteredList = _filteredList.asStateFlow()

    private val _genres = MutableStateFlow(listOf<String>())
    val genres = _genres.asStateFlow()

    init {
        fetchMovies()
    }


    private fun fetchMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _apiStatus.value = ApiStatus.Loading
            val apiResponse = getApiResponse()

            if (apiResponse.isNotEmpty()) {
                _listOfMovies.value = apiResponse
                _filteredList.value = apiResponse

                val genres = apiResponse.mapNotNull { film ->
                    film.genres
                }.flatten().filterNotNull().distinct().sorted()

                _genres.value = genres

                _apiStatus.value = ApiStatus.Success(apiResponse)
            } else {
                _apiStatus.value = ApiStatus.Error("Failed to load movies.")
            }
        }
    }

    fun filterMovies(genre: String) {
        viewModelScope.launch(Dispatchers.IO) {


            if (genre.isNotEmpty()) {
                val filtered = _listOfMovies.value.filter {
                    it.genres?.contains(genre) == true
                }

                _filteredList.value = filtered
            } else {
                _filteredList.value = _listOfMovies.value
            }


        }
    }

    expect fun getApiResponse()

    private suspend fun getApiResponse(): List<Film> {

        println("getApiResponse()")

        try {
            val client = HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
            }


            val resp: Movies = client.get(Constants.API_LINK).body()

            println("Response is: $resp")

            client.close()

            return resp.films ?: listOf(Film())

        } catch (e: Exception) {
            println(e.message)
            return listOf()
        }
    }

}
