package dev.alexandrevieira.learningdynamodb.domain.dto

import dev.alexandrevieira.learningdynamodb.domain.entities.Movie
import dev.alexandrevieira.learningdynamodb.domain.entities.MovieInfo
import org.jetbrains.annotations.NotNull
import java.net.URI
import java.time.Instant
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Past
import javax.validation.constraints.Positive

data class MovieRequest(
    @field:NotNull
    @field:Positive
    val year: Int,

    @field:NotBlank
    val title: String,

    @field:NotNull
    @field:Valid
    val info: MovieInfoRequest,

    @field:NotNull
    @field:Positive
    val rating: Double
) {
    fun toModel(): Movie {
        return Movie(year, title, info.toModel(), rating)
    }
}

data class MovieInfoRequest(
    @field:NotNull
    @field:NotEmpty
    val directors: List<String>,

    @field:Past
    val releaseDate: Instant?,

    @field:NotNull
    val rating: Double,

    @field:NotNull
    @field:NotEmpty
    val genres: List<String>,

    val imageUrl: URI?,

    @field:NotBlank
    val plot: String,

    @field:Positive
    val rank: Int?,

    @field:Positive
    val runningTimeSecs: Int?,

    @field:NotNull
    @field:NotEmpty
    val actors: List<String>
){
    fun toModel(): MovieInfo {
        return MovieInfo(actors,directors,rating,plot,genres,releaseDate,imageUrl,rank,runningTimeSecs)
    }
}