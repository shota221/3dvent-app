package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.*
import jp.microvent.microvent.view.ui.VentilatorSettingFragmentArgs
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class ManualMeasurementViewModel(
    private val myApplication: Application,
    public val ventilatorValue: VentilatorValue
) : BaseViewModel(myApplication) {

    class Factory(
        private val application: Application, private val ventilatorValue: VentilatorValue
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ManualMeasurementViewModel(application, ventilatorValue) as T
        }
    }

    val transitionToVentilatorResult: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val inhalationFirst: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val inhalationSecond: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val exhalationFirst: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val exhalationSecond: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val averageInhalationTime: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val averageExhalationTime: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val rr: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val estimatedMv: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val estimatedVt: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    fun onClickCalculateAverageButton() {
        viewModelScope.launch {
            try {
                val ieFirst =
                    IeManualFetchFormDataListElm(inhalationFirst.value, exhalationFirst.value)
                val ieSecond =
                    IeManualFetchFormDataListElm(inhalationSecond.value, exhalationSecond.value)
                val ieManualFetchFormDataList: MutableList<IeManualFetchFormDataListElm> =
                    mutableListOf(ieFirst, ieSecond)
                val ieManualFetchFrom = IeManualFetchForm(ieManualFetchFormDataList)
                val calcIeManual = repository.calcIeManual(ieManualFetchFrom, appkey)
                if (calcIeManual.isSuccessful) {
                    val calcIeManualResult = calcIeManual.body()?.result
                    if (calcIeManualResult != null) {
                        averageInhalationTime.postValue(calcIeManualResult.i_avg)
                        averageExhalationTime.postValue(calcIeManualResult.e_avg)
                        rr.postValue(calcIeManualResult.rr)
                    }
                }
            } catch (e: Exception) {
                Log.e("calculate:Failed", e.stackTrace.toString())
            }
        }
    }

    fun onClickRegisterButton() {
        viewModelScope.launch {
            try {
                val createVentilatorValueForm = CreateVentilatorValueForm(
                    ventilatorId,
                    patientId,
                    ventilatorValue.airway_pressure,
                    ventilatorValue.air_flow,
                    ventilatorValue.o2_flow,
                    rr.value,
                    averageInhalationTime.value,
                    averageExhalationTime.value,
                    ventilatorValue.predicted_vt
                )

                val createVentilatorValueResult = if (userToken.isNullOrEmpty()) {
                    val createVentilatorValue =
                        repository.createVentilatorValueNoAuth(createVentilatorValueForm, appkey)
                    createVentilatorValue.body()?.result
                } else {
                    val createVentilatorValue = repository.createVentilatorValue(
                        createVentilatorValueForm,
                        appkey,
                        userToken
                    )
                    createVentilatorValue.body()?.result
                }

                if(createVentilatorValueResult != null){
                    estimatedMv.value = createVentilatorValueResult.estimated_mv
                    estimatedVt.value = createVentilatorValueResult.estimated_vt
                    buildVentilatorValue()
                    transitionToVentilatorResult.value = Event("transitionToVentilatorResult")
                }
            } catch (e: Exception) {
                Log.e("register:Failed", e.stackTraceToString())
            }
        }
    }

    private fun buildVentilatorValue() {
        ventilatorValue.inspiratory_time = averageInhalationTime.value
        ventilatorValue.expiratory_time = averageExhalationTime.value
        ventilatorValue.rr = rr.value
        ventilatorValue.estimated_mv = estimatedMv.value
        ventilatorValue.estimated_vt = estimatedVt.value
    }
}