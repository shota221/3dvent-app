package jp.microvent.microvent.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.*
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.model.CreateUserTokenForm
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.service.repository.TestRepository
import jp.microvent.microvent.service.repository.UserRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(
    private val myApplication: Application
) : AndroidViewModel(myApplication) {

    protected val context: Context by lazy {
        getApplication<Application>().applicationContext
    }

    protected val repository = MicroventRepository.instance

    protected val networkPref by lazy {
        context.getSharedPreferences(context.getString(R.string.network_pref), Context.MODE_PRIVATE)
    }

    protected val currentVentilatorPref by lazy {
        context.getSharedPreferences(context.getString(R.string.current_ventilator_pref), Context.MODE_PRIVATE)
    }

    /**
     * アプリキー取得
     */
    val appkey:String by lazy {
        networkPref.getString(context.getString(R.string.appkey), "") ?: ""
    }

    /**
     * ユーザトークン取得
     */
    val userToken :String by lazy{
        networkPref.getString(context.getString(R.string.user_token), "") ?: ""
    }

    /**
     * 直近に読み込んだ呼吸器id取得
     */
    val ventilatorId :Int? by lazy{
        val ventilatorId = currentVentilatorPref.getInt(context.getString(R.string.current_ventilator_id),0)
        if(ventilatorId == 0){
            null
        } else {
            ventilatorId
        }
    }
}