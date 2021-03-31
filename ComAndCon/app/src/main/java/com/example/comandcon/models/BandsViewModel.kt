package com.example.comandcon.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.net.HttpURLConnection

class BandsViewModel : ViewModel() {

    companion object {
        var ENDPOINT = "https://wherever.ch/hslu/rock-bands/"
    }

    val bands: MutableLiveData<List<BandCode>> = MutableLiveData()
    val currentBand : MutableLiveData<BandInfo?> = MutableLiveData()

    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().build())
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(ENDPOINT)
        .build()

    private val bandService = retrofit.create(BandApiService::class.java)

    fun getBands(): LiveData<List<BandCode>> {
        return bands
    }

    fun getCurrentBand(): LiveData<BandInfo?> {
        return currentBand
    }

    fun reset() {
        currentBand.value = null

    }

    fun requestBandCodeFromEndpoint() {
        val callList = bandService.getBandNames()
        callList.enqueue(object : Callback<List<BandCode>> {
            override fun onResponse(call: Call<List<BandCode>>, response: Response<List<BandCode>>) {
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    bands.value = response.body().orEmpty()
                }
            }

            override fun onFailure(call: Call<List<BandCode>>, t: Throwable) {
                Log.w("Request", "The request for the band codes failed")
            }
        })
    }

    fun requestBandInfoFromEndpoint(code: String) {
        val bandInfo = bandService.getBandInfo(code)
        bandInfo.enqueue(object : Callback<BandInfo> {
            override fun onResponse(call: Call<BandInfo>, response: Response<BandInfo>) {
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    currentBand.value = response.body()
                }
            }

            override fun onFailure(call: Call<BandInfo>, t: Throwable) {
                Log.w("Request", "The request for the band info failed")
            }
        })
    }


}

data class BandCode(
    val name: String,
    val code: String
)

data class BandInfo(
    val name: String,
    val members: List<String>,
    val foundingYear: Int,
    val homeCountry: String,
    val bestOfCdCoverImageUrl: String?
)

interface BandApiService {
    @GET("all.json")
    fun getBandNames(): Call<List<BandCode>>

    @GET("info/{code}.json")
    fun getBandInfo(@Path("code") code: String): Call<BandInfo>
}
