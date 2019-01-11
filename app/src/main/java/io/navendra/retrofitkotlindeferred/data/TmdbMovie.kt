package io.navendra.retrofitkotlindeferred.data

data class TmdbMovie(
    val id: Int,
    val vote_average: Double,
    val title: String,
    val overview: String,
    val adult: Boolean
)

data class TmdbMovieResponse(
    val results: List<TmdbMovie>
)