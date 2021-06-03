package jp.microvent.microvent.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.service.repository.TestRepository
import jp.microvent.microvent.viewModel.util.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

class SettingViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication){

    val isLogoutEnabled: MutableLiveData<Boolean> by lazy{
        if(userToken.isNullOrEmpty()){
            MutableLiveData(false)
        }else{
            MutableLiveData(true)
        }
    }

    //画面遷移イベントの設定
    val transitionToHome = MutableLiveData<Event<String>>()

    fun onClickLogoutButton() {

        viewModelScope.launch {
            try{
                val deleteUserToken = repository.deleteUserToken(appkey,userToken)
                if(deleteUserToken.isSuccessful){
                    //サーバでのトークン削除が確認できたら内部ストレージに記録してあるトークンも削除する
                    with(networkPref.edit()){
                        remove(context.getString(R.string.user_token))
                        commit()
                    }
                    transitionToHome.value = Event("transitionToHome")
                } else {
                    Log.i("test","test")
                }
            }catch (e:Exception){
                Log.e("removeToken:Failed", e.stackTrace.toString())
            }
        }
    }

}