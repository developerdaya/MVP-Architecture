package com.developerdaya.mvp_android.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.developerdaya.mvp_android.R
import com.developerdaya.mvp_android.data.api.Employee
import com.developerdaya.mvp_android.data.api.RetrofitUtil
import com.developerdaya.mvp_android.presenter.EmployeePresenter
import com.developerdaya.mvp_android.ui.adapter.EmployeeAdapter
import com.developerdaya.mvp_android.ui.contract.EmployeeInterface

class EmployeeActivity : AppCompatActivity(), EmployeeInterface.View {
    private lateinit var presenter: EmployeePresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EmployeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EmployeeAdapter(emptyList())
        recyclerView.adapter = adapter
        val setUpRetrofitUtil = RetrofitUtil()
        presenter = EmployeePresenter(this, setUpRetrofitUtil)
        presenter.fetchEmployees()
    }

    override fun showLoading() {
        // Show loading progress (could be a progress bar)
    }

    override fun hideLoading() {
        // Hide loading progress
    }

    override fun showEmployees(employees: List<Employee>) {
        adapter.updateData(employees)
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }
}
