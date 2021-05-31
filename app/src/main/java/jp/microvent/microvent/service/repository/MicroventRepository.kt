package jp.microvent.microvent.service.repository

import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.microvent.microvent.service.model.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.reflect.jvm.internal.impl.util.Check

const val MICROVENT_URL = "http://api.microvent.local/"

class MicroventRepository {
    companion object Factory {
        val instance: MicroventRepository
            @Synchronized get() {
                return MicroventRepository()
            }
    }

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    //APIから取得したjsonデータをオブジェクト化してくれるRetrofitオブジェクトの作成
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(MICROVENT_URL)
        .build()

    private val microventApiService: MicroventApiService =
        retrofit.create(MicroventApiService::class.java)

    /**********
     * appkey *
     **********/
    suspend fun getAppkey(
        appkeyFetchForm: AppkeyFetchForm?,
        apiToken: String?
    ): Response<ApiResult<Appkey>> =
        microventApiService.getAppkey(appkeyFetchForm, apiToken)

    /********
     * auth *
     ********/
    suspend fun checkUserToken(
        accountName: String?,
        appkey: String?
    ): Response<ApiResult<CheckToken>> =
        microventApiService.checkUserToken(accountName, appkey)

    suspend fun getUserToken(
        userTokenFetchForm: UserTokenFetchForm?,
        appkey: String?
    ): Response<ApiResult<UserToken>> =
        microventApiService.getUserToken(userTokenFetchForm, appkey)

    suspend fun deleteUserToken(
        appkey: String?,
        userToken: String?
    ): Response<ApiResult<UserToken>> =
        microventApiService.deleteUserToken(appkey, userToken)

    /*************
     * calculate *
     *************/

    /***********
     * patient *
     ***********/

    /**************
     * ventilator *
     **************/

    /********************
     * ventilator_value *
     ********************/

    /********
     * user *
     ********/
}