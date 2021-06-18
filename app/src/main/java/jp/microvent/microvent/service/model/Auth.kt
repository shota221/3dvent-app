package jp.microvent.microvent.service.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckToken(
    @Json(name = "has_token")
    val hasToken: Boolean
)

data class CreateUserTokenForm(
    var name: String?,
    var password: String?
)

@JsonClass(generateAdapter = true)
data class CreatedUserToken(
    @Json(name = "user_id")
    val userId: Int,
    @Json(name = "api_token")
    val apiToken: String,
    @Json(name = "user_name")
    val userName: String,
    @Json(name = "organization_name")
    val organizationName: String
)

@JsonClass(generateAdapter = true)
data class DeletedUserToken(
    @Json(name = "user_id")
    val userId: Int
)