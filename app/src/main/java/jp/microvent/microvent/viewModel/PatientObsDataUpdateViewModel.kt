package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception


class PatientObsDataUpdateViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {


    val transitionToPatientObsDataDetail:MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val age:MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    fun onClickSavePatientObsDataButton() {
        transitionToPatientObsDataDetail.value = Event("transitionToPatientObsDataDetail")
    }
}