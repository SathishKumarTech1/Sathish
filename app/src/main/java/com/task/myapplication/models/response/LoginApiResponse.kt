package com.task.myapplication.models.response

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.task.myapplication.models.BaseApiResponse
import java.lang.reflect.Type

//class do - LoginApiResponse
class LoginApiResponse : BaseApiResponse() {

    class LoginApiDeserializer : JsonDeserializer<LoginApiResponse> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): LoginApiResponse {
            val loginApi = LoginApiResponse()
            val jsonObject = json!!.asJsonObject

            if (jsonObject.has("page")) {
                loginApi.message = "Success"

            }else {
                loginApi.message = "Login Failed"
            }
            return loginApi
        }

    }

}