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
}