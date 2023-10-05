package edu.pwr.iotmobile.error

import edu.pwr.iotmobile.error.exception.TokenCodeIncorrectException
import edu.pwr.iotmobile.error.exception.UserNotFoundException
import edu.pwr.iotmobile.error.exception.TokenNotFoundException
import edu.pwr.iotmobile.error.exception.UserAlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ErrorHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(UserNotFoundException::class, TokenNotFoundException::class)
    fun handleNotFound(ex: Exception): ResponseEntity<String> {
        logError(ex)
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleBadRequest(ex: Exception): ResponseEntity<String> {
        logError(ex)
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(TokenCodeIncorrectException::class)
    fun handleUnauthorized(ex: Exception): ResponseEntity<String> {
        logError(ex)
        return ResponseEntity(ex.message, HttpStatus.UNAUTHORIZED)
    }

    private fun logError(ex: Exception) {
        logger.error(ex.message, ex)
    }
}