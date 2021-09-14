package jp.microvent.microvent.service.model

data class ErrorResponse(
    var message: String,

    var error: Error
)