package com.task.myapplication.viewmodels.launch

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.task.myapplication.enums.LoaderStatus
import com.task.myapplication.viewmodels.MyBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : MyBaseViewModel(application) {
    val loginLiveData: MutableLiveData<String> = MutableLiveData<String>()

    fun login(email: String, password: String) {
        isLoading.postValue(LoaderStatus.loading)

        CoroutineScope(exceptionHandler).launch {

            val request = retrofitManager.getAuthApi().login(email,password)
            val response = request.await()

            if (isResponseSuccess(response)) {
                val apiResponse = response.body()!!

                if (apiResponse.page != 0) {
                    Log.d("Login Response: ", "Success")
                    loginLiveData.postValue("Success")
                } else {
                    checkAndDisplayError(apiResponse)
                }
                isLoading.postValue(LoaderStatus.success)
            } else {
                response.body()?.let { checkAndDisplayError(it) }
            }
        }
    }


}