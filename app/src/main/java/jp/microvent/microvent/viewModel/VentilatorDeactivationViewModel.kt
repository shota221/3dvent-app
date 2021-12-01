package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.model.DialogDescription
import jp.microvent.microvent.service.model.VentilatorValue
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception


class VentilatorDeactivationViewModel(
    private val myApplication: Application,
) : BaseViewModel(myApplication) {
    val transitionToQrReading: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    fun onClickDeactivateButton() {
        val dialogMessage = context.getString(R.string.ventilator_deactivation_confirmation)
        val dialogDescription: DialogDescription = DialogDescription(null, dialogMessage)
        showDialogConfirmation.value = Event(dialogDescription)
    }

    fun deactivateVentilator() {
        viewModelScope.launch {
            try {
                if (loggedIn()) {
                    repository.deactivateVentilator(
                        sharedCurrentVentilator.ventilatorId,
                        sharedAccessToken.appkey,
                        sharedAccessToken.userToken
                    )
                } else {
                    repository.deactivateVentilatorNoAuth(
                        sharedCurrentVentilator.ventilatorId,
                        sharedAccessToken.appkey
                    )
                }.let { res ->
                    if (res.isSuccessful) {
                        //成功時。sharedPrefのventilator情報を初期化し画面遷移
                        sharedCurrentVentilator.resetAll()
                        val dialogMessage = context.getString(R.string.ventilator_deactivated)
                        val dialogDescription: DialogDescription =
                            DialogDescription(null, dialogMessage)
                        showDialogNotification.value = Event(dialogDescription)
                        transitionToQrReading.value = Event("transitionToQrReading")
                    } else {
                        errorHandling(res)
                    }
                }
            } catch (e: Exception) {
                showToast.value = Event(context.getString(R.string.connection_error))
            }
        }
    }
}