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
                        res.body()?.result?.let {
                            patient.postValue(it)
                            genderStr.postValue(Gender.buildGender(it.gender)?.getString(context))
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
                    val calcEstimatedData =
                        repository.calcEstimatedData(airwayPressure, airFlow, o2Flow, appkey)
                    if (calcEstimatedData.isSuccessful) {
                        val calcEstimatedDataResult = calcEstimatedData.body()?.result
                        if (calcEstimatedDataResult != null) {
                            fio2.value = calcEstimatedDataResult.fio2
                            estimatedPeep.value = calcEstimatedDataResult.estimatedPeep
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
        ventilatorValue.predictedVt = patient.value?.predictedVt
    }

}