package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.enum.Gender
import jp.microvent.microvent.service.enum.StatusUse
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.model.Patient
import jp.microvent.microvent.service.model.VentilatorValue
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception


class MeasurementDataDetailViewModel(
    private val myApplication: Application,
    val ventilatorValueId: Int
) : BaseViewModel(myApplication) {

    class Factory(
        private val application: Application, val ventilatorValueId: Int
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MeasurementDataDetailViewModel(application, ventilatorValueId) as T
        }
    }

    val ventilatorValue: MutableLiveData<VentilatorValue> by lazy {
        MutableLiveData()
    }

    val genderStr: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val statusUseStr: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val transitionToMeasurementDataUpdate: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    fun onClickEditMeasurementDataButton() {
        transitionToMeasurementDataUpdate.value = Event("transitionToMeasurementDataUpdate")
    }

    init {
        viewModelScope.launch {
            try {
                repository.getVentilatorValue(ventilatorValueId, appkey).let { res ->
                    if (res.isSuccessful) {
                        res.body()?.result?.let {
                            ventilatorValue.postValue(it)
                            genderStr.postValue(Gender.buildGender(it.gender)?.getString(context))
                            statusUseStr.postValue(StatusUse.buildStatusUse(it.statusUse)?.getString(context))
                        }
                    } else {
                        errorHandling(res)
                    }
                }
            } catch (e: Exception) {
                showDialogConnectionError.value = Event("connect_error")
            }
        }
    }
}