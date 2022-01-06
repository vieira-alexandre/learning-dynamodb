package dev.alexandrevieira.learningdynamodb.gateway.usecase

import dev.alexandrevieira.learningdynamodb.domain.entities.Movie
import dev.alexandrevieira.learningdynamodb.exception.NotFoundException
import dev.alexandrevieira.learningdynamodb.gateway.service.MovieService
import org.springframework.stereotype.Component
import java.util.*

@Component
class UpdateMovieUseCase(
    private val service: MovieService
) {
    fun execute(movie: Movie): Movie {
        val optional: Optional<Movie> = service.find(movie.year, movie.title)
        if (optional.isEmpty) throw NotFoundException()
        return service.save(movie)
    }
}