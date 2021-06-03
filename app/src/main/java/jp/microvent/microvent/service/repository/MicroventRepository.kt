package jp.microvent.microvent.service.repository

import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.microvent.microvent.service.model.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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
    suspend fun createAppkey(
        appkeyFetchForm: AppkeyFetchForm?,
        apiToken: String?
    ): Response<ApiResult<Appkey>> =
        microventApiService.createAppkey(appkeyFetchForm, apiToken)

    /********
     * auth *
     ********/
    suspend fun checkUserToken(
        accountName: String?,
        appkey: String?
    ): Response<ApiResult<CheckToken>> =
        microventApiService.checkUserToken(accountName, appkey)

    suspend fun createUserToken(
        createUserTokenForm: CreateUserTokenForm?,
        appkey: String?
    ): Response<ApiResult<CreatedUserToken>> =
        microventApiService.createUserToken(createUserTokenForm, appkey)

    suspend fun deleteUserToken(
        appkey: String?,
        userToken: String?
    ): Response<ApiResult<DeletedUserToken>> =
        microventApiService.deleteUserToken(appkey, userToken)

    /*************
     * calculate *
     *************/


    /***********
     * patient *
     ***********/
    suspend fun createPatient(
        createPatientForm: CreatePatientForm?,
        appkey: String?,
        userToken: String?
    ): Response<ApiResult<CreatedPatient>> =
        microventApiService.createPatient(createPatientForm,appkey,userToken)

    suspend fun createPatientNoAuth(
        createPatientForm: CreatePatientForm?,
        appkey: String?
    ): Response<ApiResult<CreatedPatient>> =
        microventApiService.createPatientNoAuth(createPatientForm,appkey)

    suspend fun getPatient(
        patientId: Int?,
        appkey: String?
    ): Response<ApiResult<Patient>> =
        microventApiService.getPatient(patientId, appkey)

    suspend fun updatePatient(
        patientId: Int?,
        updatePatientForm: UpdatePatientForm?,
        appkey: String?,
        userToken: String?
    ): Response<ApiResult<UpdatedPatient>> =
        microventApiService.updatePatient(patientId,updatePatientForm,appkey,userToken)

    suspend fun updatePatientNoAuth(
        patientId: Int?,
        updatePatientForm: UpdatePatientForm?,
        appkey: String?,
    ): Response<ApiResult<UpdatedPatient>> =
        microventApiService.updatePatientNoAuth(patientId,updatePatientForm,appkey)

    /**************
     * ventilator *
     **************/
    suspend fun getVentilator(
        gs1Code: String?,
        appkey: String?,
        userToken: String?
    ): Response<ApiResult<Ventilator>> =
        microventApiService.getVentilator(gs1Code, appkey, userToken)

    suspend fun getVentilatorNoAuth(
        gs1Code: String?,
        appkey: String?,
    ): Response<ApiResult<Ventilator>> = microventApiService.getVentilatorNoAuth(gs1Code, appkey)

    suspend fun createVentilator(
        createVentilatorForm: CreateVentilatorForm?,
        appkey: String?,
        userToken: String?,
    ): Response<ApiResult<CreatedVentilator>> =
        microventApiService.createVentilator(createVentilatorForm, appkey, userToken)

    suspend fun createVentilatorNoAuth(
        createVentilatorForm: CreateVentilatorForm?,
        appkey: String?
    ): Response<ApiResult<CreatedVentilator>> =
        microventApiService.createVentilatorNoAuth(createVentilatorForm, appkey)

    suspend fun updateVentilator(
        ventilatorId: Int?,
        updateVentilatorForm: UpdateVentilatorForm?,
        appkey: String?,
        userToken: String?
    ): Response<ApiResult<UpdatedVentilator>> =
        microventApiService.updateVentilator(ventilatorId,updateVentilatorForm,appkey,userToken)

    /********************
     * ventilator_value *
     ********************/

    /********
     * user *
     ********/
}