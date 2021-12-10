package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.CreatePatientForm
import jp.microvent.microvent.service.model.CreatedPatient
import jp.microvent.microvent.service.model.Patient
import jp.microvent.microvent.service.model.VentilatorValue
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class VentilatorResultViewModel(
    private val myApplication: Application,
    val ventilatorValue: VentilatorValue
) : BaseViewModel(myApplication) {

    class Factory(
        private val application: Application, private val ventilatorValue: VentilatorValue
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VentilatorResultViewModel(application, ventilatorValue) as T
        }
    }

    val o2Flow: MutableLiveData<String> = MutableLiveData()
    val rr: MutableLiveData<String> = MutableLiveData()
    val iAvg: MutableLiveData<String> = MutableLiveData()
    val eAvg: MutableLiveData<String> = MutableLiveData()
    val fio2: MutableLiveData<String> = MutableLiveData()
    val predictedVt: MutableLiveData<String> = MutableLiveData()
    val estimatedVt: MutableLiveData<String> = MutableLiveData()
    val estimatedMv: MutableLiveData<String> = MutableLiveData()
    val estimatedPeep: MutableLiveData<String> = MutableLiveData()
    val airwayPressure: MutableLiveData<String> = MutableLiveData()
    val airFlow: MutableLiveData<String> = MutableLiveData()

    init {
        ventilatorValue.o2Flow?.let{sharedUnits.setUnit(o2Flow, it,context.getString(R.string.o2_flow_pref_key))}
        ventilatorValue.fio2?.let{sharedUnits.setUnit(fio2, it,context.getString(R.string.fio2_pref_key))}
        ventilatorValue.rr?.let{sharedUnits.setUnit(rr, it,context.getString(R.string.rr_pref_key))}
        ventilatorValue.predictedVt?.let{sharedUnits.setUnit(predictedVt, it,context.getString(R.string.predicted_vt_pref_key))}
        ventilatorValue.estimatedVt?.let{sharedUnits.setUnit(estimatedVt, it,context.getString(R.string.estimated_vt_pref_key))}
        ventilatorValue.estimatedMv?.let{sharedUnits.setUnit(estimatedMv, it,context.getString(R.string.estimated_mv_pref_key))}
        ventilatorValue.estimatedPeep?.let{sharedUnits.setUnit(estimatedPeep, it,context.getString(R.string.estimated_peep_pref_key))}
        ventilatorValue.airwayPressure?.let{sharedUnits.setUnit(airwayPressure, it,context.getString(R.string.airway_pressure_pref_key))}
        ventilatorValue.inspiratoryTime?.let{sharedUnits.setUnit(iAvg, it,context.getString(R.string.i_avg_pref_key))}
        ventilatorValue.expiratoryTime?.let{sharedUnits.setUnit(eAvg, it,context.getString(R.string.e_avg_pref_key))}
        ventilatorValue.airFlow?.let{sharedUnits.setUnit(airFlow, it,context.getString(R.string.air_flow_pref_key))}
    }

    val transitionToVentilatorSetting: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    fun onClickButton() {
        transitionToVentilatorSetting.value = Event("transitionToVentilatorSetting")
    }

}