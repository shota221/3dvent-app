package jp.microvent.microvent.service.repository

import jp.microvent.microvent.service.model.Test
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface TestApiService {
    @GET("jojo.json")
    suspend fun getJojo():Response<Test>
}