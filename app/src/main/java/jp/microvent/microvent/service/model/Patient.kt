package jp.microvent.microvent.service.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Patient(
    @Json(name = "patient_code")
    var patientCode: String? = null,
    var gender: Int? = null,
    @Json(name = "predicted_vt")
    var predictedVt: String? = null,
    var height: String? = null
):Serializable

@JsonClass(generateAdapter = true)
data class CreatePatientForm(
    var height: String?,
    var gender: Int?,
    @Json(name = "patient_code")
    var patientCode:String?,
    @Json(name = "ventilator_id")
    var ventilatorId: Int?
)

@JsonClass(generateAdapter = true)
data class CreatedPatient(
    @Json(name = "patient_id")
    val patientId: String,
    @Json(name = "predicted_vt")
    val predictedVt: String
)

@JsonClass(generateAdapter = true)
data class UpdatePatientForm(
    @Json(name = "patient_code")
    var patientCode: String?,
    var height: String?,
    var gender: String?
)
@JsonClass(generateAdapter = true)
data class UpdatedPatient(
    @Json(name = "patient_code")
    val patientCode: String?,
    val gender: Int,
    @Json(name = "predicted_vt")
    val predictedVt: String,
    val height: String
)