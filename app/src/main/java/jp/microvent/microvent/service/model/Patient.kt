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
    var height: String? = null,
    var weight: String? = null
) : Serializable

@JsonClass(generateAdapter = true)
data class CreatePatientForm(
    var height: String?,
    var gender: Int?,
    @Json(name = "patient_code")
    var patientCode: String?,
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
    var height: String?,
    var gender: Int?,
    @Json(name = "patient_code")
    var patientCode: String?,
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

@JsonClass(generateAdapter = true)
data class PatientObs(
    @Json(name = "has_observed")
    var hasObserved: Int,
    @Json(name = "opt_out_flg")
    var optOutFlg: Int?,
    @Json(name = "patient_code")
    var patientCode: String?,
    var age: String?,
    @Json(name = "vent_disease_name")
    var ventDiseaseName: String?,
    @Json(name = "other_disease_name_1")
    var otherDiseaseName1: String?,
    @Json(name = "other_disease_name_2")
    var otherDiseaseName2: String?,
    @Json(name = "used_place")
    var usedPlace: Int?,
    var hospital: String?,
    var national: String?,
    @Json(name = "discontinuation_at")
    var discontinuationAt: Int?,
    var outcome: Int?,
    var treatment: Int?,
    @Json(name = "adverse_event_flg")
    var adverseEventFlg: Int?,
    @Json(name = "adverse_event_contents")
    var adverseEventContents: String?,
)

@JsonClass(generateAdapter = true)
data class CreatePatientObsForm(
    @Json(name = "opt_out_flg")
    var optOutFlg: Int?,
    @Json(name = "patient_code")
    var patientCode: String?,
    var age: String?,
    @Json(name = "vent_disease_name")
    var ventDiseaseName: String?,
    @Json(name = "other_disease_name_1")
    var otherDiseaseName1: String?,
    @Json(name = "other_disease_name_2")
    var otherDiseaseName2: String?,
    @Json(name = "used_place")
    var usedPlace: Int?,
    var hospital: String?,
    var national: String?,
    @Json(name = "discontinuation_at")
    var discontinuationAt: Int?,
    var outcome: Int?,
    var treatment: Int?,
    @Json(name = "adverse_event_flg")
    var adverseEventFlg: Int?,
    @Json(name = "adverse_event_contents")
    var adverseEventContents: String?,
)

@JsonClass(generateAdapter = true)
data class CreatedPatientObs(
    @Json(name = "patient_id")
    val patientId: Int,
    @Json(name = "patient_code")
    val patientCode: String?,
)

@JsonClass(generateAdapter = true)
data class UpdatePatientObsForm(
    @Json(name = "opt_out_flg")
    var optOutFlg: Int?,
    @Json(name = "patient_code")
    var patientCode: String?,
    var age: String?,
    @Json(name = "vent_disease_name")
    var ventDiseaseName: String?,
    @Json(name = "other_disease_name_1")
    var otherDiseaseName1: String?,
    @Json(name = "other_disease_name_2")
    var otherDiseaseName2: String?,
    @Json(name = "used_place")
    var usedPlace: Int?,
    var hospital: String?,
    var national: String?,
    @Json(name = "discontinuation_at")
    var discontinuationAt: Int?,
    var outcome: Int?,
    var treatment: Int?,
    @Json(name = "adverse_event_flg")
    var adverseEventFlg: Int?,
    @Json(name = "adverse_event_contents")
    var adverseEventContents: String?,
)

data class UpdatedPatientObs(
    @Json(name = "patient_id")
    val patientId: Int,
    val patient_code: String?,
)