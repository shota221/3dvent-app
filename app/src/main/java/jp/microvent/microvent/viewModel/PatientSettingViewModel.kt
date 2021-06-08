package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.CreatePatientForm
import jp.microvent.microvent.service.model.CreatedPatient
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class PatientSettingViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val isButtonEnabled: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val height: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val gender: MutableLiveData<Int> by lazy {
        MutableLiveData()
    }

    val genderStr: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val patientNumber: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val transitionToVentilatorSetting: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val createdPatient: MutableLiveData<CreatedPatient> by lazy {
        MutableLiveData()
    }

    fun onItemSelected(genderSelected: Int, genderSelectedStr: String) {
        if (genderSelected != 0) {
            gender.postValue(genderSelected)
            genderStr.postValue(genderSelectedStr)
        }
    }

    fun onClickButton() {
        viewModelScope.launch {
            try {
                val height = height.value.toString()
                val gender = gender.value?.toInt()
                val patientNumber = patientNumber.value.toString()
                val createPatientForm =
                    CreatePatientForm(height, gender, patientNumber, ventilatorId)
                if (userToken.isEmpty()) {
                    val createPatient = repository.createPatientNoAuth(createPatientForm, appkey)
                    if (createPatient.isSuccessful) {
                        createdPatient.postValue(createPatient.body()?.result)
                    }
                } else {
                    val createPatient =
                        repository.createPatient(createPatientForm, appkey, userToken)
                    if (createPatient.isSuccessful) {
                        createdPatient.postValue(createPatient.body()?.result)
                    }
                }
                transitionToVentilatorSetting.value = Event("transitionToVentilatorSetting")

            } catch (e: Exception) {
                Log.e("Submit:Failed", e.stackTraceToString())
            }
        }
    }
}