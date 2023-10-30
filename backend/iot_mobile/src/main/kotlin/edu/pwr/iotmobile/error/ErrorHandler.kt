package edu.pwr.iotmobile.error

import edu.pwr.iotmobile.error.exception.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ErrorHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(
        DashboardNotFoundException::class,
        UserNotFoundException::class,
        TokenNotFoundException::class,
        ProjectNotFoundException::class,
        InvitationNotFoundException::class
    )
    fun handleNotFound(ex: Exception): ResponseEntity<String> {
        logError(ex)
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(
        InvalidDataException::class,
        QueueException::class,
        ChannelException::class
        )
    fun handleBadRequest(ex: Exception): ResponseEntity<String> {
        logError(ex)
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoAuthenticationException::class)
    fun handleUnauthorized(ex: Exception): ResponseEntity<String> {
        logError(ex)
        return ResponseEntity(ex.message, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(TokenCodeIncorrectException::class, NotAllowedException::class)
    fun handleForbidden(ex: Exception): ResponseEntity<String> {
        logError(ex)
        return ResponseEntity(ex.message, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(
        InvitationNotPendingException::class,
        UserAlreadyExistsException::class,
        InvitationAlreadyExistsException::class,
        UserAlreadyInProjectException::class,
        TopicAlreadyExistsException::class,
        DashboardAlreadyExistsException::class
    )
    fun handleConflict(ex: Exception): ResponseEntity<String> {
        logError(ex)
        return ResponseEntity(ex.message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(InvalidStateException::class)
    fun handleInternalServerError(ex: Exception): ResponseEntity<String> {
        logError(ex)
        return ResponseEntity(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun logError(ex: Exception) {
        logger.error(ex.message, ex)
    }
}