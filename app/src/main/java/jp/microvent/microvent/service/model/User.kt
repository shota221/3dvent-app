package jp.microvent.microvent.service.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "user_name")
    val userName: String?,
    @Json(name = "organization_name")
    val organizationName: String?,
    val email: String?
):Serializable

@JsonClass(generateAdapter = true)
data class UpdateUserForm(
    @Json(name = "user_name")
    var userName:String?,
    var email: String?
)

@JsonClass(generateAdapter = true)
data class UpdatedUser(
    @Json(name = "user_name")
    val userName:String?,
    val email:String?
)