package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.service.enum.Gender
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.model.Ventilator
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException


class VentilatorDataDetailViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val ventilator:MutableLiveData<Ventilator> by lazy {
        MutableLiveData()
    }

    val transitionToVentilatorDataUpdate:MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    fun onClickEditVentilatorDataButton() {
        transitionToVentilatorDataUpdate.value = Event("transitionToVentilatorDataUpdate")
    }

    init {
        viewModelScope.launch {
            try {
                if(loggedIn()){
                    repository.getVentilator(sharedCurrentVentilator.gs1Code, sharedAccessToken.appkey, sharedAccessToken.userToken)
                }else{
                    repository.getVentilatorNoAuth(sharedCurrentVentilator.gs1Code, sharedAccessToken.appkey)
                }.let { res ->
                    if (res.isSuccessful) {
                        res.body()?.result?.let {
                            ventilator.postValue(it)
                        }
                    } else {
                        handleErrorResponse(res)
                    }
                }
            } catch (e: ConnectException) {
                handleConnectionError()
            }
        }
    }
}