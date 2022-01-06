package dev.alexandrevieira.learningdynamodb.gateway.usecase

import dev.alexandrevieira.learningdynamodb.domain.entities.Movie
import dev.alexandrevieira.learningdynamodb.exception.DuplicateKeyException
import dev.alexandrevieira.learningdynamodb.gateway.service.MovieService
import org.springframework.stereotype.Component
import java.util.*

@Component
class CreateMovieUseCase(
    private val service: MovieService
) {
    fun execute(movie: Movie): Movie {
        val optional: Optional<Movie> = service.find(movie.year, movie.title)
        if (optional.isPresent) throw DuplicateKeyException(Movie::class)
        return service.save(movie)
    }
}