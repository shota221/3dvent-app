package jp.microvent.microvent.service.repository

import android.content.Context
import android.content.SharedPreferences
import jp.microvent.microvent.R

class SharedAccessTokenRepository(context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.network_pref),
            Context.MODE_PRIVATE
        )
    }

    /**
     * appkey
     */
    val appkey: String
        get() {
            return sharedPreferences.getString("X-App-Key", "") ?: ""
        }

    /**
     * userToken
     */
    val userToken: String
        get() {
            return sharedPreferences.getString("X-User-Token", "") ?: ""
        }

    fun resetUserToken() {
        if (sharedPreferences.contains("X-User-Token")) {
            sharedPreferences.edit().run {
                remove("X-User-Token")
                commit()
            }
        }
    }

    //TODO DELETE_ME
    fun resetAll() {
        sharedPreferences.edit().run {
            clear()
            commit()
        }
    }
}