package dev.alexandrevieira.learningdynamodb.utils

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import java.time.Instant

class InstantConverter : DynamoDBTypeConverter<String, Instant> {
    override fun convert(instant: Instant): String {
        return instant.toString()
    }

    override fun unconvert(string: String): Instant {
        return Instant.parse(string)
    }
}