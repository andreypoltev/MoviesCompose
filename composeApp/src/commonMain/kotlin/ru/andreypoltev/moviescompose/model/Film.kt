package ru.andreypoltev.moviescompose.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Film(
    @SerialName("description")
    val description: String? = "",
    @SerialName("genres")
    val genres: List<String?>? = listOf(),
    @SerialName("id")
    val id: Int? = 0,
    @SerialName("image_url")
    val imageUrl: String? = "",
    @SerialName("localized_name")
    val localizedName: String? = "",
    @SerialName("name")
    val name: String? = "",
    @SerialName("rating")
    val rating: Double? = 0.0,
    @SerialName("year")
    val year: Int? = 0
)