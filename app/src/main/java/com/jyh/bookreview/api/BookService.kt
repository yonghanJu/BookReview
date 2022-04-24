package com.jyh.bookreview.api

import com.jyh.bookreview.model.BestSellerDTO
import com.jyh.bookreview.model.SearchBookDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey:String,
        @Query("query") keyword:String
    ) : Call<SearchBookDTO>

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSeller(
        @Query("key") apiKey:String
    ):Call<BestSellerDTO>
}