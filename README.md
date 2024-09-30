# MVP (Model-View-Presenter) Architecture

### 1. Define the MVP Contracts
We'll start by defining the contracts for the MVP architecture, which includes interfaces for the **View**, **Presenter**, and **Model**.

#### **MVP Contracts**
```kotlin
interface EmployeeContract {

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
        fun getEmployees(callback: (List<Employee>?, String?) -> Unit)
    }
}
```

### 2. Create the Model
The model will handle the API call using Retrofit to fetch the employee data.

#### **Model Implementation**
```kotlin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Employee(val name: String, val profile: String)

interface ApiService {
    @GET("v1/1a44a28a-7c86-4738-8a03-1eafeffe38c8")
    fun getEmployees(): Call<EmployeeResponse>
}

data class EmployeeResponse(val message: String, val employees: List<Employee>)

class EmployeeModel : EmployeeContract.Model {
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
```

### 3. Create the Presenter
The presenter will communicate between the View and the Model.

#### **Presenter Implementation**
```kotlin
class EmployeePresenter(private val view: EmployeeContract.View, private val model: EmployeeContract.Model) :
    EmployeeContract.Presenter {

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
```

### 4. Create the Activity (View)
The View will display the data in a RecyclerView.

#### **Activity Implementation**
```kotlin
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EmployeeActivity : AppCompatActivity(), EmployeeContract.View {

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

        val model = EmployeeModel()
        presenter = EmployeePresenter(this, model)
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
```

### 5. RecyclerView Adapter
The adapter will populate the data in the RecyclerView.

#### **Adapter Implementation**
```kotlin
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmployeeAdapter(private var employees: List<Employee>) :
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_employee, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = employees[position]
        holder.nameTextView.text = employee.name
        holder.profileTextView.text = employee.profile
    }

    override fun getItemCount(): Int = employees.size

    fun updateData(newEmployees: List<Employee>) {
        employees = newEmployees
        notifyDataSetChanged()
    }

    class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val profileTextView: TextView = itemView.findViewById(R.id.profileTextView)
    }
}
```

### 6. XML Layouts

#### **Activity Layout (`activity_employee.xml`)**
```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```

#### **RecyclerView Item Layout (`item_employee.xml`)**
```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:id="@+id/nameTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Name"
        android:textSize="18sp"
        android:textStyle="bold"/>

    <TextView
        android:id="@+id/profileTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Profile"
        android:textSize="16sp"/>
</LinearLayout>
```

### 7. Dependencies
Make sure to add the necessary dependencies in your `build.gradle` file for Retrofit and RecyclerView:

```gradle
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'androidx.recyclerview:recyclerview:1.2.1'
```

Here's a suggested folder structure for organizing your Android project that implements the MVP architecture with the provided API. This structure helps keep your code modular and maintainable.

### Suggested Folder Structure

```
YourProjectName/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── yourpackage/
│   │   │   │           ├── data/
│   │   │   │           │   ├── api/
│   │   │   │           │   │   ├── ApiService.kt
│   │   │   │           │   │   ├── EmployeeModel.kt
│   │   │   │           │   │   └── EmployeeResponse.kt
│   │   │   │           │   └── model/
│   │   │   │           │       └── Employee.kt
│   │   │   │           │
│   │   │   │           ├── ui/
│   │   │   │           │   ├── adapter/
│   │   │   │           │   │   └── EmployeeAdapter.kt
│   │   │   │           │   ├── view/
│   │   │   │           │   │   └── EmployeeActivity.kt
│   │   │   │           │   └── contract/
│   │   │   │           │       └── EmployeeContract.kt
│   │   │   │           │
│   │   │   │           └── presenter/
│   │   │   │               └── EmployeePresenter.kt
│   │   │   │
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       │   ├── activity_employee.xml
│   │   │       │   └── item_employee.xml
│   │   │       └── values/
│   │   │           └── strings.xml
│   │   │
│   │   └── AndroidManifest.xml
│   │
│   └── build.gradle
│
├── build.gradle
└── settings.gradle
```

### Explanation of the Structure

- **data/**: Contains the data-related classes.
  - **api/**: Contains the API service definitions and model implementations for network requests.
  - **model/**: Contains data classes that represent the data structure, like `Employee`.

- **ui/**: Contains UI-related classes.
  - **adapter/**: Contains RecyclerView adapter classes (like `EmployeeAdapter`).
  - **view/**: Contains Activities and Fragments (like `EmployeeActivity`).
  - **contract/**: Contains interfaces that define the contracts for MVP (like `EmployeeContract`).

- **presenter/**: Contains presenter classes that handle the business logic (like `EmployeePresenter`).

- **res/**: Contains resources for the app, including layout files and string resources.
  - **layout/**: Contains XML layout files for Activities and RecyclerView items.
  - **values/**: Contains other resource files like `strings.xml`.

### Gradle Files

- **build.gradle**: Contains dependencies for the app.
- **settings.gradle**: Contains project-level settings.

