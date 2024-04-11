package com.dhxxn17.data.network

import android.util.Log
import com.dhxxn17.data.api.SwapApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitService {

    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor(
                    HttpLoggingInterceptor.Logger {
                        try {
                            JSONObject(it)
                            Log.d(LOGGER_NAME, it)
                        } catch (e: Exception) {
                            Log.d(LOGGER_NAME, it)
                        }
                    }
                ).apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            ).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): SwapApi {
        return retrofit.create(SwapApi::class.java)
    }

    companion object {
        private const val LOGGER_NAME = "WILLOG_LOGGER"
        private const val BASE_URL = "http://1.215.235.253:17098/"
    }
}