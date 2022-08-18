package com.task.myapplication.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.task.myapplication.managers.retrofit.RetrofitService
import com.task.myapplication.models.gson.Data
import java.lang.Exception

class UserPagingSource(private val apiService: RetrofitService): PagingSource<Int, Data>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {

        return try {
            val position = params.key ?: 1
            val response = apiService.getUsers(position)
            LoadResult.Page(data = response.body()!!.data, prevKey = if (position == 1) null else position - 1,
                nextKey = position + 1)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
