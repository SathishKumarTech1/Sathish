package com.task.myapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser

import com.task.myapplication.models.BaseApiResponse
import com.task.myapplication.models.ErrorResponse
import com.task.myapplication.enums.LoaderStatus
import com.task.myapplication.managers.RetrofitManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONObject
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

open class MyBaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + rootJob

    protected val TAG: String = this.javaClass.simpleName

    var errorLiveData = MutableLiveData<String?>()

    val multiDeviceLoginErrorLiveData: MutableLiveData<String> = MutableLiveData<String>()
    val multiLoginErrorLiveData: MutableLiveData<String> = MutableLiveData<String>()
    val sessionExpiredErrorLiveData: MutableLiveData<String> = MutableLiveData<String>()


    var isLoading = MutableLiveData<LoaderStatus>()

    val rootJob = Job()

    // ...because this is what we'll want to expose
    val errorMediatorLiveData = MediatorLiveData<String?>()

    init {
        errorMediatorLiveData.addSource(errorLiveData) { result: String? ->
            result?.let {
                errorMediatorLiveData.value = result
            }
        }

    }

    protected val retrofitManager: RetrofitManager by lazy {
        RetrofitManager.getInstance(
            getApplication()
        )
    }


    protected val exceptionHandler: CoroutineContext =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            isLoading.postValue(LoaderStatus.failed)
            Log.e(TAG, throwable.message ?: "message not found")
            errorLiveData.postValue(throwable.message)
            throwable.printStackTrace()
        }

    protected fun getGsonObject(obj: JSONObject): JsonObject {
        val jsonParser = JsonParser()
        return jsonParser.parse(obj.toString()) as JsonObject
    }

    protected fun <T : Any> isResponseSuccess(response: Response<T>): Boolean {
        if (!response.isSuccessful) {
            isLoading.postValue(LoaderStatus.failed)
            if (response.errorBody() != null) {
                val jsonString = response.errorBody()!!.string()
                if (jsonString.contains("{")) {
                    val errorModel = ErrorResponse(jsonString)
                    errorLiveData.postValue(errorModel.errorMessage)
                } else {
                    errorLiveData.postValue(response.message())
                }
            } else if (!response.message().isEmpty())
                errorLiveData.postValue(response.message())

        }
        return response.isSuccessful
    }

    override fun onCleared() {
        super.onCleared()
        rootJob.cancel()
    }

    protected fun checkAndDisplayError(apiResponse: BaseApiResponse) {
        when (apiResponse.errorCode) {
            323 -> { //forceLogin - when user login on one more device
                apiResponse.message?.let {
                    forceLogin(it)
                }
            }
//            325 -> { //forceLogout - when user login on another device - the user on initial device should pushed to login
//                apiResponse.message?.let { forceLogout(it) }
//            }
            324 -> { //forceLogout - when session expired
                apiResponse.message?.let { sessionExpired(it) }
            }
            328 -> { //forceLogout - when session inactive
                apiResponse.message?.let { sessionExpired(it) }
            }
            else -> {
                errorLiveData.postValue(apiResponse.message)
            }
        }
    }

    private fun forceLogin(message: String) {
        multiDeviceLoginErrorLiveData.postValue(message)
    }

    private fun forceLogout(message: String) {
        multiLoginErrorLiveData.postValue(message)
    }

    private fun sessionExpired(message: String) {
        sessionExpiredErrorLiveData.postValue(message)
    }
}