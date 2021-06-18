package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.*
import kotlinx.coroutines.launch
import java.lang.Exception

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

    override fun onClickCalculateAverageButton() {
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
                    calcIeManual.body()?.result?.let{
                        averageInhalationTime.postValue(it.iAvg)
                        averageExhalationTime.postValue(it.eAvg)
                        rr.postValue(it.rr)
                    }
                }else {
                    Log.i("calcIe", calcIeManual.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e("calculate:Failed", e.stackTrace.toString())
            }
        }
    }
}