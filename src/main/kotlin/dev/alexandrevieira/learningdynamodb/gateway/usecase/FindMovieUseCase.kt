package dev.alexandrevieira.learningdynamodb.gateway.usecase

import dev.alexandrevieira.learningdynamodb.domain.entities.Movie
import dev.alexandrevieira.learningdynamodb.exception.NotFoundException
import dev.alexandrevieira.learningdynamodb.gateway.service.MovieService
import org.springframework.stereotype.Component

@Component
class FindMovieUseCase(
    private val service: MovieService
) {
    fun execute(year: Int, title: String): Movie {
        val optional = service.find(year, title)
        if (optional.isEmpty) throw NotFoundException()
        return optional.get()
    }
}