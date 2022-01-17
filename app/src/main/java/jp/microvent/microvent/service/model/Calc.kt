package jp.microvent.microvent.service.model

import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

const val OS = "android"
const val WAV = "audio/wav"
const val DEFAULT_FILE_NAME = "recording.wav"

@JsonClass(generateAdapter = true)
data class DefaultFlow(
    @Json(name = "air_flow")
    val airFlow: String = "",
    @Json(name = "o2_flow")
    val o2Flow: String = "",
)

@JsonClass(generateAdapter = true)
data class EstimatedData(
    @Json(name = "estimated_peep")
    val estimatedPeep: String,
    val fio2: String
)

@JsonClass(generateAdapter = true)
data class IeManualFetchFormDataListElm(
    var e: String?,
    @Json(name = "respirations_per_10sec")
    var respirationsPer10sec: String?
)

data class IeManualFetchForm(
    var data: MutableList<IeManualFetchFormDataListElm>?
)

@JsonClass(generateAdapter = true)
data class IeSoundFetchFormSoundElm(
    @Json(name = "content_type")
    var contentType: String = WAV,
    var filename: String = DEFAULT_FILE_NAME,
    @Json(name = "file_data")
    var fileData: String?
)

data class IeSoundFetchForm(
    var sound: IeSoundFetchFormSoundElm?,
    var os: String = OS
)

@JsonClass(generateAdapter = true)
data class Ie(
    @Json(name = "i_avg")
    val iAvg: String,
    @Json(name = "e_avg")
    val eAvg: String,
    val rr: String,
    @Json(name = "ie_ratio")
    val ieRatio:String
)