package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.CreatePatientForm
import jp.microvent.microvent.service.model.CreatedPatient
import jp.microvent.microvent.service.model.Patient
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException

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

    val patientNumber: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val transitionToVentilatorSetting: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val predictedVt: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val patient: Patient = Patient()

    fun onItemSelected(genderSelected: Int) {
        if (genderSelected != 0) {
            gender.postValue(genderSelected)
        }
    }

    fun onClickButton() {
        viewModelScope.launch {
            try {
                val height = height.value.toString()
                val gender = gender.value?.toInt()
                val patientNumber = patientNumber.value
                val createPatientForm =
                    CreatePatientForm(height, gender, patientNumber, ventilatorId)
                if (!loggedIn()) {
                    repository.createPatientNoAuth(createPatientForm, appkey)
                } else {
                    repository.createPatient(createPatientForm, appkey, userToken)
                }.let {
                    if (it.isSuccessful) {
                        it.body()?.result?.let {
                            //postValueだと非同期となりセットされる前に画面遷移されるためここはsetValueを使う
                            predictedVt.value = it.predictedVt
                            with(currentVentilatorPref.edit()) {
                                putInt(
                                    "patientId",
                                    it.patientId.toInt()
                                )

                                commit()
                            }
                            buildPatient()
                            transitionToVentilatorSetting.value =
                                jp.microvent.microvent.viewModel.util.Event("transitionToVentilatorSetting")
                        }

                    } else {
                        Log.i("test",createPatientForm.toString())
                        Log.e("Submit:Failed", it.errorBody().toString())
                    }
                }


            } catch (e: ConnectException) {
                Log.e("Submit:Failed", e.stackTraceToString())
                showDialogConnectionError.value = Event("connection_error")
            }
        }
    }

    private fun buildPatient() {
        patient.patientCode = patientNumber.value
        patient.gender = gender.value
        patient.height = height.value
        patient.predictedVt = predictedVt.value
    }
}