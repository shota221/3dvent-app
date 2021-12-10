package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException

class SettingViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val loggedIn: MutableLiveData<Boolean> by lazy {
        MutableLiveData(loggedIn())
    }

    val hasReadQr: MutableLiveData<Boolean> by lazy {
        MutableLiveData(hasReadQr())
    }

    val transitionToUserData: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val transitionToVentilatorDeactivation: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    fun onClickLogoutButton() {
        try {
            sharedAccessToken.resetUserToken().let {
                transitionToAuth.value = Event("transitionToAuth")
            }
        } catch (e: ConnectException) {
            handleConnectionError()
        }
    }

    fun onClickUserDataButton() {
        transitionToUserData.value = Event("transitionToUserData")
    }

    fun onClickVentilatorDeactivationButton() {
        transitionToVentilatorDeactivation.value = Event("transitionToVentilatorDeactivation")
    }

}