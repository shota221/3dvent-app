package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.*
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException

abstract class IeMeasurementViewModel(
    private val myApplication: Application,
    val ventilatorValue: VentilatorValue
) : BaseViewModel(myApplication) {

    val transitionToVentilatorResult: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val averageInhalationTime: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val averageExhalationTime: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val averageInhalationTimeWithUnit: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val averageExhalationTimeWithUnit: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val rr: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val ieRatio: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val estimatedMv: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val estimatedVt: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    abstract fun onClickCalculateAverageButton()

    fun onClickRegisterButton() {
        viewModelScope.launch {
            setProgressBar.value = Event(true)
            try {
                val createVentilatorValueForm = CreateVentilatorValueForm(
                    sharedCurrentVentilator.ventilatorId,
                    sharedCurrentVentilator.patientId,
                    ventilatorValue.airwayPressure,
                    ventilatorValue.airFlow,
                    ventilatorValue.o2Flow,
                    rr.value,
                    averageInhalationTime.value,
                    averageExhalationTime.value,
                    ventilatorValue.predictedVt
                )

                if (!loggedIn()) {
                    repository.createVentilatorValueNoAuth(createVentilatorValueForm, sharedAccessToken.appkey)
                } else {
                    repository.createVentilatorValue(
                        createVentilatorValueForm,
                        sharedAccessToken.appkey,
                        sharedAccessToken.userToken
                    )
                }.let {
                    if(it.isSuccessful){
                        it.body()?.result?.let {
                            estimatedMv.value = it.estimatedMv
                            estimatedVt.value = it.estimatedVt
                            buildVentilatorValue()
                            transitionToVentilatorResult.value = Event("transitionToVentilatorResult")
                        }
                    } else {
                        handleErrorResponse(it)
                    }
                }
            } catch (e: ConnectException) {
                handleConnectionError()
            } finally {
                setProgressBar.value = Event(false)
            }
        }
    }

    protected fun buildVentilatorValue() {
        ventilatorValue.inspiratoryTime = averageInhalationTime.value
        ventilatorValue.expiratoryTime = averageExhalationTime.value
        ventilatorValue.rr = rr.value
        ventilatorValue.ieRatio = ieRatio.value
        ventilatorValue.estimatedMv = estimatedMv.value
        ventilatorValue.estimatedVt = estimatedVt.value
    }
}