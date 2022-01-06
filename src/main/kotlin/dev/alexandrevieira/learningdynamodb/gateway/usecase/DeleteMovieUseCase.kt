package dev.alexandrevieira.learningdynamodb.gateway.usecase

import dev.alexandrevieira.learningdynamodb.exception.NotFoundException
import dev.alexandrevieira.learningdynamodb.gateway.service.MovieService
import org.springframework.stereotype.Component

@Component
class DeleteMovieUseCase(
    private val service: MovieService
) {
    fun execute(year: Int, title: String) {
        val optional = service.find(year, title)
        if (optional.isEmpty) throw NotFoundException()
        service.delete(year, title)
    }
}