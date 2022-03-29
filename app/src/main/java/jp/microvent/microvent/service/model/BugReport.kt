package jp.microvent.microvent.service.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateBugReportForm(
    @Json(name = "ventilator_id")
    val ventilatorId: Int?,
    @Json(name = "bug_name")
    val bugName: String?,
    @Json(name = "request_improvement")
    val requestImprovement: String?
)

@JsonClass(generateAdapter = true)
data class CreatedBugReport(
    @Json(name = "ventilator_id")
    val ventilatorId: Int
)
