package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception


class PatientInfoViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val transitionToPatientBasicInfo :MutableLiveData<Event<String>> by lazy{
        MutableLiveData()
    }

    val transitionToPatientObsData :MutableLiveData<Event<String>> by lazy{
        MutableLiveData()
    }

    val transitionToMeasurementData :MutableLiveData<Event<String>> by lazy{
        MutableLiveData()
    }

    val transitionToVentilatorData :MutableLiveData<Event<String>> by lazy{
        MutableLiveData()
    }

    val showDialogNoPatientLinked : MutableLiveData<Event<String>> by lazy{
        MutableLiveData()
    }

    val transitionToQrReading:MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    fun onClickPatientBasicInfoButton() {
        transitionToPatientBasicInfo.value = Event("transitionToPatientBasicInfo")
    }
    fun onClickPatientObsDataButton() {
        transitionToPatientObsData.value = Event("transitionToPatientObsData")
    }
    fun onClickMeasurementDataButton() {
        transitionToMeasurementData.value = Event("transitionToMeasurementData")
    }
    fun onClickVentilatorDataButton() {
        transitionToVentilatorData.value = Event("transitionToVentilatorData")
    }

    init {
        if(patientId == null){
            showDialogNoPatientLinked.value = Event("showDialogNoPatientLinked")
            transitionToQrReading.value = Event("transitionToQrReading")
        }
    }
}