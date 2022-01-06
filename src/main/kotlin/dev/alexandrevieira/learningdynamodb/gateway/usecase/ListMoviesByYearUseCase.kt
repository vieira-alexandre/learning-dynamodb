package dev.alexandrevieira.learningdynamodb.gateway.usecase

import dev.alexandrevieira.learningdynamodb.domain.projections.MovieProjection
import dev.alexandrevieira.learningdynamodb.gateway.service.MovieService
import org.springframework.stereotype.Component

@Component
class ListMoviesByYearUseCase(
    private val service: MovieService
) {
    fun execute(year: Int): List<MovieProjection> {
        return service.findAllByYear(year)
    }
}