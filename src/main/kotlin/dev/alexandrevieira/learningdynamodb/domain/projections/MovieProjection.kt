package dev.alexandrevieira.learningdynamodb.domain.projections

import java.math.BigDecimal

data class MovieProjection (
    val year: Int,
    val title: String,
    val rating: BigDecimal?
)
