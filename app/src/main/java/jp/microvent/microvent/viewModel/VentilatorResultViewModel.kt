package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.CreatePatientForm
import jp.microvent.microvent.service.model.CreatedPatient
import jp.microvent.microvent.service.model.Patient
import jp.microvent.microvent.service.model.VentilatorValue
import jp.microvent.microvent.view.ui.VentilatorSettingFragmentArgs
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class VentilatorResultViewModel(
    private val myApplication: Application,
    private val ventilatorValue: VentilatorValue
) : BaseViewModel(myApplication) {

    class Factory(
        private val application: Application, private val ventilatorValue: VentilatorValue
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VentilatorResultViewModel(application, ventilatorValue) as T
        }
    }

    val transitionToVentilatorSetting: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val patient: Patient = Patient()

    /**
     * 登録きき観察研究データ表示用
     */
    val ventilatorValueData: MutableLiveData<VentilatorValue> = MutableLiveData(ventilatorValue)

    fun onClickButton() {
        //TODO("patientに情報を詰めること")
        transitionToVentilatorSetting.value = Event("transitionToVentilatorSetting")
    }

}