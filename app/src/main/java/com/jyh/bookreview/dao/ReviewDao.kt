package com.jyh.bookreview.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jyh.bookreview.model.Review

@Dao
interface ReviewDao {

    @Query("select * from review where id ==:id")
    fun getOneReview(id:Int): Review?

    // 대체 삽입
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review:Review)
}