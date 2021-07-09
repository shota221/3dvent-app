package jp.microvent.microvent.service.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class CreatedVentilator(
    @Json(name = "ventilator_id")
    val ventilatorId: Int,
    @Json(name = "organization_name")
    val organizationName: String?,
    @Json(name = "serial_number")
    val serialNumber: String
)

@JsonClass(generateAdapter = true)
data class CreateVentilatorForm(
    @Json(name = "gs1_code")
    var gs1Code: String?,
    var latitude: String?,
    var longitude: String?
)

@JsonClass(generateAdapter = true)
data class Ventilator(
    @Json(name = "is_registered")
    val isRegistered: Boolean,
    @Json(name = "organization_code")
    val organizationCode: String?,
    @Json(name = "organization_name")
    val organizationName: String?,
    @Json(name = "ventilator_id")
    val ventilatorId: Int?,
    @Json(name = "patient_id")
    val patientId: Int?,
    @Json(name = "serial_number")
    val serialNumber: String?,
    @Json(name = "start_using_at")
    val startUsingAt: String?,
    @Json(name = "is_recommended_period")
    val isRecommendedPeriod: Boolean?,
    val units: Units,
):Serializable

@JsonClass(generateAdapter = true)
data class UpdateVentilatorForm(
    @Json(name = "start_using_at")
    val startUsingAt: String?
)

@JsonClass(generateAdapter = true)
data class UpdatedVentilator(
    @Json(name = "start_using_at")
    val startUsingAt: String
)