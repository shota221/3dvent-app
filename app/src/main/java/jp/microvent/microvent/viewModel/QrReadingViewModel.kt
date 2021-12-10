package jp.microvent.microvent.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.CreateUserTokenForm
import jp.microvent.microvent.service.model.CreateVentilatorForm
import jp.microvent.microvent.service.model.UpdateVentilatorForm
import jp.microvent.microvent.view.permission.AccessLocationPermission
import jp.microvent.microvent.view.permission.CameraPermission
import jp.microvent.microvent.viewModel.util.Event
import jp.microvent.microvent.viewModel.util.Location
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.lang.Exception
import java.net.ConnectException
import javax.security.auth.callback.Callback

class QrReadingViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val gs1Code: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val gs1CodeForTest: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    /**
     * gs1CodeをObserveするLiveData
     */
    val transitionToPatientSetting: MediatorLiveData<Event<String>> by lazy {
        MediatorLiveData()
    }

    /**
     * gs1CodeをObserveするLiveData
     */
    val transitionToVentilatorSetting: MediatorLiveData<Event<String>> by lazy {
        MediatorLiveData()
    }

    val location by lazy {
        Location(context)
    }

    init {
        val gs1CodeObserver = Observer<String> {
            //GS1コードが読み込まれたタイミングで紐付けられていた呼吸器情報をリセット
            sharedCurrentVentilator.resetAll()
            sharedCurrentVentilator.gs1Code = it
            //新たに読み込まれたgs1codeを書き込む

            if (!loggedIn()) {
                //未ログインユーザはログイン画面へ
                transitionToAuth.value = Event("transitionToAuth")
            } else {
                viewModelScope.launch {
                    try {
                        val getVentilator = repository.getVentilator(
                            it,
                            sharedAccessToken.appkey,
                            sharedAccessToken.userToken
                        )
                        if (getVentilator.isSuccessful) {
                            getVentilator.body()?.result?.let {
                                //単位更新
                                sharedUnits.putUnits(it.units)

                                if (it.ventilatorId == null || it.patientId == null) {
                                    //未登録の呼吸器 or 登録済みかつ患者と紐付いていない呼吸器のgs1コードを読み込んだ場合PatientSetting画面へ
                                    if (it.ventilatorId != null) {
                                        //登録済みである場合はそのventilatorIdを控える
                                        sharedCurrentVentilator.ventilatorId = it.ventilatorId
                                        if (it.organizationCode == null) {
                                            //組織登録がなされていない場合は呼吸器情報の更新を行う
                                            val updateVentilatorForm =
                                                UpdateVentilatorForm(it.startUsingAt)
                                            val updateVentilator = repository.updateVentilator(
                                                it.ventilatorId,
                                                updateVentilatorForm,
                                                sharedAccessToken.appkey,
                                                sharedAccessToken.userToken
                                            )
                                            if (!updateVentilator.isSuccessful) {
                                                handleErrorResponse(updateVentilator)
                                            }
                                        }


                                    } else {
                                        //未登録の場合は呼吸器登録
                                        val createVentilatorForm =
                                            CreateVentilatorForm(
                                                sharedCurrentVentilator.gs1Code,
                                                location.latitude,
                                                location.longitude
                                            )
                                        val createVentilator =
                                            repository.createVentilator(
                                                createVentilatorForm,
                                                sharedAccessToken.appkey,
                                                sharedAccessToken.userToken
                                            )
                                        if (createVentilator.isSuccessful) {
                                            createVentilator.body()?.result?.let { createdVentilator ->
                                                sharedCurrentVentilator.ventilatorId =
                                                    createdVentilator.ventilatorId
                                            }
                                        } else {
                                            handleErrorResponse(createVentilator)
                                        }
                                    }

                                    transitionToPatientSetting.value =
                                        Event("transitionToPatientSetting")


                                } else {
                                    //ログイン済みで、登録済みかつ患者と紐付いている呼吸器のgs1コードを読み込んだ場合VentilatorSetting画面へ
                                    //画面遷移前に紐付いているvenitlatorId及びpatientdIdを保存
                                    sharedCurrentVentilator.ventilatorId = it.ventilatorId
                                    sharedCurrentVentilator.patientId = it.patientId
                                    if (it.organizationCode == null) {
                                        //組織登録がなされていない場合は呼吸器情報の更新を行う
                                        val updateVentilatorForm =
                                            UpdateVentilatorForm(it.startUsingAt)
                                        val updateVentilator = repository.updateVentilator(
                                            it.ventilatorId,
                                            updateVentilatorForm,
                                            sharedAccessToken.appkey,
                                            sharedAccessToken.userToken
                                        )
                                        if (!updateVentilator.isSuccessful) {
                                            handleErrorResponse(updateVentilator)
                                        }
                                    }

                                    transitionToVentilatorSetting.value =
                                        Event("transitionToVentilatorSetting")
                                }


                            }
                        } else {
                            handleErrorResponse(getVentilator)
                        }
                    } catch (e: ConnectException) {
                        handleConnectionError()
                    }
                }

            }
        }//define Observer

        transitionToAuth.addSource(gs1Code, gs1CodeObserver)
    }

    fun test() {
        val testCode = "0145715100100194172402121021021221" + gs1CodeForTest.value
        gs1Code.postValue(testCode)
    }

    fun setLocation() {
        location.setLocation()
    }
}