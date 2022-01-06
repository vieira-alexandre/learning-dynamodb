package dev.alexandrevieira.learningdynamodb.domain.entities

import com.amazonaws.services.dynamodbv2.datamodeling.*
import dev.alexandrevieira.learningdynamodb.utils.Constants
import dev.alexandrevieira.learningdynamodb.utils.InstantConverter
import org.apache.logging.log4j.util.Strings
import java.net.URI
import java.time.Instant

@DynamoDBTable(tableName = Constants.TABLE_NAME)
data class Movie (
    @DynamoDBHashKey(attributeName = "year")
    @DynamoDBIndexHashKey(attributeName = "year", globalSecondaryIndexName = "RatingIndex")
    var year: Int,

    @DynamoDBRangeKey(attributeName = "title")
    var title: String,

    @DynamoDBAttribute(attributeName = "info")
    var info: MovieInfo,

    @DynamoDBIndexRangeKey(attributeName = "rating", globalSecondaryIndexName = "RatingIndex")
    var rating: Double = info.rating
)

@DynamoDBDocument
data class MovieInfo (
    var actors: List<String> = listOf(),
    var directors: List<String> = listOf(),
    var rating: Double,
    var plot: String = Strings.EMPTY,
    var genres: List<String> = listOf(),
    @DynamoDBTypeConverted(converter = InstantConverter::class)
    var releaseDate: Instant? = null,
    var imageUrl: URI? = null,
    var rank: Int? = null,
    var runningTimeSecs: Int? = null
)
