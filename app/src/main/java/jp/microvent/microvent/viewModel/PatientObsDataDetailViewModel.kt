package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.service.enum.*
import jp.microvent.microvent.service.model.PatientObs
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception


class PatientObsDataDetailViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val transitionToPatientObsDataUpdate:MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val patientObs: MutableLiveData<PatientObs> by lazy {
        MutableLiveData()
    }

    val optOutFlgStr : MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val usedPlaceStr : MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val treatmentStr : MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val outcomeStr : MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val adverseEventFlgStr : MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    fun onClickEditPatientObsDataButton() {
        transitionToPatientObsDataUpdate.value = Event("transitionToPatientObsDataUpdate")
    }

    init {
        viewModelScope.launch {
            try {
                repository.getPatientObs(patientId, appkey,userToken).let { res ->
                    if (res.isSuccessful) {
                        res.body()?.result?.let {
                            patientObs.postValue(it)
                            optOutFlgStr.postValue(OptOutFlg.buildOptOutFlg(it.optOutFlg)?.getString(context))
                            usedPlaceStr.postValue(UsedPlace.buildUsedPlace(it.usedPlace)?.getString(context))
                            treatmentStr.postValue(Treatment.buildTreatment(it.treatment)?.getString(context))
                            outcomeStr.postValue(Outcome.buildOutcome(it.outcome)?.getString(context))
                            adverseEventFlgStr.postValue(AdverseEventFlg.buildAdverseEventFlg(it.adverseEventFlg)?.getString(context))
                        }
                    } else {
                        errorHandling(res)
                    }
                }
            } catch (e: Exception) {
                showDialogConnectionError.value = Event("connect_error")
            }
        }
    }
}