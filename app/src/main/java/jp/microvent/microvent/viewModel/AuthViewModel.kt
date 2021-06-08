package jp.microvent.microvent.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.CreateUserTokenForm
import jp.microvent.microvent.service.model.CreateVentilatorForm
import jp.microvent.microvent.service.model.UpdateVentilatorForm
import jp.microvent.microvent.view.ui.AuthFragmentArgs
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthViewModel(
    private val myApplication: Application,
    private val gs1Code:String
) : BaseViewModel(myApplication) {

    class Factory(private val application: Application, private val gs1Code: String) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(application, gs1Code) as T
        }
    }

    val isButtonEnabled: MediatorLiveData<Boolean> by lazy  {
        MediatorLiveData()
    }

    //btNotLoginやbtLoginを有効にするためのトリガー
    val isCheckedTermsOfUse: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val organizationName: MutableLiveData<String> by lazy {
        MutableLiveData(context.getString(R.string.unregistered))
    }

    val accountName: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val password: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val transitionToPatientSetting: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    init{
        setOrganizationName()

        val checkBoxObserver = Observer<Boolean> {
            isButtonEnabled.value = isCheckedTermsOfUse.value
        }

        isButtonEnabled.addSource(isCheckedTermsOfUse, checkBoxObserver)
    }

    private fun setOrganizationName() {
        viewModelScope.launch {
            try {

                val getVentilatorNoAuth = repository.getVentilatorNoAuth(gs1Code, appkey)
                if (getVentilatorNoAuth.isSuccessful) {
                    val getVentilatorNoAuthResult = getVentilatorNoAuth.body()?.result
                    if (getVentilatorNoAuthResult?.organization_name != null) {
                        organizationName.postValue(getVentilatorNoAuthResult.organization_name)
                    }
                }

            } catch (e: Exception) {
                Log.e("NotLogin:Failed", e.stackTraceToString())
            }
        }
    }

    fun onClickNotLoginButton() {
        viewModelScope.launch {
            try {
                saveVentilatorId()
                transitionToPatientSetting.value = Event("transitionToPatientSetting")
            } catch (e: Exception) {
                Log.e("NotLogin:Failed", e.stackTraceToString())
            }
        }

    }


    //TODO("異常系の実装")
    fun onClickLoginButton() {

        viewModelScope.launch {
            try {
                val accountName = accountName.value.toString()
                val checkUserToken = repository.checkUserToken(accountName, appkey)
                if (checkUserToken.isSuccessful) {
                    val hasToken: Boolean = checkUserToken.body()?.result?.has_token ?: false
                    if (hasToken) {
                        //TODO("トークンを所持しているのであれば別端末でのログインが続行中であるため、確認のダイアログを表示する")
                    }

                    //ログイン(=トークン発行)処理
                    val password = password.value.toString()
                    var createUserTokenForm: CreateUserTokenForm =
                        CreateUserTokenForm(accountName, password)
                    val createUserToken = repository.createUserToken(createUserTokenForm, appkey)
                    if (createUserToken.isSuccessful) {
                        val userTokenResult = createUserToken.body()?.result
                        with(networkPref.edit()) {
                            putString(
                                context.getString(R.string.user_token),
                                userTokenResult?.api_token.toString()
                            )
                            commit()
                        }

                    }

                    //次にgs1Codeが読まれるまでは直近に読んだventilator_idを参照する…sharedPrefに保存
                    saveVentilatorId()

                    transitionToPatientSetting.value = Event("transitionToPatientSetting")
                } else {
                    //TODO("バリデ処理")
                }

            } catch (e: Exception) {
                Log.e("Login:Failed", e.stackTraceToString())
            }
        }
    }


    /**
     * gs1Codeが登録済みか、登録済みであればログイン中のユーザの組織との齟齬を判定、そうでなければ登録してventilatorIdを
     * 読み込んだventilatorIdは即座にsharedPrefに保存される
     */
    private suspend fun saveVentilatorId() {
        //TODO("バンドルからgs1Code")

        if (userToken.isEmpty()) {
            val getVentilatorNoAuth = repository.getVentilatorNoAuth(gs1Code, appkey)
            if (getVentilatorNoAuth.isSuccessful) {
                val getVentilatorNoAuthResult = getVentilatorNoAuth.body()?.result

                val isRegistered = getVentilatorNoAuthResult?.is_registered ?: false

                //登録済みであるかどうか
                if (isRegistered) {
                    val ventilatorId: Int? = getVentilatorNoAuthResult?.ventilator_id
                    if (ventilatorId != null) {
                        with(currentVentilatorPref.edit()) {

                            putInt(
                                context.getString(R.string.current_ventilator_id),
                                ventilatorId.toInt()
                            )
                            commit()
                        }
                    }

                } else {
                    //TODO("位置情報の取得")
                    val latitude = "35.681300"
                    val longitude = "139.767165"
                    val createVentilatorForm = CreateVentilatorForm(gs1Code, latitude, longitude)
                    val createVentilatorNoAuth =
                        repository.createVentilatorNoAuth(createVentilatorForm, appkey)
                    if (createVentilatorNoAuth.isSuccessful) {
                        val ventilatorId: Int? =
                            createVentilatorNoAuth.body()?.result?.ventilator_id
                        if (ventilatorId != null) {
                            with(currentVentilatorPref.edit()) {

                                putInt(
                                    context.getString(R.string.current_ventilator_id),
                                    ventilatorId.toInt()
                                )
                                commit()
                            }
                        }
                    }
                }//登録済みでない場合
            }

        } else {
            //TODO("ログインする=ユーザトークンがある場合の処理")
            val getVentilator = repository.getVentilator(gs1Code, appkey, userToken)
            if (getVentilator.isSuccessful) {
                val getVentilatorResult = getVentilator.body()?.result

                val isRegistered = getVentilatorResult?.is_registered ?: false
                //登録済みであるかどうか
                if (isRegistered) {
                    val organizationChecked = getVentilatorResult?.organization_checked ?: false

                    val ventilatorId = getVentilatorResult?.ventilator_id

                    if (ventilatorId != null) {
                        with(currentVentilatorPref.edit()) {

                            putInt(
                                context.getString(R.string.current_ventilator_id),
                                ventilatorId.toInt()
                            )
                            commit()
                        }
                    }
                    //登録済みであればユーザトークンとの組織齟齬がないかどうか
                    if (organizationChecked) {

                        //組織齟齬がなければ、組織登録されているかどうか
                        if (getVentilatorResult?.organization_code.isNullOrEmpty()) {
                            //組織登録がなされていなければユーザトークンをもとに組織登録を行う
                            val updateVentilatorForm =
                                UpdateVentilatorForm(getVentilatorResult?.start_using_at)
                            val updateVentilator = repository.updateVentilator(
                                ventilatorId,
                                updateVentilatorForm,
                                appkey,
                                userToken
                            )
                        }
                    } else {
                        //TODO("組織齟齬がある場合の処理")
                    }

                } else {
                    //TODO("位置情報の取得")
                    val latitude = "35.681300"
                    val longitude = "139.767165"
                    val createVentilatorForm = CreateVentilatorForm(gs1Code, latitude, longitude)
                    val createVentilator =
                        repository.createVentilator(createVentilatorForm, appkey, userToken)
                    if (createVentilator.isSuccessful) {
                        val ventilatorId: Int? = createVentilator.body()?.result?.ventilator_id
                        if (ventilatorId != null) {
                            with(currentVentilatorPref.edit()) {

                                putInt(
                                    context.getString(R.string.current_ventilator_id),
                                    ventilatorId.toInt()
                                )
                                commit()
                            }
                        }
                    }
                }//登録済みでない場合
            }
        }
    }


}