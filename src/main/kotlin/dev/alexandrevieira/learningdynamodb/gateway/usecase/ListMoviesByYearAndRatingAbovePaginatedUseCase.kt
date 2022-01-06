package dev.alexandrevieira.learningdynamodb.gateway.usecase

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import dev.alexandrevieira.learningdynamodb.domain.projections.MovieProjection
import dev.alexandrevieira.learningdynamodb.gateway.service.MovieService
import org.springframework.stereotype.Component

@Component
class ListMoviesByYearAndRatingAbovePaginatedUseCase(
    private val service: MovieService
) {
    fun execute(
        year: Int,
        rating: Double,
        pageSize: Int,
        exclusiveStartKey: Map<String, AttributeValue>
    ): QueryResultPage<MovieProjection> {
        return service.listRatingsAbove(year, rating, pageSize, exclusiveStartKey)
    }
}