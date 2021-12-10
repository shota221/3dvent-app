package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.model.Patient
import jp.microvent.microvent.service.model.UpdateVentilatorForm
import jp.microvent.microvent.service.model.Ventilator
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException


class VentilatorDataUpdateViewModel(
    private val myApplication: Application,
    val ventilator: Ventilator
) : BaseViewModel(myApplication) {

    class Factory(
        private val application: Application, val ventilator: Ventilator
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VentilatorDataUpdateViewModel(application, ventilator) as T
        }
    }

    val transitionToVentilatorDataDetail: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val serialNumber: MutableLiveData<String> = MutableLiveData(ventilator.serialNumber)

    val startUsingAt: MutableLiveData<String> = MutableLiveData(ventilator.startUsingAt)

    val showDialogDatePicker: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    fun onClickEtStartUsingAt(){
        showDialogDatePicker.value = Event("showDialogDatePicker")
    }

    fun onClickSaveVentilatorDataButton() {
        viewModelScope.launch {
            setProgressBar.value = Event(true)
            try {
                val updateVentilatorForm = UpdateVentilatorForm(startUsingAt.value)
                repository.updateVentilator(sharedCurrentVentilator.ventilatorId, updateVentilatorForm, sharedAccessToken.appkey, sharedAccessToken.userToken)
                    .let {
                        if (it.isSuccessful) {
                            showToastUpdated()
                            transitionToVentilatorDataDetail.value =
                                Event("transitionToVentilatorDataDetail")
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