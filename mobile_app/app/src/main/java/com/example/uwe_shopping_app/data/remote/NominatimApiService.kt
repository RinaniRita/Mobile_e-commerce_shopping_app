package com.example.uwe_shopping_app.data.remote

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

data class NominatimResponse(
    val lat: String,
    val lon: String,
    val display_name: String
)

interface NominatimApiService {
    @Headers("User-Agent: UweShoppingApp/1.0")
    @GET("search")
    suspend fun searchAddress(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 1,
        @Query("countrycodes") country: String = "vn"
    ): List<NominatimResponse>
}
