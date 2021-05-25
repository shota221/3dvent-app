package jp.microvent.microvent.service.model

data class User(
    var name: String = "",
    var api_token: String = "",
    var organization_name: String = "未登録"
)