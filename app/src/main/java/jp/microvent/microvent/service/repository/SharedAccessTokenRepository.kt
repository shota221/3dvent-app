package jp.microvent.microvent.service.repository

import android.content.Context
import android.content.SharedPreferences
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.ApiResult
import jp.microvent.microvent.service.model.DeletedUserToken
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class SharedAccessTokenRepository(context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.network_pref),
            Context.MODE_PRIVATE
        )
    }

    private val repository = MicroventRepository.instance

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
    var userToken: String
        get() {
            return sharedPreferences.getString("X-User-Token", "") ?: ""
        }
        set(userToken: String) {
            sharedPreferences.edit().run {
                putString("X-User-Token", userToken)
                commit()
            }
        }

    fun resetUserToken(): Response<ApiResult<DeletedUserToken>> = runBlocking {
            repository.deleteUserToken(appkey, userToken).also {
                sharedPreferences.edit().run {
                    remove("X-User-Token")
                    commit()
                }
            }
    }

    fun installAppkey(appkey: String) {
        sharedPreferences.edit().run {
            putString("X-App-Key", appkey)
            commit()
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