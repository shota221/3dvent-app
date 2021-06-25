package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.service.model.*
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException


class PatientObsDataUpdateViewModel(
    private val myApplication: Application,
    val patientObs: PatientObs
) : BaseViewModel(myApplication) {

    class Factory(
        private val application: Application, val patientObs: PatientObs
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PatientObsDataUpdateViewModel(application, patientObs) as T
        }
    }

    val hasObserved: Boolean = patientObs.hasObserved

    val optOutFlg: MutableLiveData<Int> = MutableLiveData(patientObs.optOutFlg)

    val usedPlace: MutableLiveData<Int> = MutableLiveData(patientObs.usedPlace)

    val treatment: MutableLiveData<Int> = MutableLiveData(patientObs.treatment)

    val outcome: MutableLiveData<Int> = MutableLiveData(patientObs.outcome)

    val adverseEventFlg: MutableLiveData<Int> = MutableLiveData(patientObs.adverseEventFlg)

    val age: MutableLiveData<String> = MutableLiveData(patientObs.age)

    val ventDiseaseName: MutableLiveData<String> = MutableLiveData(patientObs.ventDiseaseName)

    val otherDiseaseName1: MutableLiveData<String> = MutableLiveData(patientObs.otherDiseaseName1)

    val otherDiseaseName2: MutableLiveData<String> = MutableLiveData(patientObs.otherDiseaseName2)

    val hospital: MutableLiveData<String> = MutableLiveData(patientObs.hospital)

    val national: MutableLiveData<String> = MutableLiveData(patientObs.national)

    val discontinuationAt: MutableLiveData<String> = MutableLiveData(patientObs.discontinuationAt)

    val adverseEventContents: MutableLiveData<String> =
        MutableLiveData(patientObs.adverseEventContents)

    val transitionToPatientObsDataDetail: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    fun onOptOutFlgSelected(itemSelected: Int) {
            optOutFlg.postValue(itemSelected)
    }

    fun onUsedPlaceSelected(itemSelected: Int) {
        if (itemSelected != 0) {
            usedPlace.postValue(itemSelected)
        }
    }

    fun onTreatmentSelected(itemSelected: Int) {
        if (itemSelected != 0) {
            treatment.postValue(itemSelected)
        }
    }

    fun onOutcomeSelected(itemSelected: Int) {
        if (itemSelected != 0) {
            outcome.postValue(itemSelected)
        }
    }

    fun onAdverseEventFlgSelected(itemSelected: Int) {
            adverseEventFlg.postValue(itemSelected)
    }

    fun onClickSavePatientObsDataButton() {
        viewModelScope.launch{
            try{
                if(!hasObserved){
                    repository.createPatientObs(patientId, buildCreatePatientObsForm(), appkey, userToken).let { res ->
                        if(res.isSuccessful){
                            showToastCreated()
                            transitionToPatientObsDataDetail.value = Event("transitionToPatientObsDataDetail")
                        } else {
                            errorHandling(res)
                        }
                    }
                }else{
                    repository.updatePatientObs(patientId, buildUpdatePatientObsForm(), appkey, userToken).let { res ->
                        if(res.isSuccessful){
                            showToastUpdated()
                            transitionToPatientObsDataDetail.value = Event("transitionToPatientObsDataDetail")
                        } else {
                            errorHandling(res)
                        }
                    }
                }
            }catch (e:ConnectException){
                showDialogConnectionError.value = Event("connection_error")
            }
        }
    }

    private fun buildCreatePatientObsForm(): CreatePatientObsForm = CreatePatientObsForm(
        optOutFlg.value,
        age.value,
        ventDiseaseName.value,
        otherDiseaseName1.value,
        otherDiseaseName2.value,
        usedPlace.value,
        hospital.value,
        national.value,
        discontinuationAt.value,
        outcome.value,
        treatment.value,
        adverseEventFlg.value,
        adverseEventContents.value
    )

    private fun buildUpdatePatientObsForm(): UpdatePatientObsForm = UpdatePatientObsForm(
        optOutFlg.value,
        age.value,
        ventDiseaseName.value,
        otherDiseaseName1.value,
        otherDiseaseName2.value,
        usedPlace.value,
        hospital.value,
        national.value,
        discontinuationAt.value,
        outcome.value,
        treatment.value,
        adverseEventFlg.value,
        adverseEventContents.value
    )
}