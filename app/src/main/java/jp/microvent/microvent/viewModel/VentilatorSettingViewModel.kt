package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.*
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class VentilatorSettingViewModel(
    private val myApplication: Application,
) : BaseViewModel(myApplication) {

    val patient:MutableLiveData<Patient> by lazy {
        MutableLiveData()
    }

    val genderStr:MutableLiveData<String> by lazy {
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

    val ventilatorValue: VentilatorValue by lazy{
        VentilatorValue()
    }

    init{
        /**
         * 現在観察中のpatientIdから患者情報を取得
         */
        viewModelScope.launch{
            try{
                repository.getPatient(patientId,appkey).let{
                    if(it.isSuccessful) {
                        it.body()?.result?.let {
                            patient.postValue(it)
                            genderStr.postValue(Gender.buildGender(it.gender)?.getString(context))
                        }
                    }else{
                        Log.e("rendering:Failed", it.errorBody().toString())
                    }
                }
            }catch (e:Exception){
                Log.e("rendering:Failed", e.stackTraceToString())
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
                try{
                    val calcEstimatedData = repository.calcEstimatedData(airwayPressure,airFlow,o2Flow,appkey)
                    if(calcEstimatedData.isSuccessful) {
                        val calcEstimatedDataResult = calcEstimatedData.body()?.result
                        if(calcEstimatedDataResult != null){
                            fio2.value = calcEstimatedDataResult.fio2
                            estimatedPeep.value = calcEstimatedDataResult.estimatedPeep
                        }
                    }
                } catch (e:Exception){
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

    fun onClickSoundMeasurementButton(){
        buildVentilatorValue()
        transitionToSoundMeasurement.value = Event("transitionToSoundMeasurement")
    }

    fun onClickManualMeasurementButton(){
        buildVentilatorValue()
        transitionToManualMeasurement.value = Event("transitionToManualMeasurement")
    }

    private fun buildVentilatorValue(){
        ventilatorValue.airwayPressure = airwayPressure.value
        ventilatorValue.o2Flow = o2Flow.value
        ventilatorValue.airFlow = airFlow.value
        ventilatorValue.fio2 = fio2.value
        ventilatorValue.estimatedPeep = estimatedPeep.value
        ventilatorValue.predictedVt = patient.value?.predictedVt
    }

}