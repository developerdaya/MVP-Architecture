package com.developerdaya.mvp_android.ui.contract
import com.developerdaya.mvp_android.data.api.Employee
interface EmployeeInterface {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showEmployees(employees: List<Employee>)
        fun showError(error: String)
    }
    interface Presenter {
        fun fetchEmployees()
    }
    interface Model {
        fun getEmployees(callback: (employees: List<Employee>?, error: String?) -> Unit)
    }
}
