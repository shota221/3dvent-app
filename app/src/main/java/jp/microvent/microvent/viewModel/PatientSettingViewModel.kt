package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.camera.core.impl.MutableOptionsBundle
import androidx.lifecycle.*
import jp.microvent.microvent.R
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

    val weight: MutableLiveData<String> by lazy {
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
    val transitionToQrReading: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val predictedVt: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val patient: Patient = Patient()

    val heightLabel:MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val weightLabel:MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    init {
        if(sharedCurrentVentilator.ventilatorId == null) {
            transitionToQrReading.value = Event("transitionToQrReading")
        }
        //現在読み込んでいるventilatorにpatientId登録済みである場合はventilator_settingへ移動
        if(sharedCurrentVentilator.patientId != null) {
            transitionToVentilatorSetting.value = Event("transitionToVentilatorSetting")
        }
        sharedUnits.setUnit(heightLabel,context.getString(R.string.height_label),context.getString(R.string.height_pref_key))
        sharedUnits.setUnit(weightLabel,context.getString(R.string.weight_label),context.getString(R.string.weight_pref_key))
    }

    fun onItemSelected(genderSelected: Int) {
        if (genderSelected != 0) {
            gender.postValue(genderSelected)
        }
    }

    fun onClickButton() {
        viewModelScope.launch {
            setProgressBar.value = Event(true)
            try {
                val height = height.value.toString()
                val weight = weight.value
                val gender = gender.value?.toInt()
                val patientNumber = patientNumber.value
                val createPatientForm =
                    CreatePatientForm(height, weight, gender, patientNumber, sharedCurrentVentilator.ventilatorId)
                if (!loggedIn()) {
                    repository.createPatientNoAuth(createPatientForm, sharedAccessToken.appkey)
                } else {
                    repository.createPatient(createPatientForm, sharedAccessToken.appkey, sharedAccessToken.userToken)
                }.let {
                    if (it.isSuccessful) {
                        it.body()?.result?.let {
                            //postValueだと非同期となりセットされる前に画面遷移されるためここはsetValueを使う
                            predictedVt.value = it.predictedVt
                            sharedCurrentVentilator.patientId = it.patientId
                            transitionToVentilatorSetting.value = Event("transitionToVentilatorSetting")
                        }

                    } else {
                        handleErrorResponse(it)
                    }
                }


            } catch (e: ConnectException) {
                handleConnectionError()
            }

            setProgressBar.value = Event(false)
        }
    }
}