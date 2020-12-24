package ru.itrequest.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import ru.itrequest.data.WaterfallResponse

interface WaterfallService {

    @GET("/v3/2bbdea4d-0fa4-497d-9994-fb6770b02a31")
    suspend fun getAll(): WaterfallResponse

    /**
     * We use design.mocky.io for mock web service
     * (There are test data in a waterfalls.json file)
     */
    companion object {
        private const val BASE = "https://run.mocky.io"

        fun create(): WaterfallService {
            val logger = HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY }
            val client = OkHttpClient.Builder().apply {
                addInterceptor(logger)
            }.build()

            return Retrofit.Builder()
                .baseUrl(BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WaterfallService::class.java)
        }
    }
}