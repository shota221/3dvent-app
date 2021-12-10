 package jp.microvent.microvent.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.messaging.FirebaseMessaging
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.service.repository.SharedAccessTokenRepository
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.util.*

 class MainViewModel(
    private val myApplication: Application
) : AndroidViewModel(myApplication) {
     private val context: Context by lazy {
         getApplication<Application>().applicationContext
     }

     private val accessTokenRepos: SharedAccessTokenRepository by lazy {
         SharedAccessTokenRepository(context)
     }
     val showAppkeyCheckError: MutableLiveData<Event<String>> by lazy {
         MutableLiveData()
     }

     fun checkAppkey() {
         /**
          * 端末内にappkeyが存在しない場合
          */
         if (accessTokenRepos.appkey == "") {
             FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                 if (!task.isSuccessful) {
                     Log.w("test", "getInstanceId failed", task.exception)
                     return@addOnCompleteListener
                 }

                 //create new instance id Token
                 val idfv = task.result
                 createAppkey(idfv)

             }
         }
     }

     private fun createAppkey(idfv: String) {
         val apiRepos = MicroventRepository.instance
         //インスタンスidを取得
         val appkeyFetchForm = AppkeyFetchForm(idfv)
         viewModelScope.launch {
             try {
                 val createAppkey = apiRepos.createAppkey(appkeyFetchForm)
                 if (createAppkey.isSuccessful) {
                     val createAppkeyResult = createAppkey.body()?.result?.let {
                         accessTokenRepos.installAppkey(it.appkey)
                     }
                 }
             } catch (e: Exception) {
                 showAppkeyCheckError.value = Event("appkeyCheckError")
             }
         }
     }

 }