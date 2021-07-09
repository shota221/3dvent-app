package jp.microvent.microvent.service.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class VentilatorValue(
    @Json(name = "has_observed")
    var hasObserved: Boolean? = null,
    @Json(name = "ventilator_value_id")
    var ventilatorValueId: Int? = null,
    @Json(name = "ventilator_id")
    var ventilatorId: Int? = null,
    var city: String? = null,
    var height: String? = null,
    var weight: String? = null,
    var gender: Int? = null,
    @Json(name = "ideal_weight")
    var idealWeight: String? = null,
    @Json(name = "airway_pressure")
    var airwayPressure: String? = null,
    @Json(name = "total_flow")
    var totalFlow: String? = null,
    @Json(name = "air_flow")
    var airFlow: String? = null,
    @Json(name = "o2_flow")
    var o2Flow: String? = null,
    var rr: String? = null,
    @Json(name = "vt_per_kg")
    var vtPerKg: String? = null,
    @Json(name = "predicted_vt")
    var predictedVt: String? = null,
    @Json(name = "estimated_vt")
    var estimatedVt: String? = null,
    @Json(name = "estimated_mv")
    var estimatedMv: String? = null,
    @Json(name = "estimated_peep")
    var estimatedPeep: String? = null,
    var fio2: String? = null,
    @Json(name = "fixed_flg")
    var fixedFlg: String? = null,
    @Json(name = "registered_at")
    var registeredAt: String? = null,
    @Json(name = "registered_user_name")
    var registeredUserName: String? = null,
    @Json(name = "confirmed_user_name")
    var confirmedUserName: String? = null,
    @Json(name = "status_use")
    var statusUse: Int? = null,
    @Json(name = "status_use_other")
    var statusUseOther: String? = null,
    var spo2: String? = null,
    var etco2: String? = null,
    var pao2: String? = null,
    var paco2: String? = null,
    @Json(name = "expiratory_time")
    var expiratoryTime: String? = null,
    @Json(name = "inspiratory_time")
    var inspiratoryTime: String? = null,
    @Json(name = "ie_ratio")
    var ieRatio: String? = null
) : Serializable

@JsonClass(generateAdapter = true)
data class VentilatorValueListElm(
    val id: Int?,
    @Json(name = "registered_at")
    val registeredAt: String?,
    @Json(name = "registered_user_name")
    val registeredUserName: String?
)

@JsonClass(generateAdapter = true)
data class CreateVentilatorValueForm(
    @Json(name = "ventilator_id")
    var ventilatorId: Int?,
    @Json(name = "patient_id")
    var patientId: Int?,
    @Json(name = "airway_pressure")
    var airwayPressure: String?,
    @Json(name = "air_flow")
    var airFlow: String?,
    @Json(name = "o2_flow")
    var o2Flow: String?,
    var rr: String?,
    @Json(name = "i_avg")
    var iAvg: String?,
    @Json(name = "e_avg")
    var eAvg: String?,
    @Json(name = "predicted_vt")
    var predictedVt: String?
)

@JsonClass(generateAdapter = true)
data class CreatedVentilatorValue(
    @Json(name = "ventilator_id")
    val ventilatorId: Int?,
    val fio2: String?,
    @Json(name = "estimated_vt")
    val estimatedVt: String?,
    @Json(name = "estimated_mv")
    val estimatedMv: String?,
    @Json(name = "estimated_peep")
    val estimatedPeep: String?
)

@JsonClass(generateAdapter = true)
data class UpdateVentilatorValueForm(
    var gender: Int?,
    var height: String?,
    var weight: String?,
    @Json(name = "airway_pressure")
    var airwayPressure: String?,
    @Json(name = "air_flow")
    var airFlow: String?,
    @Json(name = "o2_flow")
    var o2Flow: String?,
    @Json(name = "status_use")
    var statusUse: Int?,
    @Json(name = "status_use_other")
    var statusUseOther: String?,
    var spo2: String?,
    var etco2: String?,
    var pao2: String?,
    var paco2: String?
)

@JsonClass(generateAdapter = true)
data class UpdatedVentilatorValue(
    val id: Int?,
    @Json(name = "revised_at")
    val revisedAt: String?
)

@JsonClass(generateAdapter = true)
data class Units(
    val height: String?,

    val weight: String?,

    @Json(name = "air_flow")
    val airFlow: String?,
    @Json(name = "o2_flow")
    val o2Flow: String?,
    @Json(name = "total_flow")
    val totalFlow: String?,
    @Json(name = "estimated_mv")
    val estimatedMv: String?,

    @Json(name = "airway_pressure")
    val airwayPressure: String?,
    @Json(name = "estimated_peep")
    val estimatedPeep: String?,

    @Json(name = "vt_per_kg")
    val vtPerKg: String?,
    @Json(name = "predicted_vt")
    val predictedVt: String?,
    @Json(name = "estimated_vt")
    val estimatedVt: String?,

    val i: String?,
    val e: String?,
    @Json(name = "i_avg")
    val iAvg: String?,
    @Json(name = "e_avg")
    val eAvg: String?,

    val rr: String?,

    val fio2: String?,
    val spo2: String?,

    val etco2: String?,
    val pao2: String?,
    val paco2: String?,
)