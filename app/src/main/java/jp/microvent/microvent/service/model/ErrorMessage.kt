package jp.microvent.microvent.service.model

data class ErrorMessage(
    val key: String?,
    val message: Message?
)

data class Message(
    val translated: String
)