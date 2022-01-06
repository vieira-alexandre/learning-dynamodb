package dev.alexandrevieira.learningdynamodb.gateway.service

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import dev.alexandrevieira.learningdynamodb.domain.entities.Movie
import dev.alexandrevieira.learningdynamodb.domain.projections.MovieProjection
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import java.util.*

@Service
class MovieServiceImpl(
    private val db: DynamoDBMapper
) : MovieService {

    override fun save(movie: Movie): Movie {
        db.save(movie)
        return movie
    }

    override fun find(year: Int, title: String): Optional<Movie> {
        return Optional.ofNullable(db.load(Movie::class.java, year, title))
    }

    override fun delete(year: Int, title: String) {
        val loaded = db.load(Movie::class.java, year, title)
        db.delete(loaded)
    }


    override fun findAllByYear(year: Int): List<MovieProjection> {
        val query = createQuery(year)
        val resultList: List<Movie> = db.query(Movie::class.java, query)
        return resultList.map { MovieProjection(it.year, it.title, it.rating.toBigDecimal()) }
    }

    override fun listRatingsAbove(year: Int, rating: Double): List<MovieProjection> {
        val query = createGSIQuery(year, rating)
        val resultList: List<Movie> = db.query(Movie::class.java, query)
        return resultList.map { MovieProjection(it.year, it.title, it.rating.toBigDecimal()) }
    }

    override fun listRatingsAbove(
        year: Int,
        rating: Double,
        pageSize: Int,
        exclusiveStartKey: Map<String, AttributeValue>
    ): QueryResultPage<MovieProjection> {
        val limit: Int = if (pageSize <= 0)
            5
        else if (pageSize > 50)
            50
        else
            pageSize

        val query = createGSIQuery(year, rating).withLimit(limit)

        if (exclusiveStartKey.isNotEmpty()) {
            query.withExclusiveStartKey(exclusiveStartKey)
        }

        val resultPage = db.queryPage(Movie::class.java, query)

        return QueryResultPage<MovieProjection>().apply {
            BeanUtils.copyProperties(resultPage, this, "results")
            this.results = resultPage.results.map { MovieProjection(it.year, it.title, it.rating.toBigDecimal()) }
        }
    }

    private fun createQuery(year: Int): DynamoDBQueryExpression<Movie> {
        //year is a reserved word in DynamoDB, so I needed a name map

        val nameMap = mapOf("#yr" to "year")
        val valueMap = mapOf(":yyyy" to AttributeValue().withN(year.toString()))

        return DynamoDBQueryExpression<Movie>()
//            .withScanIndexForward(true) //true (default) = ascending, false = descending
//            .withConsistentRead(true) //gsi only supports false (non-consistent read)
            .withProjectionExpression("#yr, title, rating")
            .withKeyConditionExpression("#yr = :yyyy")
            .withExpressionAttributeNames(nameMap)
            .withExpressionAttributeValues(valueMap)
    }

    private fun createGSIQuery(year: Int, rating: Double): DynamoDBQueryExpression<Movie> {
        //year is a reserved word in DynamoDB, so I needed a name map

        val nameMap = mapOf("#yr" to "year")
        val valueMap = mapOf(
            ":yyyy" to AttributeValue().withN(year.toString()),
            ":rating" to AttributeValue().withN(rating.toString())
        )

        return DynamoDBQueryExpression<Movie>()
            .withIndexName("RatingIndex")
            .withScanIndexForward(false) //true (default) = ascending, false = descending
            .withConsistentRead(false) //gsi only supports false (non-consistent read)
            .withProjectionExpression("#yr, rating, title")
            .withKeyConditionExpression("#yr = :yyyy and rating >= :rating")
            .withExpressionAttributeNames(nameMap)
            .withExpressionAttributeValues(valueMap)
    }
}