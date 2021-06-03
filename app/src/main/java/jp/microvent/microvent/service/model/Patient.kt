package jp.microvent.microvent.service.model

data class CreatePatientForm(
    var patient_code:String?,
    var height: String,
    var gender: Int,
    var ventilator_id: Int
)

data class CreatedPatient(
    val patient_id: String,
    val predicted_vt: String
)

data class Patient(
    val patient_code: String?,
    val gender: Int,
    val predicted_vt: String,
    val height: String
)

data class UpdatePatientForm(
    var patient_code: String?,
    var height: String,
    var gender: String
)

data class UpdatedPatient(
    val patient_code: String?,
    val gender: Int,
    val predicted_vt: String,
    val height: String
)