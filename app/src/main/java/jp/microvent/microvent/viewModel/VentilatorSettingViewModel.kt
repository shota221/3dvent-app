package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.CreatePatientForm
import jp.microvent.microvent.service.model.CreatedPatient
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class VentilatorSettingViewModel(
    private val myApplication: Application,
    private val height: String,
    private val gender: String,
    private val predictedVt: String,
) : BaseViewModel(myApplication) {

    class Factory(
        private val application: Application, private val height: String, private val gender: String, private val predictedVt: String,
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VentilatorSettingViewModel(application, height, gender, predictedVt) as T
        }
    }

    val patientHeight: MutableLiveData<String> by lazy{
        MutableLiveData()
    }

    val patientGender: MutableLiveData<String> by lazy{
        MutableLiveData()
    }

    val patientPredictedVt: MutableLiveData<String> by lazy{
        MutableLiveData()
    }

    val isButtonEnabled: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }


    val transitionToVentilatorSetting: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val createdPatient: MutableLiveData<CreatedPatient> by lazy {
        MutableLiveData()
    }

    init{
        setPatientValue()
    }

    private fun setPatientValue(){
        patientHeight.postValue(height)
        patientGender.postValue(gender)
        patientPredictedVt.postValue(predictedVt)
    }

}