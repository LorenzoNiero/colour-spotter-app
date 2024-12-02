package com.challenge.colour_spotter.network.data

import com.challenge.colour_spotter.network.BuildConfig
import com.challenge.colour_spotter.network.api.ColorApiService
import com.challenge.colour_spotter.network.data.model.ColorResponse
import com.squareup.moshi.Moshi
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

private const val API_BASE_URL = BuildConfig.BASE_URL

class RetrofitNetwork @Inject constructor(
    okhttpCallFactory: Call.Factory,
    moshi : Moshi
) : NetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            MoshiConverterFactory.create(
                moshi
            )
        )
        .build()

    private val colorApi = networkApi.create(ColorApiService::class.java)

    override suspend fun fetchColorInfo(hex: String): ColorResponse = colorApi.getColorDescription(hex = hex)
}