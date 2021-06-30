 package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.messaging.FirebaseMessaging
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.util.*

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
                //インスタンスidを取得
                val appkeyFetchForm = AppkeyFetchForm(token)
                val createAppkey = repository.createAppkey(appkeyFetchForm)
                if (createAppkey.isSuccessful) {
                    val createAppkeyResult = createAppkey.body()?.result
                    with(networkPref.edit()) {
                        putString(
                            context.getString(R.string.appkey),
                            createAppkeyResult?.appkey.toString()
                        )
                        commit()
                    }
                } else {
                    Log.e("checkAppkey:Failed",createAppkey.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e("checkAppkey:Failed", e.stackTraceToString())
                showDialogConnectionError.value = Event("connection_error")
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