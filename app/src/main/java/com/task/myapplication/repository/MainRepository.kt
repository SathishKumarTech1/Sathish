package com.task.myapplication.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.task.myapplication.managers.retrofit.RetrofitService
import com.task.myapplication.models.gson.Data
import com.task.myapplication.models.gson.NETWORK_PAGE_SIZE
import com.task.myapplication.paging.UserPagingSource

class MainRepository constructor(private val retrofitService: RetrofitService) {

    fun getUsers(): LiveData<PagingData<Data>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 1
            ),
            pagingSourceFactory = {
                UserPagingSource(retrofitService)
            }
        , initialKey = 1
        ).liveData
    }

}