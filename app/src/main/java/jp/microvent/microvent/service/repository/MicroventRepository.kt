package jp.microvent.microvent.service.repository

import android.preference.PreferenceManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.microvent.microvent.service.model.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

//forLocal
const val MICROVENT_URL = "http://api.microvent.local/"
const val API_TOKEN = "secret"

class MicroventRepository {
    companion object Factory {
        val instance: MicroventRepository
            @Synchronized get() {
                return MicroventRepository()
            }
    }

    val moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val microventApiInterceptor by lazy {
        //リクエスト内容に対する干渉を記述(header付与)等
        Interceptor { chain ->
            val original = chain.request()
            val languageCode:String = Locale.getDefault().language

            chain.proceed(
                original.newBuilder()
                    .header("Accept", "application/json")
                    .header("Accept-Language", languageCode)
                    .build()
            )
        }
    }

    private val microventApiClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(microventApiInterceptor)
            .addInterceptor(logging)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(MICROVENT_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(microventApiClient)
            .build()
    }

    protected val microventApiService: MicroventApiService =
        retrofit.create(MicroventApiService::class.java)

    /**********
     * appkey *
     **********/
    suspend fun createAppkey(
        appkeyFetchForm: AppkeyFetchForm?,
    ): Response<ApiResult<Appkey>> =
        microventApiService.createAppkey(appkeyFetchForm, API_TOKEN)

    fun syncedCreateAppkey(
        appkeyFetchForm: AppkeyFetchForm?
    ): Response<ApiResult<Appkey>> =
        microventApiService.syncedCreateAppkey(appkeyFetchForm, API_TOKEN).execute()

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
    suspend fun calcDefaultFlow(
        accountName: String?,
        appkey: String?
    ): Response<ApiResult<DefaultFlow>> =
        microventApiService.calcDefaultFlow(accountName, appkey)

    suspend fun calcEstimatedData(
        airwayPressure: String?,
        airFlow: String?,
        o2Flow: String?,
        appkey: String?
    ): Response<ApiResult<EstimatedData>> =
        microventApiService.calcEstimatedData(airwayPressure, airFlow, o2Flow, appkey)

    suspend fun calcIeManual(
        ieManualFetchForm: IeManualFetchForm?,
        appkey: String?
    ): Response<ApiResult<Ie>> =
        microventApiService.calcIeManual(ieManualFetchForm, appkey)

    suspend fun calcIeSound(
        ieSoundFetchForm: IeSoundFetchForm?,
        appkey: String?
    ): Response<ApiResult<Ie>> =
        microventApiService.calcIeSound(ieSoundFetchForm, appkey)

    /***********
     * patient *
     ***********/
    suspend fun createPatient(
        createPatientForm: CreatePatientForm?,
        appkey: String?,
        userToken: String?
    ): Response<ApiResult<CreatedPatient>> =
        microventApiService.createPatient(createPatientForm, appkey, userToken)

    suspend fun createPatientNoAuth(
        createPatientForm: CreatePatientForm?,
        appkey: String?
    ): Response<ApiResult<CreatedPatient>> =
        microventApiService.createPatientNoAuth(createPatientForm, appkey)

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
        microventApiService.updatePatient(patientId, updatePatientForm, appkey, userToken)

    suspend fun updatePatientNoAuth(
        patientId: Int?,
        updatePatientForm: UpdatePatientForm?,
        appkey: String?,
    ): Response<ApiResult<UpdatedPatient>> =
        microventApiService.updatePatientNoAuth(patientId, updatePatientForm, appkey)

    suspend fun getPatientObs(
        patientId: Int?,
        appkey: String?,
        userToken: String
    ): Response<ApiResult<PatientObs>> =
        microventApiService.getPatientObs(patientId,appkey,userToken)

    suspend fun createPatientObs(
        patientId: Int?,
        createPatientObsForm: CreatePatientObsForm?,
        appkey: String?,
        userToken: String
    ): Response<ApiResult<CreatedPatientObs>> =
        microventApiService.createPatientObs(patientId,createPatientObsForm,appkey,userToken)

    suspend fun updatePatientObs(
        patientId: Int?,
        updatePatientObsForm: UpdatePatientObsForm?,
        appkey: String?,
        userToken: String
    ): Response<ApiResult<UpdatedPatientObs>> =
        microventApiService.updatePatientObs(patientId,updatePatientObsForm,appkey,userToken)

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
        microventApiService.updateVentilator(ventilatorId, updateVentilatorForm, appkey, userToken)

    suspend fun deactivateVentilator(
        ventilatorId: Int?,
        appkey: String?,
        userToken: String?
    ): Response<ApiResult<DeactivatedVentilator>> =
        microventApiService.deactivateVentilator(ventilatorId, appkey, userToken)

    suspend fun deactivateVentilatorNoAuth(
        ventilatorId: Int?,
        appkey: String?,
    ): Response<ApiResult<DeactivatedVentilator>> =
        microventApiService.deactivateVentilatorNoAuth(ventilatorId, appkey)

    /********************
     * ventilator_value *
     ********************/
    suspend fun getVentilatorValueList(
        ventilatorId: Int?,
        limit: Int?,
        offset: Int?,
        fixedFlg: Int?,
        appkey: String?,
    ): Response<ApiResult<List<VentilatorValueListElm>>> =
        microventApiService.getVentilatorValueList(ventilatorId, limit, offset, fixedFlg, appkey)

    suspend fun getVentilatorValue(
        ventilatorValueId: Int?,
        appkey: String?,
    ): Response<ApiResult<VentilatorValue>> =
        microventApiService.getVentilatorValue(ventilatorValueId, appkey)

    suspend fun createVentilatorValue(
        createVentilatorValueForm: CreateVentilatorValueForm?,
        appkey: String?,
        userToken: String?
    ): Response<ApiResult<CreatedVentilatorValue>> =
        microventApiService.createVentilatorValue(createVentilatorValueForm, appkey, userToken)

    suspend fun createVentilatorValueNoAuth(
        createVentilatorValueForm: CreateVentilatorValueForm?,
        appkey: String?
    ): Response<ApiResult<CreatedVentilatorValue>> =
        microventApiService.createVentilatorValueNoAuth(createVentilatorValueForm, appkey)

    suspend fun updateVentilatorValue(
        ventilatorValueId: Int?,
        updateVentilatorValueForm: UpdateVentilatorValueForm?,
        appkey: String?,
        userToken: String?
    ): Response<ApiResult<UpdatedVentilatorValue>> = microventApiService.updateVentilatorValue(
        ventilatorValueId,
        updateVentilatorValueForm,
        appkey,
        userToken
    )

    /********
     * user *
     ********/
    suspend fun getUser(
        appkey: String?,
        userToken: String?,
    ): Response<ApiResult<User>> = microventApiService.getUser(appkey, userToken)

    suspend fun updateUser(
        updateUserForm: UpdateUserForm?,
        appkey: String?,
        userToken: String?,
    ): Response<ApiResult<UpdatedUser>> =
        microventApiService.updateUser(updateUserForm, appkey, userToken)

    /**************
     * bug_report *
     **************/
    suspend fun createBugReport(
        createBugReportForm: CreateBugReportForm?,
        appkey: String?,
        userToken: String?
    ): Response<ApiResult<CreatedBugReport>> = microventApiService.createBugReport(
        createBugReportForm,
        appkey,
        userToken
    )

    suspend fun createBugReportNoAuth(
        createBugReportForm: CreateBugReportForm?,
        appkey: String?
    ): Response<ApiResult<CreatedBugReport>> = microventApiService.createBugReportNoAuth(
        createBugReportForm,
        appkey
    )

    /*************
     * chat_room *
     *************/
    suspend fun fetchRoomUri(
        appkey: String?
    ): Response<ApiResult<CreatedRoom>> = microventApiService.fetchRoomUri(
        appkey
    )
}