package com.developerdaya.mvp_android.data.api

import com.developerdaya.mvp_android.data.model.EmployeeResponse
import com.developerdaya.mvp_android.ui.contract.EmployeeInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitUtil : EmployeeInterface.Model {
    private val apiService: ApiService
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://mocki.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    override fun getEmployees(callback: (List<Employee>?, String?) -> Unit) {
        apiService.getEmployees().enqueue(object : Callback<EmployeeResponse> {
            override fun onResponse(call: Call<EmployeeResponse>, response: Response<EmployeeResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.employees, null)
                } else {
                    callback(null, "Failed to fetch data")
                }
            }

            override fun onFailure(call: Call<EmployeeResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }
}
