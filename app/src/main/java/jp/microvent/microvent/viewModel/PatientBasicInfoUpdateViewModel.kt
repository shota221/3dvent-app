package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.camera.core.impl.MutableOptionsBundle
import androidx.lifecycle.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.CreatePatientForm
import jp.microvent.microvent.service.model.CreatedPatient
import jp.microvent.microvent.service.model.Patient
import jp.microvent.microvent.service.model.UpdatePatientForm
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException

class PatientBasicInfoUpdateViewModel(
    private val myApplication: Application,
    val patient: Patient
) : BaseViewModel(myApplication) {

    class Factory(
        private val application: Application, val patient: Patient
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PatientBasicInfoUpdateViewModel(application, patient) as T
        }
    }

    val height: MutableLiveData<String> = MutableLiveData(patient.height)

    val weight: MutableLiveData<String> = MutableLiveData(patient.weight)

    val gender: MutableLiveData<Int> = MutableLiveData(patient.gender)

    val patientNumber: MutableLiveData<String> = MutableLiveData(patient.patientCode)

    val heightLabel: MutableLiveData<String> = MutableLiveData()

    val weightLabel: MutableLiveData<String> = MutableLiveData()

    val transitionToPatientBasicInfoDetail: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    init {
        sharedUnits.setUnit(
            heightLabel,
            context.getString(R.string.height_label),
            context.getString(R.string.height_pref_key)
        )
        sharedUnits.setUnit(
            weightLabel,
            context.getString(R.string.weight_label),
            context.getString(R.string.weight_pref_key)
        )
    }

    fun onItemSelected(genderSelected: Int) {
        if (genderSelected != 0) {
            gender.postValue(genderSelected)
        }
    }

    fun onClickSavePatientBasicInfoButton() {
        viewModelScope.launch {
            setProgressBar.value = Event(true)
            try {
                val height = height.value
                val weight = weight.value
                val gender = gender.value
                val patientNumber = patientNumber.value
                val updatePatientForm =
                    UpdatePatientForm(height, weight, gender, patientNumber)
                if (!loggedIn()) {
                    repository.updatePatientNoAuth(sharedCurrentVentilator.patientId, updatePatientForm, sharedAccessToken.appkey)
                } else {
                    repository.updatePatient(sharedCurrentVentilator.patientId, updatePatientForm, sharedAccessToken.appkey, sharedAccessToken.userToken)
                }.let { res ->
                    if (res.isSuccessful) {
                        sharedCurrentVentilator.patientHeight = height
                        sharedCurrentVentilator.patientGender = gender
                        showToastUpdated()
                        transitionToPatientBasicInfoDetail.value =
                            Event("transitionToBasicInfoDetail")
                    } else {
                        handleErrorResponse(res)
                    }
                }
            } catch (e: ConnectException) {
                handleConnectionError()
            }
            setProgressBar.value = Event(false)
        }

    }
}