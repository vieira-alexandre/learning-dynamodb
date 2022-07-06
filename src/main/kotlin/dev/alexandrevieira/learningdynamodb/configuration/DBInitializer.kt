package dev.alexandrevieira.learningdynamodb.configuration

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.Table
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import dev.alexandrevieira.learningdynamodb.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import java.io.File

@Configuration
class DBInitializer(
    private val dynamoDB: DynamoDB
) {
    private val log = LoggerFactory.getLogger(DBInitializer::class.java)
    private val tableName = Constants.TABLE_NAME

    @Value("\${database.initialize.amount}")
    private val amount: Int = 0

    @EventListener(ApplicationReadyEvent::class)
    fun doSomethingAfterStartup() {
        val table: Table = dynamoDB.getTable(tableName)
        val parser: JsonParser = JsonFactory().createParser(File("localstack/moviedata.json"))
        val rootNode: JsonNode = ObjectMapper().readTree(parser)
        val iterator: Iterator<JsonNode> = rootNode.iterator()
        var currentNode: JsonNode
        var count = 0

        while (iterator.hasNext() && count < amount) {
            currentNode = iterator.next()
            val year: Int = currentNode.path("year").asInt()
            val title: String = currentNode.path("title").asText()
            val rating: String = currentNode.path("info").path("rating").toString()

            try {
                if (rating.isBlank()) continue
                CoroutineScope(Dispatchers.IO).async {
                    table.putItem(
                        Item()
                            .withPrimaryKey("year", year, "title", title)
                            .withDouble("rating", rating.toDouble())
                            .withJSON("info", currentNode.path("info").toString())
                    )
                    log.info("PutItem succeeded: '$year - $title'")
                }
                count++
            } catch (e: Exception) {
                log.error("Error while adding movie: '$year - $title'. Message: '${e.message}'")
                throw e
            }
        }
        parser.close()
    }
}
