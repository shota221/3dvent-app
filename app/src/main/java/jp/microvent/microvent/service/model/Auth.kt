package jp.microvent.microvent.service.model

data class CheckToken(
    val has_token: Boolean
)

data class UserTokenFetchForm(
    var name: String,
    var password: String
)

data class UserToken(
    val user_id: Int,
    val api_token: String,
    val user_name: String,
    val organization_name: String
)