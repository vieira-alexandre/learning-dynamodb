package dev.alexandrevieira.learningdynamodb.exception.handler

import dev.alexandrevieira.learningdynamodb.exception.DuplicateKeyException
import dev.alexandrevieira.learningdynamodb.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(e : NotFoundException) = Unit

    @ExceptionHandler(DuplicateKeyException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleDuplicateKeyException(e : DuplicateKeyException) = Unit
}