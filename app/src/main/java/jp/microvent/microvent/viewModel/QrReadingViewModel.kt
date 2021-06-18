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

    val transitionToAuth: MediatorLiveData<Event<String>> by lazy {
        MediatorLiveData()
    }

    val transitionToPatientSetting: MediatorLiveData<Event<String>> by lazy {
        MediatorLiveData()
    }

    val transitionToVentilatorSetting: MediatorLiveData<Event<String>> by lazy {
        MediatorLiveData()
    }

    init {
        val gs1CodeObserver = Observer<String> {
            //GS1コードが読み込まれたタイミングで紐付けられていた呼吸器情報をリセット
            resetCurrentVentilatorPref()
            //新たに読み込まれたgs1codeを書き込む
            with(currentVentilatorPref.edit()) {
                putString(
                    "gs1Code",
                    it
                )

                commit()
            }

            if (!loggedIn()) {
                //未ログインユーザはログイン画面へ
                transitionToAuth.value = Event("transitionToAuth")
            } else {
                viewModelScope.launch {
                    try {
                        val getVentilator = repository.getVentilator(it, appkey, userToken)
                        if (getVentilator.isSuccessful) {
                            getVentilator.body()?.result?.let {
                                //単位更新
                                putUnits(it.units)

                                if (it.ventilatorId == null || it.patientId == null) {
                                    //未登録の呼吸器 or 登録済みかつ患者と紐付いていない呼吸器のgs1コードを読み込んだ場合PatientSetting画面へ
                                    if (it.ventilatorId != null) {
                                        //登録済みである場合はそのventilatorIdを控える
                                        with(currentVentilatorPref.edit()) {
                                            putInt(
                                                context.getString(R.string.current_ventilator_id),
                                                it.ventilatorId
                                            )
                                            commit()
                                        }
                                        if (it.organizationCode == null) {
                                            //組織登録がなされていない場合は呼吸器情報の更新を行う
                                            val updateVentilatorForm =
                                                UpdateVentilatorForm(it.startUsingAt)
                                            val updateVentilator = repository.updateVentilator(
                                                it.ventilatorId,
                                                updateVentilatorForm,
                                                appkey,
                                                userToken
                                            )
                                            if (!updateVentilator.isSuccessful) {
                                                errorHandling(updateVentilator)
                                            }
                                        }


                                    } else {
                                        //未登録の場合は呼吸器登録
                                        val createVentilatorForm =
                                            CreateVentilatorForm(latestGs1Code, latitude, longitude)
                                        val createVentilator =
                                            repository.createVentilator(
                                                createVentilatorForm,
                                                appkey,
                                                userToken
                                            )
                                        if (createVentilator.isSuccessful) {
                                            createVentilator.body()?.result?.let {
                                                with(currentVentilatorPref.edit()) {

                                                    putInt(
                                                        context.getString(R.string.current_ventilator_id),
                                                        it.ventilatorId
                                                    )
                                                    commit()
                                                }
                                            }
                                        } else {
                                            errorHandling(createVentilator)
                                        }
                                    }

                                    transitionToPatientSetting.value =
                                        Event("transitionToPatientSetting")


                                } else {
                                    //ログイン済みで、登録済みかつ患者と紐付いている呼吸器のgs1コードを読み込んだ場合VentilatorSetting画面へ
                                    //画面遷移前に紐付いているvenitlatorId及びpatientdIdを保存
                                    with(currentVentilatorPref.edit()) {
                                        putInt(
                                            context.getString(R.string.current_ventilator_id),
                                            it.ventilatorId
                                        )
                                        putInt(
                                            context.getString(R.string.current_patient_id),
                                            it.patientId
                                        )
                                        commit()
                                    }
                                    if (it.organizationCode == null) {
                                        //組織登録がなされていない場合は呼吸器情報の更新を行う
                                        val updateVentilatorForm =
                                            UpdateVentilatorForm(it.startUsingAt)
                                        val updateVentilator = repository.updateVentilator(
                                            it.ventilatorId,
                                            updateVentilatorForm,
                                            appkey,
                                            userToken
                                        )
                                        if (!updateVentilator.isSuccessful) {
                                            errorHandling(updateVentilator)
                                        }
                                    }

                                    transitionToVentilatorSetting.value =
                                        Event("transitionToVentilatorSetting")
                                }


                            }
                        } else {
                            errorHandling(getVentilator)
                        }
                    } catch (e: ConnectException) {
                        showDialogConnectionError.value = Event("connection_error")
                    }
                }

            }
        }//define Observer

        transitionToAuth.addSource(gs1Code, gs1CodeObserver)
    }

    fun test() {
        gs1Code.postValue(gs1CodeForTest.value)
    }


    //エラー処理 TODO:バリデエラー時の処理詳細
    private fun <T : Any?> errorHandling(errorResponse: Response<T>) {
        when (errorResponse.code()) {
            400 -> {
                showToast.value = Event(context.getString(R.string.validation_error_toast))
            }

            401 -> {
                resetUserToken()
                transitionToAuth.value = Event("transitionToAuth")
            }

            500 -> {
                showToast.value = Event(context.getString(R.string.server_error_toast))
            }
        }
    }
}