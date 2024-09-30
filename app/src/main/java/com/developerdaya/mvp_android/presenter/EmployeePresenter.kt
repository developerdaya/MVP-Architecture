package com.developerdaya.mvp_android.presenter
import com.developerdaya.mvp_android.ui.contract.EmployeeInterface
class EmployeePresenter(private val view: EmployeeInterface.View, private val model: EmployeeInterface.Model) :
    EmployeeInterface.Presenter {
    override fun fetchEmployees() {
        view.showLoading()
        model.getEmployees { employees, error ->
            view.hideLoading()
            if (employees != null) {
                view.showEmployees(employees)
            } else if (error != null) {
                view.showError(error)
            }
        }
    }
}
