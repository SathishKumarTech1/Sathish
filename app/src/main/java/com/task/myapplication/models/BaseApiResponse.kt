package com.task.myapplication.models


open class BaseApiResponse {
    var pagestatus : Boolean ?= null
    var errorCode : Int ?= null
    var page : Int ?= null
    var errorMessage : String ?= null
    var successMessage : String ?= null
    var message : String ?= null
}