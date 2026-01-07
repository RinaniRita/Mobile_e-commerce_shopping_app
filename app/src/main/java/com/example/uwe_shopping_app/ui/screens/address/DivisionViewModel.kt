package com.example.uwe_shopping_app.ui.screens.address

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.remote.District
import com.example.uwe_shopping_app.data.remote.DivisionApiService
import com.example.uwe_shopping_app.data.remote.Province
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DivisionViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://provinces.open-api.vn/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(DivisionApiService::class.java)

    var provinces by mutableStateOf<List<Province>>(emptyList())
        private set
    var districts by mutableStateOf<List<District>>(emptyList())
        private set
    var isLoadingProvinces by mutableStateOf(false)
        private set
    var isLoadingDistricts by mutableStateOf(false)
        private set

    init {
        loadProvinces()
    }

    private fun loadProvinces() {
        viewModelScope.launch {
            isLoadingProvinces = true
            try {
                provinces = apiService.getProvinces()
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoadingProvinces = false
            }
        }
    }

    fun loadDistricts(provinceName: String) {
        val province = provinces.find { it.name == provinceName } ?: return
        viewModelScope.launch {
            isLoadingDistricts = true
            try {
                val result = apiService.getProvinceWithDistricts(province.code)
                districts = result.districts
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoadingDistricts = false
            }
        }
    }
}
