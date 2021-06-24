package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.model.VentilatorValueListElm
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception


class MeasurementDataListViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val transitionToMeasurementDataDetail: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val ventilatorValueList: MutableLiveData<List<VentilatorValueListElm>> by lazy {
        MutableLiveData()
    }

    val ventilatorValueId: MutableLiveData<Int> by lazy {
        MutableLiveData()
    }

    init {
        viewModelScope.launch {
            try {
                repository.getVentilatorValueList(ventilatorId, null, null, 1, appkey).let { res ->
                    if(res.isSuccessful) {
                        res.body()?.result?.let {
                            ventilatorValueList.value = it
                        }
                    }else{
                        errorHandling(res)
                    }
                }
            } catch (e: Exception) {
                showDialogConnectionError.value = Event("connection_error")
            }
        }
    }

    fun onClickItem(ventilatorValueListElm: VentilatorValueListElm){
        ventilatorValueId.value = ventilatorValueListElm.id
        transitionToMeasurementDataDetail.value = Event("transitionToMeasurementDataDetail")
    }
}