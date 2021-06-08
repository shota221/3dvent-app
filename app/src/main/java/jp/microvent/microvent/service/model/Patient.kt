package jp.microvent.microvent.service.model

import java.io.Serializable

data class Patient(
    var patient_code: String? = null,
    var gender: Int? = null,
    var gender_str : String? = null,
    var predicted_vt: String? = null,
    var height: String? = null
):Serializable

data class CreatePatientForm(
    var height: String?,
    var gender: Int?,
    var patient_code:String?,
    var ventilator_id: Int?
)

data class CreatedPatient(
    val patient_id: String,
    val predicted_vt: String
)

data class UpdatePatientForm(
    var patient_code: String?,
    var height: String?,
    var gender: String?
)

data class UpdatedPatient(
    val patient_code: String?,
    val gender: Int,
    val predicted_vt: String,
    val height: String
)