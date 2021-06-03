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
    suspend fun createAppkey(
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
    suspend fun createUserToken(
        @Body createUserTokenForm: CreateUserTokenForm?,
        @Header("X-App-Key") appkey: String?
    ): Response<ApiResult<CreatedUserToken>>

    /**
     * ログアウト
     */
    @Headers("Content-Type:application/json; charset=UTF-8")
    @DELETE("auth/token")
    suspend fun deleteUserToken(
        @Header("X-App-Key") appkey: String?,
        @Header("X-User-Token") userToken: String?,
    ): Response<ApiResult<DeletedUserToken>>

    /*************
     * calculate *
     *************/
    @GET("calculate/default_flow")
    suspend fun calcDefaultFlow(
        @Query("name") accountName: String?,
        @Header("X-App-Key") appkey: String?
    ): Response<ApiResult<DefaultFlow>>

    @GET("calculate/estimated_data")
    suspend fun calcEstimatedData(
        @Query("airway_pressure") airwayPressure: String?,
        @Query("air_flow") airFlow: String?,
        @Query("o2_flow") o2Flow: String?,
        @Header("X-App-Key") appkey: String?
    ): Response<ApiResult<EstimatedData>>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("calculate/ie/manual")
    suspend fun calcIeManual(
        @Body ieManualFetchForm: IeManualFetchForm?,
        @Header("X-App-Key") appkey: String?
    ):Response<ApiResult<Ie>>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("calculate/ie/sound")
    suspend fun calcIeSound(
        @Body ieSoundFetchForm: IeSoundFetchForm?,
        @Header("X-App-Key") appkey: String?
    ):Response<ApiResult<Ie>>


    /***********
     * patient *
     ***********/
    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("patient")
    suspend fun createPatient(
        @Body createPatientForm: CreatePatientForm?,
        @Header("X-App-Key") appkey: String?,
        @Header("X-User-Token") userToken: String?
    ): Response<ApiResult<CreatedPatient>>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("patient/no_auth")
    suspend fun createPatientNoAuth(
        @Body createPatientForm: CreatePatientForm?,
        @Header("X-App-Key") appkey: String?,
    ): Response<ApiResult<CreatedPatient>>

    @GET("patient/{id}")
    suspend fun getPatient(
        @Path("id") patientId:Int?,
        @Header("X-App-Key") appkey: String?
    ): Response<ApiResult<Patient>>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @PUT("patient/{id}")
    suspend fun updatePatient(
        @Path("id") patientId:Int?,
        @Body updatePatientForm: UpdatePatientForm?,
        @Header("X-App-Key") appkey: String?,
        @Header("X-User-Token") userToken: String?
    ): Response<ApiResult<UpdatedPatient>>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @PUT("patient/{id}")
    suspend fun updatePatientNoAuth(
        @Path("id") patientId:Int?,
        @Body updatePatientForm: UpdatePatientForm?,
        @Header("X-App-Key") appkey: String?,
    ): Response<ApiResult<UpdatedPatient>>

    /**************
     * ventilator *
     **************/
    @GET("ventilator")
    suspend fun getVentilator(
        @Query("gs1_code") gs1Code: String?,
        @Header("X-App-Key") appkey: String?,
        @Header("X-User-Token") userToken: String?
    ):Response<ApiResult<Ventilator>>

    @GET("ventilator/no_auth")
    suspend fun getVentilatorNoAuth(
        @Query("gs1_code") gs1Code: String?,
        @Header("X-App-Key") appkey: String?,
    ):Response<ApiResult<Ventilator>>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("ventilator")
    suspend fun createVentilator(
        @Body createVentilatorForm: CreateVentilatorForm?,
        @Header("X-App-Key") appkey: String?,
        @Header("X-User-Token") userToken: String?
    ):Response<ApiResult<CreatedVentilator>>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("ventilator/no_auth")
    suspend fun createVentilatorNoAuth(
        @Body createVentilatorForm: CreateVentilatorForm?,
        @Header("X-App-Key") appkey: String?
    ):Response<ApiResult<CreatedVentilator>>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @PUT("ventilator/{id}")
    suspend fun updateVentilator(
        @Path("id") ventilatorId: Int?,
        @Body updateVentilatorForm: UpdateVentilatorForm?,
        @Header("X-App-Key") appkey: String?,
        @Header("X-User-Token") userToken: String?
    ):Response<ApiResult<UpdatedVentilator>>

    /********************
     * ventilator_value *
     ********************/

    /********
     * user *
     ********/


}