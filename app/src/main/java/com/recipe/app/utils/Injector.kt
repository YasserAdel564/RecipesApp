package com.recipe.app.utils

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.recipe.app.App
import com.recipe.app.data.storage.local.AppDatabase
import com.recipe.app.data.storage.local.PreferencesHelper
import com.recipe.app.data.storage.remote.RetrofitApiServices
import com.recipe.app.repos.RecipesRepo
import com.recipe.app.utils.Constants.BASE_URL
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


object Injector {

    private val coroutinesDispatcherProvider = CoroutinesDispatcherProvider(
        Dispatchers.Main,
        Dispatchers.Default,
        Dispatchers.IO
    )

    fun getApplicationContext() = App.instance
    fun getCoroutinesDispatcherProvider() = coroutinesDispatcherProvider
    fun getApiService() = create(BASE_URL, getOkHttpClient())
    fun getPreferenceHelper() = PreferencesHelper(getApplicationContext())
    fun getAppDatabase() = AppDatabase.invoke(getApplicationContext())


    /*=================Retrofit=================*/

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun getApiServiceHeader(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
                .newBuilder()

            if (chain.request().header("Accept-Language") == null) {
                request.addHeader(
                    "Accept-Language",
                    chain.request().header("Accept-Language") ?: Constants.Language.ENGLISH.value
                )
            }
            chain.proceed(request.build())
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(getApiServiceHeader())
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    private fun create(baseUrl: String, client: OkHttpClient): RetrofitApiServices {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(client)
            .build()
        return retrofit.create(RetrofitApiServices::class.java)
    }


    /*================================Repos======================================*/
    fun RecipesRepo() = RecipesRepo(getApiService())

}