package jp.microvent.microvent.service.model

data class CreatedVentilator(
    val ventilator_id: Int,
    val organization_name: String?,
    val serial_number: String
)

data class CreateVentilatorForm(
    var gs1_code: String?,
    var latitude: String?,
    var longitude: String?
)

data class Ventilator(
    val is_registered: Boolean,
    val organization_checked: Boolean?,
    val organization_code: String?,
    val organization_name: String?,
    val ventilator_id: Int?,
    val patient_id: Int?,
    val serial_number: String?,
    val start_using_at: String?
)

data class UpdateVentilatorForm(
    val start_using_at: String?
)

data class UpdatedVentilator(
    val start_using_at: String
)