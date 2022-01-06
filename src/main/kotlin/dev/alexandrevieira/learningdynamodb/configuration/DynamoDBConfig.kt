package dev.alexandrevieira.learningdynamodb.configuration

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DynamoDBConfig {
    @Value("\${aws.config.endpoint}")
    private val awsEndpoint: String? = null
    @Value("\${aws.config.access-key}")
    private val awsAccessKey: String? = null
    @Value("\${aws.config.secret-key}")
    private val awsSecretKey: String? = null
    @Value("\${aws.config.region}")
    private val awsRegion: String? = null

    @Bean
    fun dynamoDBMapperConfig(): DynamoDBMapperConfig {
        return DynamoDBMapperConfig.DEFAULT
    }

    @Bean
    fun dynamoDB() : DynamoDB {
        return DynamoDB(amazonDynamoDb())
    }

    @Bean
    fun dynamoDBMapper(): DynamoDBMapper {
        return DynamoDBMapper(amazonDynamoDb())
    }

    @Bean
    fun amazonDynamoDb(): AmazonDynamoDB {
        val endpointConfiguration = AwsClientBuilder.EndpointConfiguration(awsEndpoint, awsRegion)
        val credentialsProvider = AWSStaticCredentialsProvider(BasicAWSCredentials(awsAccessKey, awsSecretKey))

        return AmazonDynamoDBClientBuilder
            .standard()
            .withEndpointConfiguration(endpointConfiguration)
            .withCredentials(credentialsProvider)
            .build()
    }
}