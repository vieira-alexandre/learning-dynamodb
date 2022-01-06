package dev.alexandrevieira.learningdynamodb.gateway.http

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import dev.alexandrevieira.learningdynamodb.domain.dto.MovieRequest
import dev.alexandrevieira.learningdynamodb.domain.dto.MovieResponse
import dev.alexandrevieira.learningdynamodb.domain.projections.MovieProjection
import dev.alexandrevieira.learningdynamodb.gateway.usecase.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/movies")
class MovieController(
    private val createMovie: CreateMovieUseCase,
    private val findMovie: FindMovieUseCase,
    private val updateMovie: UpdateMovieUseCase,
    private val deleteMovie: DeleteMovieUseCase,
    private val listByYear: ListMoviesByYearUseCase,
    private val listByYearAndRatingAbove: ListMoviesByYearAndRatingAboveUseCase,
    private val listByYearAndRatingAbovePaginated: ListMoviesByYearAndRatingAbovePaginatedUseCase
) {

    @PostMapping
    fun create(@RequestBody @Valid movie: MovieRequest): ResponseEntity<Void> {
        createMovie.execute(movie.toModel())

        val uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{year}/{title}")
            .buildAndExpand(movie.year, movie.title).toUri()

       return ResponseEntity.created(uri).build()
    }

    @GetMapping("/{year}")
    fun listByYear(
        @PathVariable year: Int,
        @RequestParam(required = false) rating: Double?
    ): List<MovieProjection> {
        return if(rating == null)
            listByYear.execute(year)
        else
            listByYearAndRatingAbove.execute(year, rating)
    }

    @GetMapping("/{year}/paginated")
    fun listByYearAndRatingAbove(
        @PathVariable year: Int,
        @RequestParam rating: Double,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestBody(required = false) exclusiveStartKey: Map<String, AttributeValue>?
    ): QueryResultPage<MovieProjection> {
        return listByYearAndRatingAbovePaginated.execute(year, rating, pageSize, exclusiveStartKey ?: emptyMap())
    }

    @GetMapping("/{year}/{title}")
    fun findByYearAndTitle(
        @PathVariable year: Int,
        @PathVariable title: String
    ): MovieResponse {
        val movie = findMovie.execute(year, title)
        return MovieResponse.of(movie)
    }

    @DeleteMapping("/{year}/{title}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteByYearAndTitle(
        @PathVariable year: Int,
        @PathVariable title: String
    ) {
        deleteMovie.execute(year, title)
    }

    @PutMapping("/{year}/{title}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateByYearAndTitle(
        @PathVariable year: Int,
        @PathVariable title: String,
        @RequestBody request: MovieRequest
    ) {
        val movie = request.toModel()
        movie.year = year
        movie.title = title
        updateMovie.execute(movie)
    }
}