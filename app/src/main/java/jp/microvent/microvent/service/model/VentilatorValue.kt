package jp.microvent.microvent.service.model

import java.io.Serializable

data class VentilatorValue(
    var id: Int?,
    var has_observed: Boolean?,
    var ventilator_value_id: Int?,
    var ventilator_id: Int?,
    var city: String?,
    var height: String?,
    var weight: String?,
    var gender: Int?,
    var ideal_weight: String?,
    var airway_pressure: String?,
    var total_flow: String?,
    var air_flow: String?,
    var o2_flow: String?,
    var rr: String?,
    var vt_per_kg: String?,
    var predicted_vt: String?,
    var estimated_vt: String?,
    var estimated_mv: String?,
    var estimated_peep: String?,
    var fio2: String?,
    var fixed_flg: String?,
    var registered_at: String?,
    var registered_user_name: String?,
    var confirmed_user_name: String?,
    var status_use: String?,
    var status_use_other: String?,
    var spo2: String?,
    var etco2: String?,
    var pao2: String?,
    var paco2: String?,
    var expiratory_time: String?,
    var inspiratory_time: String?,
) : Serializable

data class VentilatorValueListElm(
    val id: Int?,
    val registered_at: String?,
    val registered_user_name: String?
)

data class CreateVentilatorValueForm(
    var ventilator_id: Int?,
    var patient_id: Int?,
    var airway_pressure: String?,
    var air_flow: String?,
    var o2_flow: String?,
    var rr: String?,
    var i_avg: String?,
    var e_avg: String?,
    var predicted_vt: String?
)

data class CreatedVentilatorValue(
    val ventilator_id: Int?,
    val fio2: String?,
    val estimated_vt: String?,
    val estimated_mv: String?,
    val estimated_peep: String?
)

data class UpdateVentilatorValueForm(
    var id: Int?,
    var registered_at: String?,
    var gender: Int?,
    var height: String?,
    var weight: String?,
    var airway_pressure: String?,
    var air_flow: String?,
    var o2_flow: String?,
    var status_use: Int?,
    var status_use_other: String?,
    var spo2: String?,
    var etco2: String?,
    var pao2: String?,
    var paco2: String?
)

data class UpdatedVentilatorValue(
    val id: Int?,
    val revised_at: String?
)