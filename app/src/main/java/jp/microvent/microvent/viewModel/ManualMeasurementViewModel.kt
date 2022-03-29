package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.*
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException

class ManualMeasurementViewModel(
    myApplication: Application,
    ventilatorValue: VentilatorValue
) : IeMeasurementViewModel(myApplication,ventilatorValue) {

    class Factory(
        private val application: Application, private val ventilatorValue: VentilatorValue
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ManualMeasurementViewModel(application, ventilatorValue) as T
        }
    }

    val respirationsPer10secFirst: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val respirationsPer10secSecond: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val exhalationFirst: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val exhalationSecond: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val exhalationFirstLabel: MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val exhalationSecondLabel: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    init {
        sharedUnits.setUnit(exhalationFirstLabel,context.getString(R.string.exhalation_first),context.getString(R.string.e_pref_key))
        sharedUnits.setUnit(exhalationSecondLabel,context.getString(R.string.exhalation_second),context.getString(R.string.e_pref_key))
    }


    override fun onClickCalculateAverageButton() {
        viewModelScope.launch {
            setProgressBar.value = Event(true)
            try {
                val ieFirst =
                    IeManualFetchFormDataListElm(exhalationFirst.value, respirationsPer10secFirst.value)
                val ieSecond =
                    IeManualFetchFormDataListElm(exhalationSecond.value, respirationsPer10secSecond.value)
                val ieManualFetchFormDataList: MutableList<IeManualFetchFormDataListElm> =
                    mutableListOf(ieFirst, ieSecond)
                val ieManualFetchFrom = IeManualFetchForm(ieManualFetchFormDataList)
                val calcIeManual = repository.calcIeManual(ieManualFetchFrom, sharedAccessToken.appkey)
                if (calcIeManual.isSuccessful) {
                    calcIeManual.body()?.result?.let{
                        averageInhalationTime.postValue(it.iAvg)
                        sharedUnits.setUnit(averageInhalationTimeWithUnit, it.iAvg, context.getString(R.string.i_avg_pref_key))
                        averageExhalationTime.postValue(it.eAvg)
                        sharedUnits.setUnit(averageExhalationTimeWithUnit, it.eAvg, context.getString(R.string.e_avg_pref_key))
                        rr.postValue(it.rr)
                        ieRatio.postValue(it.ieRatio)
                    }
                }else {
                    handleErrorResponse(calcIeManual)
                }
            } catch (e: ConnectException) {
                handleConnectionError()
            }
            setProgressBar.value = Event(false)
        }
    }
}