package jp.microvent.microvent.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.location.*
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.*
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.service.repository.SharedAccessTokenRepository
import jp.microvent.microvent.service.repository.SharedCurrentVentilatorRepository
import jp.microvent.microvent.service.repository.SharedUnitsRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.Response

open class BaseViewModel(
    private val myApplication: Application
) : AndroidViewModel(myApplication) {

    protected val context: Context by lazy {
        getApplication<Application>().applicationContext
    }

    protected val repository = MicroventRepository.instance

    private val moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    protected val sharedAccessToken: SharedAccessTokenRepository by lazy {
        SharedAccessTokenRepository(context)
    }
    protected val sharedCurrentVentilator: SharedCurrentVentilatorRepository by lazy {
        SharedCurrentVentilatorRepository(context)
    }
    protected val sharedUnits: SharedUnitsRepository by lazy {
        SharedUnitsRepository(context)
    }

    /**
     * ログイン中かどうか
     */
    protected fun loggedIn(): Boolean = !sharedAccessToken.userToken.isNullOrEmpty()

    /**
     * ventilator読み込み中かどうか（shardPrefに記録されているか）
     */
    protected fun hasReadQr(): Boolean = !(sharedCurrentVentilator.ventilatorId == null)

    /**
     * 読込中のventilatorにpatientが紐付いているかどうか（sharedPrefにpatientIdがあるか）
     */
    protected fun hasPatient(): Boolean = !(sharedCurrentVentilator.patientId == null)

    /**
     * ログイン画面遷移
     */
    val transitionToAuth: MediatorLiveData<Event<String>> by lazy {
        MediatorLiveData()
    }

    /**
     * ダイアログ表示用イベント
     */
    val showDialogConnectionError: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    /**
     * 通知ダイアログ表示用LiveData
     */
    val showDialogNotification: MutableLiveData<Event<DialogDescription>> by lazy {
        MutableLiveData()
    }

    /**
     * 通知ダイアログ表示用LiveData
     */
    val showDialogConfirmation: MutableLiveData<Event<DialogDescription>> by lazy {
        MutableLiveData()
    }

    /**
     * トースト表示用イベント
     */
    val showToast: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    /**
     * プログレスバー制御用イベント/trueでvisible,falseでgone
     */
    val setProgressBar: MutableLiveData<Event<Boolean>> by lazy {
        MutableLiveData()
    }


    protected fun handleConnectionError() {
        setProgressBar.value = Event(false)
        showToast.value = Event(context.getString(R.string.connection_error_toast))
    }

    protected fun <T : Any?> handleErrorResponse(errorResponse: Response<T>) {
        setProgressBar.value = Event(false)
        when (errorResponse.code()) {
            400 -> {
                handleBadRequest(errorResponse)
            }
            401 -> {
                handleUnauthorized()
            }
            403 -> {
                handleForbidden()
            }
            500 -> {
                handleServerError()
            }
        }
    }

    protected fun <T : Any?> handleBadRequest(errorResponse: Response<T>) {
        try {
            val adapter = moshi.adapter(ValidationError::class.java)
            val errorBody = JSONObject(errorResponse.errorBody()?.string());
            val validationError = adapter.fromJson(errorBody.toString())
            validationError!!.errors.forEach {
                showToast.value = if (it.message == null) {
                    Event(context.getString(R.string.validation_error_toast))
                } else {
                    Event(it.message.translated)
                }
            }
        } catch (e: Exception) {
            Log.e("test", e.stackTraceToString())
        }
    }

    protected fun handleUnauthorized() {
        showToast.value = if(sharedAccessToken.userToken.isNullOrEmpty()){
            Event(context.getString(R.string.login_required_toast))
        } else {
            sharedAccessToken.resetUserToken()
            Event(context.getString(R.string.invalid_user_token_toast))
        }
        transitionToAuth.value = Event("transitionToAuth")
    }

    protected fun handleForbidden() {
        showToast.value = Event(context.getString(R.string.forbidden_toast))
    }

    protected fun handleServerError() {
        showToast.value = Event(context.getString(R.string.server_error_toast))
    }

    protected fun showToastUpdated() {
        showToast.value = Event(context.getString(R.string.update_success))
    }

    protected fun showToastCreated() {
        showToast.value = Event(context.getString(R.string.create_success))
    }


    /**
     * ヘルプボタン用
     */
    val transitionToHelp: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    fun onClickHelpButton() {
        transitionToHelp.value = Event("transitionToHelp")
    }

    val showFlowDrawer: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    fun onClickFlowButton() {
        showFlowDrawer.value = Event("showFlowDrawer")
    }
}