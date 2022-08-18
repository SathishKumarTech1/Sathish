package com.task.myapplication.models

import org.json.JSONObject

class ErrorResponse(jsonString: String) {
    var status: Boolean? = null
    var errorCode: Int? = null
    var errorMessage: String? = null

    init {
        val jsonObject = JSONObject(jsonString)

        if (jsonObject.has("status"))
            status = jsonObject.getBoolean("status")
        if (jsonObject.has("errorCode"))
            errorCode = jsonObject.getInt("errorCode")
        if (jsonObject.has("message"))
            errorMessage = jsonObject.getString("errorMessage")
    }
}