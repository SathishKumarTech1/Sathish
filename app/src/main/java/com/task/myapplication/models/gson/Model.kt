package com.task.myapplication.models.gson

const val NETWORK_PAGE_SIZE = 6

data class UserResponse(val page: Int, val per_page: Int, val total: Int, val total_pages: Int, val data: List<Data>)

data class Data(val id: Int, val email: String, val first_name: String,val last_name: String,val avatar: String,)