package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.*
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException


class MeasurementDataUpdateViewModel(
    private val myApplication: Application,
    val ventilatorValue: VentilatorValue
) : BaseViewModel(myApplication) {

    class Factory(
        private val application: Application, val ventilatorValue: VentilatorValue
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MeasurementDataUpdateViewModel(application, ventilatorValue) as T
        }
    }

    val height: MutableLiveData<String> = MutableLiveData(ventilatorValue.height)
    val weight: MutableLiveData<String> = MutableLiveData(ventilatorValue.weight)
    val gender: MutableLiveData<Int> = MutableLiveData(ventilatorValue.gender)
    val airwayPressure: MutableLiveData<String> = MutableLiveData(ventilatorValue.airwayPressure)
    val airFlow: MutableLiveData<String> = MutableLiveData(ventilatorValue.airFlow)
    val o2Flow: MutableLiveData<String> = MutableLiveData(ventilatorValue.o2Flow)
    val rrWithUnit: MutableLiveData<String> = MutableLiveData()
    val estimatedVtWithUnit: MutableLiveData<String> = MutableLiveData()
    val estimatedMvWithUnit: MutableLiveData<String> = MutableLiveData()
    val estimatedPeepWithUnit: MutableLiveData<String> = MutableLiveData()
    val fio2WithUnit: MutableLiveData<String> = MutableLiveData()
    val registeredAt: MutableLiveData<String> = MutableLiveData(ventilatorValue.registeredAt)
    val statusUse: MutableLiveData<Int> = MutableLiveData(ventilatorValue.statusUse)
    val statusUseOther: MutableLiveData<String> = MutableLiveData(ventilatorValue.statusUseOther)
    val spo2: MutableLiveData<String> = MutableLiveData(ventilatorValue.spo2)
    val etco2: MutableLiveData<String> = MutableLiveData(ventilatorValue.etco2)
    val pao2: MutableLiveData<String> = MutableLiveData(ventilatorValue.pao2)
    val paco2: MutableLiveData<String> = MutableLiveData(ventilatorValue.paco2)

    val updatedVentilatorValueId: MutableLiveData<Int> by lazy {
        MutableLiveData()
    }

    val heightLabel: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val weightLabel: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val o2FlowLabel: MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val airFlowLabel: MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val airwayPressureLabel: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val spo2Label: MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val etco2Label: MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val pao2Label: MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val paco2Label: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    init {
        setUnit(
            heightLabel,
            context.getString(R.string.height_label),
            context.getString(R.string.height_pref_key)
        )
        setUnit(
            weightLabel,
            context.getString(R.string.weight_label),
            context.getString(R.string.weight_pref_key)
        )
        setUnit(
            o2FlowLabel,
            context.getString(R.string.o2_flow_label),
            context.getString(R.string.o2_flow_pref_key)
        )
        setUnit(
            airFlowLabel,
            context.getString(R.string.air_flow_label),
            context.getString(R.string.air_flow_pref_key)
        )
        setUnit(
            airwayPressureLabel,
            context.getString(R.string.airway_pressure_label),
            context.getString(R.string.airway_pressure_pref_key)
        )
        setUnit(
            spo2Label,
            context.getString(R.string.spo2_label),
            context.getString(R.string.spo2_pref_key)
        )
        setUnit(
            etco2Label,
            context.getString(R.string.etco2_label),
            context.getString(R.string.etco2_pref_key)
        )
        setUnit(
            pao2Label,
            context.getString(R.string.pao2_label),
            context.getString(R.string.pao2_pref_key)
        )
        setUnit(
            paco2Label,
            context.getString(R.string.paco2_label),
            context.getString(R.string.paco2_pref_key)
        )
        if (!ventilatorValue.fio2.isNullOrEmpty()) ventilatorValue.fio2?.run {
            setUnit(
                rrWithUnit,
                this,
                context.getString(R.string.rr_pref_key)
            )
        }
        if (!ventilatorValue.rr.isNullOrEmpty()) ventilatorValue.rr?.run {
            setUnit(
                fio2WithUnit,
                this,
                context.getString(R.string.fio2_pref_key)
            )
        }
        if (!ventilatorValue.estimatedVt.isNullOrEmpty()) ventilatorValue.estimatedVt?.run {
            setUnit(
                estimatedVtWithUnit,
                this,
                context.getString(R.string.estimated_vt_pref_key)
            )
        }
        if (!ventilatorValue.estimatedMv.isNullOrEmpty()) ventilatorValue.estimatedMv?.run {
            setUnit(
                estimatedMvWithUnit,
                this,
                context.getString(R.string.estimated_mv_pref_key)
            )
        }
        if (!ventilatorValue.estimatedPeep.isNullOrEmpty()) ventilatorValue.estimatedPeep?.run {
            setUnit(
                estimatedPeepWithUnit,
                this,
                context.getString(R.string.estimated_peep_pref_key)
            )
        }
    }

    val transitionToMeasurementDataDetail: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    fun onGenderSelected(itemSelected: Int) {
        if (itemSelected != 0) {
            gender.postValue(itemSelected)
        }
    }

    fun onStatusUseSelected(itemSelected: Int) {
        if (itemSelected != 0) {
            statusUse.postValue(itemSelected)
        }
    }

    fun onClickSaveMeasurementDataButton() {
        viewModelScope.launch {
            setProgressBar.value = Event(true)
            try {
                repository.updateVentilatorValue(
                    ventilatorValue.ventilatorValueId,
                    buildUpdateVentilatorValueForm(),
                    appkey,
                    userToken
                ).let { res ->
                    if (res.isSuccessful) {
                        res.body()?.result?.let {
                            showToastUpdated()
                            updatedVentilatorValueId.value = it.id
                            transitionToMeasurementDataDetail.value =
                                Event("transitionToMeasurementDataDetail")
                        }
                    } else {
                        errorHandling(res)
                    }
                }

            } catch (e: Exception) {
                showDialogConnectionError.value = Event("connection_error")
            }
            setProgressBar.value = Event(false)
        }
    }

    private fun buildUpdateVentilatorValueForm(): UpdateVentilatorValueForm =
        UpdateVentilatorValueForm(
            gender.value,
            height.value,
            weight.value,
            airwayPressure.value,
            airFlow.value,
            o2Flow.value,
            statusUse.value,
            statusUseOther.value,
            spo2.value,
            etco2.value,
            pao2.value,
            paco2.value
        )
}