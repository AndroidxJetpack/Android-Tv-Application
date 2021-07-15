package com.ncgtelevision.net.retrofit_clients

import android.content.Context
import com.ncgtelevision.net.local_storage.TokenStorage
import com.ncgtelevision.net.utilities.ApiConfig
import com.ncgtelevision.net.utilities.ConstantUtility
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {

        private val BASE_URL = ApiConfig.BASE_URL
        private val WITHOUT_AUTH_BASE_URL = ApiConfig.WITHOUT_AUTH_BASE_URL

        private var retrofit: Retrofit? = null
//        private lateinit var context: Context
//        var authToken: String = TokenStorage.readSharedToken(context, ConstantUtility.AUTH_TOKEN, null)

        private fun ApiClient() {
            //default constructor
        }

        @JvmStatic
        fun getClient(mContext:Context?): Retrofit? {
            val interceptor = HttpLoggingInterceptor()
            val interceptor2 = OAuthInterceptor("Bearer", TokenStorage.readSharedToken(mContext))
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor2).addInterceptor(
                interceptor
            ).build()
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit
        }


        @JvmStatic
        fun getSigInClient(): Retrofit? {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                    .connectTimeout(5,TimeUnit.MINUTES)
                    .writeTimeout(5,TimeUnit.MINUTES)
                    .readTimeout(5,TimeUnit.MINUTES)
                    .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
                    .protocols(listOf(Protocol.HTTP_1_1))
                    .addInterceptor(interceptor)
                    .build()
            retrofit = Retrofit.Builder()
                .baseUrl(WITHOUT_AUTH_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit
        }
    }
}

/*

OkHttpClient okHttpClient = new OkHttpClient.Builder()
    .connectTimeout(1, TimeUnit.MINUTES)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(15, TimeUnit.SECONDS)
    .build();

 */