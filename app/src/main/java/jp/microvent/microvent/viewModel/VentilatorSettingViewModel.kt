package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.CreatePatientForm
import jp.microvent.microvent.service.model.CreatedPatient
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class VentilatorSettingViewModel(
    private val myApplication: Application,
    private val height: String,
    private val gender: String,
    private val predictedVt: String,
) : BaseViewModel(myApplication) {

    class Factory(
        private val application: Application, private val height: String, private val gender: String, private val predictedVt: String,
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VentilatorSettingViewModel(application, height, gender, predictedVt) as T
        }
    }

    val transitionToSoundMeasurement: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val transitionToManualMeasurement: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val patientHeight: MutableLiveData<String> by lazy{
        MutableLiveData()
    }

    val patientGender: MutableLiveData<String> by lazy{
        MutableLiveData()
    }

    val patientPredictedVt: MutableLiveData<String> by lazy{
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

    init{
        setPatientValue()

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
                            estimatedPeep.value = calcEstimatedDataResult.estimated_peep
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

    /**
     * 画面遷移時患者情報表示用
     */
    private fun setPatientValue(){
        patientHeight.postValue(height)
        patientGender.postValue(gender)
        patientPredictedVt.postValue(predictedVt)
    }

    public fun onClickSoundMeasurementButton(){
        transitionToSoundMeasurement.value = Event("transitionToSoundMeasurement")
    }

    public fun onClickManualMeasurementButton(){
        transitionToManualMeasurement.value = Event("transitionToManualMeasurement")
    }

}