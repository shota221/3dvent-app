package jp.microvent.microvent.service.model

data class CheckToken(
    val has_token: Boolean
)

data class CreateUserTokenForm(
    var name: String?,
    var password: String?
)

data class CreatedUserToken(
    val user_id: Int,
    val api_token: String,
    val user_name: String,
    val organization_name: String
)

data class DeletedUserToken(
    val user_id: Int
)