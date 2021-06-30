package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.*
import jp.microvent.microvent.service.enum.Gender
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class VentilatorSettingViewModel(
    private val myApplication: Application,
) : BaseViewModel(myApplication) {

    val patient: MutableLiveData<Patient> by lazy {
        MutableLiveData()
    }

    val genderStr: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val transitionToSoundMeasurement: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val transitionToManualMeasurement: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val o2Flow: MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val airFlow: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val airwayPressure: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val fio2: MediatorLiveData<String> by lazy {
        MediatorLiveData()
    }

    val estimatedPeep: MediatorLiveData<String> by lazy {
        MediatorLiveData()
    }

    val fio2WithUnit: MediatorLiveData<String> by lazy {
        MediatorLiveData()
    }

    val estimatedPeepWithUnit: MediatorLiveData<String> by lazy {
        MediatorLiveData()
    }

    val isValidSpo2: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val isValidRr: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val isButtonEnabled: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData()
    }

    val ventilatorValue: VentilatorValue by lazy {
        VentilatorValue()
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

    val heightWithUnit: MutableLiveData<String> = MutableLiveData()
    val predictedVt: MutableLiveData<String> = MutableLiveData()
    val predictedVtWithUnit: MutableLiveData<String> = MutableLiveData()


    init {
        /**
         * 単位設定
         */
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

        /**
         * 現在観察中のpatientIdから患者情報を取得
         */
        viewModelScope.launch {
            try {
                repository.getPatient(patientId, appkey).let { res ->
                    if (res.isSuccessful) {
                        res.body()?.result?.let { patient ->
                            patient.height?.let {
                                setUnit(
                                    heightWithUnit,
                                    it,
                                    context.getString(R.string.height_pref_key)
                                )
                            }
                            predictedVt.postValue(patient.predictedVt)
                            patient.predictedVt?.let {
                                setUnit(
                                    predictedVtWithUnit,
                                    it,
                                    context.getString(R.string.predicted_vt_pref_key)
                                )
                            }
                            genderStr.postValue(
                                Gender.buildGender(patient.gender)?.getString(context)
                            )
                        }
                    } else {
                        errorHandling(res)
                    }
                }
            } catch (e: Exception) {
                showDialogConnectionError.value = Event("connect_error")
            }
        }

        /**
         * チェックボックス2つを監視、両方チェック時のみボタンを有効に
         */
        val checkBoxObserver = Observer<Boolean> {
            val isValidRr = isValidRr.value ?: false
            val isValidSpo2 = isValidSpo2.value ?: false
            isButtonEnabled.value = isValidRr && isValidSpo2
        }

        isButtonEnabled.addSource(isValidRr, checkBoxObserver)
        isButtonEnabled.addSource(isValidSpo2, checkBoxObserver)

        /**
         * airwayPressure,airFlow,o2Flowを監視、リアルタイムでfio2やestimatedPeepを返却
         */
        val ventilatorSettingObserver = Observer<String> {
            val airwayPressure = airwayPressure.value ?: ""
            val airFlow = airFlow.value ?: ""
            val o2Flow = o2Flow.value ?: ""

            viewModelScope.launch {
                try {
                    repository.calcEstimatedData(airwayPressure, airFlow, o2Flow, appkey)
                        .let { res ->
                            if (res.isSuccessful) {
                                res.body()?.result?.let {
                                    fio2.value = it.fio2
                                    setUnit(fio2WithUnit, it.fio2, context.getString(R.string.fio2_pref_key))

                                    estimatedPeep.value = it.estimatedPeep
                                    setUnit(estimatedPeepWithUnit, it.estimatedPeep, context.getString(R.string.estimated_peep_pref_key))
                                }
                            }
                        }

                } catch (e: Exception) {
                    Log.e("calculate:Failed", e.stackTraceToString())
                }
            }
        }

        fio2.addSource(airwayPressure, ventilatorSettingObserver)
        fio2.addSource(airFlow, ventilatorSettingObserver)
        fio2.addSource(o2Flow, ventilatorSettingObserver)
        estimatedPeep.addSource(airwayPressure, ventilatorSettingObserver)
        estimatedPeep.addSource(airFlow, ventilatorSettingObserver)
        estimatedPeep.addSource(o2Flow, ventilatorSettingObserver)
        fio2WithUnit.addSource(airwayPressure, ventilatorSettingObserver)
        fio2WithUnit.addSource(airFlow, ventilatorSettingObserver)
        fio2WithUnit.addSource(o2Flow, ventilatorSettingObserver)
        estimatedPeepWithUnit.addSource(airwayPressure, ventilatorSettingObserver)
        estimatedPeepWithUnit.addSource(airFlow, ventilatorSettingObserver)
        estimatedPeepWithUnit.addSource(o2Flow, ventilatorSettingObserver)

    }

    fun onClickSoundMeasurementButton() {
        buildVentilatorValue()
        transitionToSoundMeasurement.value = Event("transitionToSoundMeasurement")
    }

    fun onClickManualMeasurementButton() {
        buildVentilatorValue()
        transitionToManualMeasurement.value = Event("transitionToManualMeasurement")
    }

    private fun buildVentilatorValue() {
        ventilatorValue.airwayPressure = airwayPressure.value
        ventilatorValue.o2Flow = o2Flow.value
        ventilatorValue.airFlow = airFlow.value
        ventilatorValue.fio2 = fio2.value
        ventilatorValue.estimatedPeep = estimatedPeep.value
        ventilatorValue.predictedVt = predictedVt.value
    }

}