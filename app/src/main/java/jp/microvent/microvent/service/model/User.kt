package jp.microvent.microvent.service.model

data class User(
    val user_name: String?,
    val organization_name: String?,
    val email: String?
)

data class updateUserForm(
    var user_name:String?,
    var email: String?
)

data class updatedUser(
    val user_name:String?,
    val email:String?
)