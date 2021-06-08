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

    val predictedVt: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val patient: Patient = Patient()

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
                val createPatient = if (userToken.isNullOrEmpty()) {
                    repository.createPatientNoAuth(createPatientForm, appkey)
                } else {
                    repository.createPatient(createPatientForm, appkey, userToken)
                }
                if (createPatient.isSuccessful) {
                    val createPatientResult = createPatient.body()?.result
                    if(createPatientResult != null){
                        //postValueだと非同期となりセットされる前に画面遷移されるためここはsetValueを使う
                        predictedVt.value = createPatientResult.predicted_vt
                        with(currentVentilatorPref.edit()) {
                            putInt(
                                "id",
                                createPatientResult.patient_id.toInt()
                            )

                            commit()
                        }
                        buildPatient()
                        transitionToVentilatorSetting.value =
                            jp.microvent.microvent.viewModel.util.Event("transitionToVentilatorSetting")
                    }

                }


            } catch (e: Exception) {
                Log.e("Submit:Failed", e.stackTraceToString())
            }
        }
    }

    private fun buildPatient() {
        patient.patient_code = patientNumber.value
        patient.gender = gender.value
        patient.gender_str = genderStr.value
        patient.height = height.value
        patient.predicted_vt = predictedVt.value
    }
}