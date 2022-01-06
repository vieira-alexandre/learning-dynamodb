package dev.alexandrevieira.learningdynamodb.gateway.service

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import dev.alexandrevieira.learningdynamodb.domain.entities.Movie
import dev.alexandrevieira.learningdynamodb.domain.projections.MovieProjection
import java.util.*

interface MovieService {
    fun save(movie: Movie): Movie
    fun find(year: Int, title: String): Optional<Movie>
    fun delete(year: Int, title: String)
    fun findAllByYear(year: Int): List<MovieProjection>
    fun listRatingsAbove(year: Int, rating: Double): List<MovieProjection>

    fun listRatingsAbove(
        year: Int,
        rating: Double,
        pageSize: Int,
        exclusiveStartKey: Map<String, AttributeValue>
    ): QueryResultPage<MovieProjection>
}