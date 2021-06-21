package jp.microvent.microvent.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.*
import com.google.android.gms.location.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.*
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.runBlocking
import retrofit2.Response

open class BaseViewModel(
    private val myApplication: Application
) : AndroidViewModel(myApplication) {

    protected val context: Context by lazy {
        getApplication<Application>().applicationContext
    }

    protected val repository = MicroventRepository.instance

    protected val networkPref: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.network_pref),
            Context.MODE_PRIVATE
        )
    }

    protected val currentVentilatorPref: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.current_ventilator_pref),
            Context.MODE_PRIVATE
        )
    }

    private val unitPref: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.unit_pref),
            Context.MODE_PRIVATE
        )
    }

    /**
     * アプリキー取得
     */
    val appkey: String by lazy {
        networkPref.getString(context.getString(R.string.appkey), "") ?: ""
    }

    /**
     * ユーザトークン取得
     */
    val userToken: String by lazy {
        networkPref.getString(context.getString(R.string.user_token), "") ?: ""
    }

    /**
     * 直近に読み込んだ呼吸器id取得
     */
    val ventilatorId: Int? by lazy {
        val ventilatorId =
            currentVentilatorPref.getInt(context.getString(R.string.current_ventilator_id), 0)
        if (ventilatorId == 0) {
            null
        } else {
            ventilatorId
        }
    }

    /**
     * 直近に読み込んだ呼吸器のgs1Code取得
     */
    val latestGs1Code: String? by lazy {
        currentVentilatorPref.getString("gs1Code", null)
    }

    /**
     * 直近に読み込んだ呼吸器に紐づく患者id取得
     */
    val patientId: Int? by lazy {
        val patientId =
            currentVentilatorPref.getInt(context.getString(R.string.current_patient_id), 0)
        if (patientId == 0) {
            null
        } else {
            patientId
        }
    }

    /**
     * 与えられた測定項目文字列の単位を取得
     * strings.xmlで測定項目文字列定義
     */
    fun unitOf(label: String): String? = unitPref.run {
        getString(label, null)
    }

    /**
     * 単位をsharedPrefに格納
     */
    fun putUnits(units: Units) {
        with(unitPref.edit()) {
            putString(context.getString(R.string.height_pref_key), units.height)
            putString(context.getString(R.string.weight_pref_key), units.weight)
            putString(context.getString(R.string.air_flow_pref_key), units.airFlow)
            putString(context.getString(R.string.o2_flow_pref_key), units.o2Flow)
            putString(context.getString(R.string.total_flow_pref_key), units.totalFlow)
            putString(context.getString(R.string.estimated_mv_pref_key), units.estimatedMv)
            putString(context.getString(R.string.airway_pressure_pref_key), units.airwayPressure)
            putString(context.getString(R.string.estimated_peep_pref_key), units.estimatedPeep)
            putString(context.getString(R.string.vt_per_kg_pref_key), units.vtPerKg)
            putString(context.getString(R.string.predicted_vt_pref_key), units.predictedVt)
            putString(context.getString(R.string.estimated_vt_pref_key), units.estimatedVt)
            putString(context.getString(R.string.i_pref_key), units.i)
            putString(context.getString(R.string.e_pref_key), units.e)
            putString(context.getString(R.string.i_avg_pref_key), units.iAvg)
            putString(context.getString(R.string.e_avg_pref_key), units.eAvg)
            putString(context.getString(R.string.rr_pref_key), units.rr)
            putString(context.getString(R.string.fio2_pref_key), units.fio2)
            putString(context.getString(R.string.spo2_pref_key), units.spo2)
            putString(context.getString(R.string.etco2_pref_key), units.etco2)
            putString(context.getString(R.string.pao2_pref_key), units.pao2)
            putString(context.getString(R.string.paco2_pref_key), units.paco2)
            commit()
        }
    }

    /**
     * 実際に表示する文字列に単位をsharedPrefから適用する\
     * layoutLiveData:layout.xmlとリンクしているlivedata->実際に表示される文字列
     * stringValue:string.xmlとリンクしている文字列。これを加工して実際に表示される文字列が決まる
     * prefKey:unitPrefで定義されている測定値のキー
     */
    fun setUnit(layoutLiveData: MutableLiveData<String>, stringValue: String, prefKey: String) {
        val unit = unitPref.getString(prefKey, null)
        val netString = String.format(stringValue, unit)
        layoutLiveData.postValue(netString)
    }


    /**
     * 直近に読み込んだ呼吸器idとそれに紐づく情報(患者idを含む)を破棄
     */
    @SuppressLint("CommitPrefEdits")
    protected fun resetCurrentVentilatorPref() {
        with(currentVentilatorPref.edit()) {
            clear()
            commit()
        }
    }

    /**
     * ユーザトークンの破棄。サーバ側での破棄も合わせて
     */
    protected fun resetUserToken(): Response<ApiResult<DeletedUserToken>> = runBlocking {
        repository.deleteUserToken(appkey, userToken).also {
            //内部ストレージに記録してあるトークンも削除する
            with(networkPref.edit()) {
                remove(context.getString(R.string.user_token))
                commit()
            }
        }
    }

    /**
     * ログイン中かどうか
     */
    protected fun loggedIn(): Boolean = !userToken.isNullOrEmpty()

    /**
     * ログイン画面遷移
     */
    val transitionToAuth: MediatorLiveData<Event<String>> by lazy {
        MediatorLiveData()
    }

    /**
     * 位置情報取得用
     */
    var latitude: String? = null
    var longitude: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    fun setLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient
            .lastLocation
            .addOnSuccessListener {
                if (it != null) {
                    latitude = it.latitude.toString()
                    longitude = it.longitude.toString()
                } else {
                    //以下、端末で一度も位置情報を取得したことがない場合にnullが帰ってこないようにrequestLocationUpdateを走らせる
                    val request = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(500)
                        .setFastestInterval(300)

                    fusedLocationClient
                        .requestLocationUpdates(
                            request,
                            object : LocationCallback() {
                                override fun onLocationResult(result: LocationResult) {
                                    val location = result.lastLocation
                                    latitude = location.latitude.toString()
                                    longitude = location.longitude.toString()

                                    // 現在地だけ欲しいので、1回取得したらすぐに外す
                                    fusedLocationClient.removeLocationUpdates(this)
                                }
                            },
                            null
                        )
                }
            }
    }

    /**
     * ダイアログ表示用イベント
     */
    val showDialogConnectionError: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }

    /**
     * トースト表示用イベント
     */
    val showToast: MutableLiveData<Event<String>> by lazy {
        MutableLiveData()
    }


    //エラー処理 TODO:バリデエラー時の処理詳細
    protected fun <T : Any?> errorHandling(errorResponse: Response<T>) {
        when (errorResponse.code()) {
            400 -> {
                badRequestHandling()
            }
            401 -> {
                unauthorizedHandling()
            }

            500 -> {
                serverErrorHandling()
            }
        }
    }

    protected fun badRequestHandling() {
        showToast.value = Event(context.getString(R.string.validation_error_toast))
    }

    protected fun unauthorizedHandling() {
        resetUserToken()
        transitionToAuth.value = Event("transitionToAuth")
    }

    protected fun serverErrorHandling() {
        showToast.value = Event(context.getString(R.string.server_error_toast))
    }
}



