package com.example.uwe_shopping_app.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class Province(
    val name: String,
    val code: Int,
    val division_type: String,
    val codename: String,
    val phone_code: Int,
    val districts: List<District> = emptyList()
)

data class District(
    val name: String,
    val code: Int,
    val division_type: String,
    val codename: String,
    val province_code: Int
)

interface DivisionApiService {
    @GET("p/")
    suspend fun getProvinces(): List<Province>

    @GET("p/{code}")
    suspend fun getProvinceWithDistricts(
        @Path("code") provinceCode: Int,
        @Query("depth") depth: Int = 2
    ): Province
}
