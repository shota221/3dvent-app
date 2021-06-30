package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception


class MeasurementDataViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val transitionToPrevMeasurementList: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val transitionToLatestMeasurementData: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val ventilatorValueId: MutableLiveData<Int> by lazy {
        MutableLiveData()
    }

    fun onClickPrevMeasurementListButton() {
        transitionToPrevMeasurementList.value = Event("transitionToPrevMeasurementList")
    }

    fun onClickLatestMeasurementDataButton() {
        viewModelScope.launch {
            setProgressBar.value = Event(true)
            try {
                repository.getVentilatorValueList(ventilatorId, 1, 0, null, appkey).let { res ->
                    if (res.isSuccessful) {
                        res.body()?.result?.let {
                            if(it.isEmpty()) {
                                showToast.value = Event(context.getString(R.string.measurement_data_not_found))
                            } else {
                                ventilatorValueId.value = it.first().id
                                transitionToLatestMeasurementData.value =
                                    Event("transitionToLatestMeasurementData")
                            }
                        }
                    } else {
                        errorHandling(res)
                    }
                }
            } catch (e: Exception) {
                showDialogConnectionError.value = Event("connect_error")
            }
            setProgressBar.value = Event(false)
        }

    }


}