package jp.microvent.microvent.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.location.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.CreateUserTokenForm
import jp.microvent.microvent.service.model.CreateVentilatorForm
import jp.microvent.microvent.service.model.UpdateVentilatorForm
import jp.microvent.microvent.view.permission.AccessLocationPermission
import jp.microvent.microvent.view.ui.AuthFragment
import jp.microvent.microvent.view.ui.dialog.DialogConfirmLogoutOnAnotherTerminalFragment
import jp.microvent.microvent.viewModel.util.Event
import jp.microvent.microvent.viewModel.util.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.lang.Exception
import java.net.ConnectException

class AuthViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    val isButtonEnabled: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData()
    }

    //btNotLoginやbtLoginを有効にするためのトリガー
    val isCheckedTermsOfUse: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val organizationName: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val accountName: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val password: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val transitionToQrReading: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val transitionToPatientSetting: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val transitionToVentilatorSetting: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val showDialogConfirmLogoutOnAnotherTerminal: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    val location by lazy {
        Location(context)
    }

    init {
        setOrganizationName()

        val checkBoxObserver = Observer<Boolean> {
            isButtonEnabled.value = isCheckedTermsOfUse.value
        }

        isButtonEnabled.addSource(isCheckedTermsOfUse, checkBoxObserver)
    }

    /**
     * 画面遷移先制御
     */
    private fun transit() {
        if (sharedCurrentVentilator.gs1Code.isNullOrEmpty()) {
            //qrコードが読み込まれていない場合
            transitionToQrReading.value = Event("transitionToQrReading")
        } else if (sharedCurrentVentilator.patientId != null) {
            //患者登録済みの場合
            transitionToVentilatorSetting.value = Event("transitionToVentilatorSetting")
        } else if (sharedCurrentVentilator.ventilatorId != null) {
            //患者未登録の場合
            transitionToPatientSetting.value = Event("transitionToPatientSetting")
        }
    }

    /**
     * 画面上部、読み込んだ呼吸器の組織名を表示
     */
    private fun setOrganizationName() {
        viewModelScope.launch {
            try {
                val getVentilatorNoAuth = repository.getVentilatorNoAuth(sharedCurrentVentilator.gs1Code, sharedAccessToken.appkey)
                if (getVentilatorNoAuth.isSuccessful) {
                    getVentilatorNoAuth.body()?.result?.let {
                        sharedUnits.putUnits(it.units)
                        organizationName.postValue(it.organizationName)
                    }
                }
            } catch (e: ConnectException) {
                handleConnectionError()
            }
        }
    }

    fun onClickNotLoginButton() {
        viewModelScope.launch {
            setProgressBar.value = Event(true)
            try {
                saveVentilatorId()
                transit()
            } catch (e: ConnectException) {
                handleConnectionError()
            }
            setProgressBar.value = Event(false)
        }
    }

    fun onClickLoginButton() {
        viewModelScope.launch {
            setProgressBar.value = Event(true)
            try {

                val accountName = accountName.value.toString()
                val checkUserToken = repository.checkUserToken(accountName, sharedAccessToken.appkey)
                if (checkUserToken.isSuccessful) {
                    val hasToken: Boolean = checkUserToken.body()?.result?.hasToken ?: false
                    if (hasToken) {
                        showDialogConfirmLogoutOnAnotherTerminal.value = Event("confirm")

                    }

                    //ログイン(=トークン発行)処理
                    val password = password.value.toString()
                    var createUserTokenForm: CreateUserTokenForm =
                        CreateUserTokenForm(accountName, password)
                    val createUserToken =
                        repository.createUserToken(createUserTokenForm, sharedAccessToken.appkey)
                    if (createUserToken.isSuccessful) {
                        val userTokenResult = createUserToken.body()?.result
                        sharedAccessToken.userToken = userTokenResult?.apiToken.toString()


                        //次にGs1Codeが読まれるまでは直近に読んだventilatorIdを参照する…sharedPrefに保存
                        saveVentilatorId()
                        transit()

                    } else {
                        handleErrorResponse(createUserToken)
                    }

                } else {
                    handleErrorResponse(checkUserToken)
                }

            } catch (e: ConnectException) {
                handleConnectionError()
            }
            setProgressBar.value = Event(false)
        }
    }


    /**
     * latestGs1Codeが登録済みか、登録済みであればログイン中のユーザの組織との齟齬を判定、そうでなければ登録してventilatorIdを
     * 読み込んだventilatorIdは即座にsharedPrefに保存される
     */
    private suspend fun saveVentilatorId() {
        if (loggedIn()) {
            val getVentilator = repository.getVentilator(sharedCurrentVentilator.gs1Code, sharedAccessToken.appkey, sharedAccessToken.userToken)
            if (getVentilator.isSuccessful) {
                val getVentilatorResult = getVentilator.body()?.result

                val isRegistered = getVentilatorResult?.isRegistered ?: false
                //登録済みであるかどうか
                if (isRegistered) {
                    val ventilatorId = getVentilatorResult?.ventilatorId

                    sharedCurrentVentilator.ventilatorId = ventilatorId

                    //紐付いているpatientIdがあれば保存
                    getVentilatorResult?.patientId?.let {
                        sharedCurrentVentilator.patientId = it
                    }

                    if (getVentilatorResult?.organizationCode.isNullOrEmpty()) {
                        //組織登録がなされていなければユーザトークンをもとに組織登録を行う
                        val updateVentilatorForm =
                            UpdateVentilatorForm(getVentilatorResult?.startUsingAt)
                        val updateVentilator = repository.updateVentilator(
                            ventilatorId,
                            updateVentilatorForm,
                            sharedAccessToken.appkey,
                            sharedAccessToken.userToken
                        )
                    }


                } else {
                    val createVentilatorForm =
                        CreateVentilatorForm(sharedCurrentVentilator.gs1Code, location.latitude, location.longitude)
                    val createVentilator =
                        repository.createVentilator(createVentilatorForm, sharedAccessToken.appkey, sharedAccessToken.userToken)
                    if (createVentilator.isSuccessful) {
                        val ventilatorId: Int? = createVentilator.body()?.result?.ventilatorId
                        if (ventilatorId != null) {
                            sharedCurrentVentilator.ventilatorId = ventilatorId
                        }
                    }
                }//登録済みでない場合
            } else {
                handleErrorResponse(getVentilator)
            }//ログインする場合

        } else {
            val getVentilatorNoAuth = repository.getVentilatorNoAuth(sharedCurrentVentilator.gs1Code, sharedAccessToken.appkey)
            if (getVentilatorNoAuth.isSuccessful) {
                val getVentilatorNoAuthResult = getVentilatorNoAuth.body()?.result

                val isRegistered = getVentilatorNoAuthResult?.isRegistered ?: false

                //登録済みであるかどうか
                if (isRegistered) {
                    getVentilatorNoAuthResult?.ventilatorId?.let {
                        sharedCurrentVentilator.ventilatorId = it
                    }

                    //紐付いているpatientIdがあれば保存
                    getVentilatorNoAuthResult?.patientId?.let {
                        sharedCurrentVentilator.patientId = it
                    }
                } else {
                    val createVentilatorForm =
                        CreateVentilatorForm(sharedCurrentVentilator.gs1Code, location.latitude, location.longitude)
                    val createVentilatorNoAuth =
                        repository.createVentilatorNoAuth(createVentilatorForm, sharedAccessToken.appkey)
                    if (createVentilatorNoAuth.isSuccessful) {
                        val ventilatorId: Int? =
                            createVentilatorNoAuth.body()?.result?.ventilatorId
                        if (ventilatorId != null) {
                            sharedCurrentVentilator.ventilatorId = ventilatorId
                        }
                    }
                }//登録済みでない場合
            } else {
                handleErrorResponse(getVentilatorNoAuth)
            }

        }//ログインしない場合
    }

    fun setLocation() {
        location.setLocation()
    }
}