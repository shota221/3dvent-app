 package jp.microvent.microvent.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.messaging.FirebaseMessaging
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.Appkey
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.model.CreateUserTokenForm
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.service.repository.TestRepository
import jp.microvent.microvent.service.repository.UserRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {

    /**
     * アプリキーがnetwork_prefに記録されていなければサーバーに問いかけて登録
     */
    fun checkAppkey(){
        if(appkey==""){
            createInstanceId()
        }
    }

    /**
     * アプリキー発行
     */
    private fun createAppkey(token: String?) {
        viewModelScope.launch {
            try {
                val apiToken = context.getString(R.string.api_token_string)
                //インスタンスidを取得
                val appkeyFetchForm = AppkeyFetchForm(token)
                val createAppkey = repository.createAppkey(appkeyFetchForm, apiToken)
                if (createAppkey.isSuccessful) {
                    val createAppkeyResult = createAppkey.body()?.result
                    with(networkPref.edit()) {
                        putString(
                            context.getString(R.string.appkey),
                            createAppkeyResult?.appkey.toString()
                        )
                        commit()
                    }
                    Log.i("test", "done")
                } else {
                    Log.i("test", "test")
                }
            } catch (e: Exception) {
                Log.e("checkAppkey:Failed", e.stackTraceToString())
            }

        }
    }

    /**
     * プッシュ通知の際に必要となるInstanceId（microventサーバーに送りappkeyとしても用いる）をfirebaseから取得
     */
    private fun createInstanceId() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("test", "getInstanceId failed", task.exception)
                return@addOnCompleteListener
            }

            //create new instance id Token
            val msg = task.result
            Log.d("test", "token: $msg")
            createAppkey(msg)

        }
    }
}