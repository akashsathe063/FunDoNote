package com.example.fundonote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.fundonote.model.Fields
import com.example.fundonote.model.MyApi
import com.example.fundonote.model.RetrofitRetrive

// class NotePagingSource(val myApi: MyApi):PagingSource<Int,Fields> {
//  override fun getRefreshKey(state: PagingState<Int, Fields>): Int? {
//   TODO("Not yet implemented")
//  }
//
//  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Fields> {
//   TODO("Not yet implemented")
//  }
// }