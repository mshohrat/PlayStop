package com.ms.playstop.network.base

import android.annotation.SuppressLint
import android.os.Build
import com.ms.playstop.BuildConfig
import com.ms.playstop.MainActivity
import com.ms.playstop.model.Profile
import com.ms.playstop.network.model.RefreshTokenRequest
import com.orhanobut.hawk.Hawk
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.*

class ApiServiceGenerator {

    companion object {

        private val BASE_URL = BuildConfig.SERVER_ADDRESS

        val getApiService : ApiService by lazy {
            return@lazy setupApiService()
        }

        private fun setupApiService() : ApiService {
            val okHttpClient = OkHttpClient.Builder()

            okHttpClient.connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)

            val dispatcher = Dispatcher()
            dispatcher.maxRequestsPerHost = 1
            dispatcher.maxRequests = 1

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            okHttpClient.addInterceptor(object : Interceptor {
                @SuppressLint("CheckResult")
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val request: Request = if (token.isEmpty()) {
                        original.newBuilder()
                            .method(original.method(), original.body())
                            .header("Accept","application/json")
                            .header("Content-Type","application/json")
                            .header("User-Agent",userAgent)
                            .header("Version-Name",appVersionName)
                            .header("Version-Code",appVersionCode)
                            .build()
                    } else {
                        original.newBuilder()
                            .header("Authorization", "Bearer $token")
                            .header("Accept","application/json")
                            .header("Content-Type","application/json")
                            .header("User-Agent",userAgent)
                            .header("Version-Name",appVersionName)
                            .header("Version-Code",appVersionCode)
                            .method(original.method(), original.body())
                            .build()
                    }
                    val response = chain.proceed(request)
                    if (response.code() == 401) {
                        if(request?.url()?.pathSegments()?.contains("auth") == true && request?.url()?.pathSegments()?.contains("login") == true) {
                            return response
                        }
                        else if(request?.url()?.pathSegments()?.contains("oauth") == true && request?.url()?.pathSegments()?.contains("token") == true
                            || refreshToken.isEmpty())
                        {
                            //this is refresh token request. so, we should remove user and restart app
                            Hawk.delete(Profile.SAVE_KEY)
                            MainActivity.doRestart()
                        }
                        else
                        {
                            getApiService.refreshToken(RefreshTokenRequest(refreshToken))
                                ?.subscribeOn(Schedulers.io())
                                ?.observeOn(AndroidSchedulers.mainThread())
                                ?.subscribe({
                                    it?.let {
                                        val profile = Hawk.get(Profile.SAVE_KEY) as? Profile
                                        profile?.token = it.token
                                        profile?.refreshToken = it.refreshToken
                                        profile?.expiresIn = it.expiresIn
                                        Hawk.put(Profile.SAVE_KEY,profile)
                                    } ?: kotlin.run {
                                        Hawk.delete(Profile.SAVE_KEY)
                                        MainActivity.doRestart()
                                    }
                                },{
                                    Hawk.delete(Profile.SAVE_KEY)
                                    MainActivity.doRestart()
                                })
                        }
                    }
                    return response
                }

                private val token: String
                    private get() {
                        if (Hawk.contains(Profile.SAVE_KEY) && Hawk.get<Profile?>(Profile.SAVE_KEY) is Profile) {
                            val profile = Hawk.get(Profile.SAVE_KEY) as? Profile
                            return profile?.token?.let { it } ?: ""
                        }
                        return ""
                    }

                private val refreshToken: String
                    private get() {
                        if (Hawk.contains(Profile.SAVE_KEY) && Hawk.get<Profile?>(Profile.SAVE_KEY) is Profile) {
                            val profile = Hawk.get(Profile.SAVE_KEY) as? Profile
                            return profile?.refreshToken?.let { it } ?: ""
                        }
                        return ""
                    }

                private val userAgent: String
                    get() {
                        var userAgent = "Android "
                        val androidV = Build.VERSION.RELEASE
                        val appV = BuildConfig.VERSION_NAME
                        userAgent += "$androidV/$deviceName ; App/$appV"
                        return userAgent.trim { it <= ' ' }
                    }

                private val appVersionName: String
                    get() {
                        return BuildConfig.VERSION_NAME
                    }

                private val appVersionCode: String
                    get() {
                        return BuildConfig.VERSION_CODE.toString()
                    }

                private val deviceName: String
                    get() {
                        val manufacturer = Build.MANUFACTURER
                        val model = Build.MODEL
                        return if (model.startsWith(manufacturer)) {
                            model.capitalize(Locale.US)
                        } else {
                            manufacturer.capitalize(Locale.US) + " " + model
                        }
                    }
            })

            okHttpClient.dispatcher(dispatcher)
            okHttpClient.addInterceptor(logging)

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}