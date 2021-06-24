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


    fun onClickSaveVentilatorDataButton() {
        viewModelScope.launch {
            try {
                val updateVentilatorForm = UpdateVentilatorForm(startUsingAt.value)
                repository.updateVentilator(ventilatorId, updateVentilatorForm, appkey, userToken)
                    .let {
                        if (it.isSuccessful) {
                            showToastUpdated()
                            transitionToVentilatorDataDetail.value =
                                Event("transitionToVentilatorDataDetail")
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