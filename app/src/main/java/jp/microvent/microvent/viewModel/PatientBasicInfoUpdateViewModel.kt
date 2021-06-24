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

    val gender: MutableLiveData<Int> = MutableLiveData(patient.gender)

    val patientNumber: MutableLiveData<String> = MutableLiveData(patient.patientCode)

    val heightLabel: MutableLiveData<String> = MutableLiveData()

    val transitionToPatientBasicInfoDetail: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    init {
        setUnit(
            heightLabel,
            context.getString(R.string.height_label),
            context.getString(R.string.height_pref_key)
        )
    }

    fun onItemSelected(genderSelected: Int) {
        if (genderSelected != 0) {
            gender.postValue(genderSelected)
        }
    }

    fun onClickSavePatientBasicInfoButton() {
        viewModelScope.launch {
            try {
                val height = height.value
                val gender = gender.value
                val patientNumber = patientNumber.value
                val updatePatientForm =
                    UpdatePatientForm(height, gender, patientNumber)
                if (!loggedIn()) {
                    repository.updatePatientNoAuth(patientId, updatePatientForm, appkey)
                } else {
                    repository.updatePatient(patientId, updatePatientForm, appkey, userToken)
                }.let { res ->
                    if (res.isSuccessful) {
                        showToastUpdated()
                        transitionToPatientBasicInfoDetail.value =
                            Event("transitionToBasicInfoDetail")
                    } else {
                        errorHandling(res)
                    }
                }



            } catch (e: Exception) {
                showDialogConnectionError.value = Event("connection_error")
            }
        }

    }
}