package dev.alexandrevieira.learningdynamodb.domain.dto

import dev.alexandrevieira.learningdynamodb.domain.entities.Movie
import dev.alexandrevieira.learningdynamodb.domain.entities.MovieInfo
import java.net.URI
import java.time.Instant

data class MovieResponse(
    val year: Int,
    val title: String,
    val info: MovieInfoResponse,
    val rating: Double
) {
    companion object {
        fun of(movie: Movie): MovieResponse {
            return MovieResponse(movie.year, movie.title, MovieInfoResponse.of(movie.info), movie.rating)
        }
    }
}

data class MovieInfoResponse(
    val directors: List<String> = emptyList(),
    val releaseDate: Instant?,
    val rating: Double,
    val genres: List<String> = emptyList(),
    val imageUrl: URI?,
    val plot: String,
    val rank: Int?,
    val runningTimeSecs: Int?,
    val actors: List<String> = emptyList()
) {
    companion object {
        fun of(movieInfo: MovieInfo): MovieInfoResponse {
            return MovieInfoResponse(
                movieInfo.directors,
                movieInfo.releaseDate,
                movieInfo.rating,
                movieInfo.genres,
                movieInfo.imageUrl,
                movieInfo.plot,
                movieInfo.rank,
                movieInfo.runningTimeSecs,
                movieInfo.actors
            )
        }
    }
}