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

    init {
        if(ventilatorId == null) {
            transitionToQrReading.value = Event("transitionToQrReading")
        }
        //現在読み込んでいるventilatorにpatientId登録済みである場合はventilator_settingへ移動
        if(patientId != null) {
            transitionToVentilatorSetting.value = Event("transitionToVentilatorSetting")
        }
        setUnit(heightLabel,context.getString(R.string.height_label),context.getString(R.string.height_pref_key))
    }

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
                            transitionToVentilatorSetting.value =
                                jp.microvent.microvent.viewModel.util.Event("transitionToVentilatorSetting")
                        }

                    } else {
                        errorHandling(it)
                    }
                }


            } catch (e: Exception) {
                showDialogConnectionError.value = Event("connection_error")
            }
        }
    }
}