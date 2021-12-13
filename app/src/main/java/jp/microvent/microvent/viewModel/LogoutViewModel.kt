package jp.microvent.microvent.viewModel

import android.app.Application
import jp.microvent.microvent.R
import jp.microvent.microvent.viewModel.util.Event
import java.net.ConnectException

class LogoutViewModel(
    private val myApplication: Application
) : BaseViewModel(myApplication) {
    fun onClickLogoutButton() {
        try {
            sharedAccessToken.resetUserToken().let {
                showToast.value = Event(context.getString(R.string.logout_success))
                transitionToAuth.value = Event("transitionToAuth")
            }
        } catch (e: ConnectException) {
            handleConnectionError()
        }
    }
}