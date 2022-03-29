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
import java.net.ConnectException


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

    val o2FlowWithUnit: MutableLiveData<String> = MutableLiveData()
    val fio2WithUnit: MutableLiveData<String> = MutableLiveData()
    val rrWithUnit: MutableLiveData<String> = MutableLiveData()
    val estimatedVtWithUnit: MutableLiveData<String> = MutableLiveData()
    val estimatedMvWithUnit: MutableLiveData<String> = MutableLiveData()
    val estimatedPeepWithUnit: MutableLiveData<String> = MutableLiveData()
    val heightWithUnit: MutableLiveData<String> = MutableLiveData()
    val paco2WithUnit: MutableLiveData<String> = MutableLiveData()
    val spo2WithUnit: MutableLiveData<String> = MutableLiveData()
    val etco2WithUnit: MutableLiveData<String> = MutableLiveData()
    val pao2WithUnit: MutableLiveData<String> = MutableLiveData()
    val weightWithUnit: MutableLiveData<String> = MutableLiveData()
    val airwayPressureWithUnit: MutableLiveData<String> = MutableLiveData()
    val airFlowWithUnit: MutableLiveData<String> = MutableLiveData()


    fun onClickEditMeasurementDataButton() {
        transitionToMeasurementDataUpdate.value = Event("transitionToMeasurementDataUpdate")
    }

    init {
        viewModelScope.launch {
            try {
                repository.getVentilatorValue(ventilatorValueId, sharedAccessToken.appkey).let { res ->
                    if (res.isSuccessful) {
                        res.body()?.result?.let {
                            ventilatorValue.postValue(it)
                            if(!it.o2Flow.isNullOrEmpty())it.o2Flow?.run{ sharedUnits.setUnit(o2FlowWithUnit, this,context.getString(R.string.o2_flow_pref_key))}
                            if(!it.fio2.isNullOrEmpty())it.fio2?.run{ sharedUnits.setUnit(fio2WithUnit, this,context.getString(R.string.fio2_pref_key))}
                            if(!it.rr.isNullOrEmpty())it.rr?.run{ sharedUnits.setUnit(rrWithUnit, this,context.getString(R.string.rr_pref_key))}
                            if(!it.estimatedVt.isNullOrEmpty())it.estimatedVt?.run{ sharedUnits.setUnit(estimatedVtWithUnit, this,context.getString(R.string.estimated_vt_pref_key))}
                            if(!it.estimatedMv.isNullOrEmpty())it.estimatedMv?.run{ sharedUnits.setUnit(estimatedMvWithUnit, this,context.getString(R.string.estimated_mv_pref_key))}
                            if(!it.estimatedPeep.isNullOrEmpty())it.estimatedPeep?.run{ sharedUnits.setUnit(estimatedPeepWithUnit, this,context.getString(R.string.estimated_peep_pref_key))}
                            if(!it.height.isNullOrEmpty())it.height?.run{ sharedUnits.setUnit(heightWithUnit, this,context.getString(R.string.height_pref_key))}
                            if(!it.paco2.isNullOrEmpty())it.paco2?.run{ sharedUnits.setUnit(paco2WithUnit, this,context.getString(R.string.paco2_pref_key))}
                            if(!it.spo2.isNullOrEmpty())it.spo2?.run{ sharedUnits.setUnit(spo2WithUnit, this,context.getString(R.string.spo2_pref_key))}
                            if(!it.etco2.isNullOrEmpty())it.etco2?.run{ sharedUnits.setUnit(etco2WithUnit, this,context.getString(R.string.etco2_pref_key))}
                            if(!it.pao2.isNullOrEmpty())it.pao2?.run{ sharedUnits.setUnit(pao2WithUnit, this,context.getString(R.string.pao2_pref_key))}
                            if(!it.weight.isNullOrEmpty())it.weight?.run{ sharedUnits.setUnit(weightWithUnit, this,context.getString(R.string.weight_pref_key))}
                            if(!it.airwayPressure.isNullOrEmpty())it.airwayPressure?.run{ sharedUnits.setUnit(airwayPressureWithUnit, this,context.getString(R.string.airway_pressure_pref_key))}
                            if(!it.airFlow.isNullOrEmpty())it.airFlow?.run{ sharedUnits.setUnit(airFlowWithUnit, this,context.getString(R.string.air_flow_pref_key))}
                            genderStr.postValue(Gender.buildGender(it.gender)?.getString(context))
                            statusUseStr.postValue(StatusUse.buildStatusUse(it.statusUse)?.getString(context))
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