package ru.itrequest.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import ru.itrequest.data.WaterfallResponse

interface WaterfallService {

    @GET("/all")
    suspend fun getAll(): WaterfallResponse

    companion object {
        private const val BASE = "http://blabla.com"

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