package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.model.Gender
import jp.microvent.microvent.service.model.Patient
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException


class PatientBasicInfoDetailViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val transitionToPatientBasicInfoUpdate: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val patient: MutableLiveData<Patient> by lazy {
        MutableLiveData()
    }

    val genderStr: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    fun onClickEditPatientBasicInfo() {
            transitionToPatientBasicInfoUpdate.value = Event("transitionToPatientBasicInfoUpdate")
    }

    init {
        viewModelScope.launch {
            try {
                repository.getPatient(patientId, appkey).let { res ->
                    if (res.isSuccessful) {
                        res.body()?.result?.let {
                            patient.postValue(it)
                            genderStr.postValue(Gender.buildGender(it.gender)?.getString(context))
                        }
                    } else {
                        errorHandling(res)
                    }
                }
            } catch (e: Exception) {
                showDialogConnectionError.value = Event("connect_error")
                Log.e("test", e.stackTraceToString())
            }
        }
    }
}