package com.developerdaya.mvp_android.data.api
import com.developerdaya.mvp_android.data.model.EmployeeResponse
import retrofit2.Call
import retrofit2.http.GET

data class Employee(val name: String, val profile: String)
interface ApiService {
    @GET("v1/1a44a28a-7c86-4738-8a03-1eafeffe38c8")
    fun getEmployees(): Call<EmployeeResponse>
}
