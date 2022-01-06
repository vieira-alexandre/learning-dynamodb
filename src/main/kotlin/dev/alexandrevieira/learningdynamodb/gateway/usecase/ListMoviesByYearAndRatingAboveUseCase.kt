package dev.alexandrevieira.learningdynamodb.gateway.usecase

import dev.alexandrevieira.learningdynamodb.domain.projections.MovieProjection
import dev.alexandrevieira.learningdynamodb.gateway.service.MovieService
import org.springframework.stereotype.Component

@Component
class ListMoviesByYearAndRatingAboveUseCase(
    private val service: MovieService
) {
    fun execute(year: Int, rating: Double): List<MovieProjection> {
        return service.listRatingsAbove(year, rating)
    }
}