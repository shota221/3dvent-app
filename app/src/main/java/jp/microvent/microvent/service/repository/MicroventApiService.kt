package jp.microvent.microvent.service.repository

import jp.microvent.microvent.service.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * retrofitにより自動で実装される
 */

interface MicroventApiService {

    /**********
     * appkey *
     **********/
    /**
     * 初回利用時アプリキー生成
     */
    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("appkey")
    suspend fun getAppkey(
        @Body appkeyFetchForm: AppkeyFetchForm?,
        @Header("X-Api-Token") apiToken: String?
    ): Response<ApiResult<Appkey>>

    /********
     * auth *
     ********/
    /**
     * ユーザが他端末でログイン中であるかどうか
     */
    @GET("auth/token")
    suspend fun checkUserToken(
        @Query("name") accountName: String?,
        @Header("X-App-Key") appkey: String?
    ): Response<ApiResult<CheckToken>>

    /**
     * ログイン
     */
    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("auth/token")
    suspend fun getUserToken(
        @Body userTokenFetchForm: UserTokenFetchForm?,
        @Header("X-App-Key") appkey: String?
    ): Response<ApiResult<UserToken>>

    /**
     * ログアウト
     */
    @Headers("Content-Type:application/json; charset=UTF-8")
    @DELETE("auth/token")
    suspend fun deleteUserToken(
        @Header("X-App-Key") appkey: String?,
        @Header("X-User-Token") userToken: String?,
    ): Response<ApiResult<UserToken>>

    /*************
     * calculate *
     *************/
    @GET("calculate/default_flow")
    suspend fun calcDefaultFlow(
        @Query("name") accountName: String?,
        @Header("X-App-Key") appkey: String?
    ): Response<ApiResult<CheckToken>>

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