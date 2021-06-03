package jp.microvent.microvent.service.model

import androidx.lifecycle.MutableLiveData

const val OS = "android"

data class DefaultFlow(
    val air_flow: String = "",
    val o2_flow: String = "",
)

data class EstimatedData(
    val estimated_peep: String,
    val fio2: String
)

data class IeManualFetchFormDataListElm(
    var i: String?,
    var e: String?
)

data class IeManualFetchForm(
    var data: MutableList<IeManualFetchFormDataListElm>?
)

data class IeSoundFetchFormSoundElm(
    var content_type: String?,
    var file_name: String?,
    var file_data: String?
)

data class IeSoundFetchForm(
    var sound: IeSoundFetchFormSoundElm?,
    var os: String = OS
)

data class Ie(
    val i_avg: String,
    val e_avg: String,
    val rr: String
)