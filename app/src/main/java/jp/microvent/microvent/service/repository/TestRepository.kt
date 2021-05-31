package jp.microvent.microvent.service.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.microvent.microvent.R
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.IllegalStateException

private const val BASE_URL = "http://apppppp.com/"

class TestRepository {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    //APIから取得したjsonデータをオブジェクト化してくれるRetrofitオブジェクトの作成
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    private val testApiService: TestApiService = retrofit.create(TestApiService::class.java)

    suspend fun getJojo() = testApiService.getJojo()

    companion object Factory {
        val instance: TestRepository
            @Synchronized get() {
                return TestRepository()
            }
    }
}