package jp.microvent.microvent.service.model

import java.io.Serializable

data class VentilatorValue(
    var id: Int? = null,
    var has_observed: Boolean? = null,
    var ventilator_value_id: Int? = null,
    var ventilator_id: Int? = null,
    var city: String? = null,
    var height: String? = null,
    var weight: String? = null,
    var gender: Int? = null,
    var ideal_weight: String? = null,
    var airway_pressure: String? = null,
    var total_flow: String? = null,
    var air_flow: String? = null,
    var o2_flow: String? = null,
    var rr: String? = null,
    var vt_per_kg: String? = null,
    var predicted_vt: String? = null,
    var estimated_vt: String? = null,
    var estimated_mv: String? = null,
    var estimated_peep: String? = null,
    var fio2: String? = null,
    var fixed_flg: String? = null,
    var registered_at: String? = null,
    var registered_user_name: String? = null,
    var confirmed_user_name: String? = null,
    var status_use: String? = null,
    var status_use_other: String? = null,
    var spo2: String? = null,
    var etco2: String? = null,
    var pao2: String? = null,
    var paco2: String? = null,
    var expiratory_time: String? = null,
    var inspiratory_time: String? = null,
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