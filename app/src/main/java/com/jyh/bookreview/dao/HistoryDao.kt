package com.jyh.bookreview.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jyh.bookreview.model.History

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getAll() : List<History>

    @Insert
    fun insertHistory(history:History)

    @Query("DELETE FROM history WHERE keyword==:keyword")
    fun delete(keyword:String)
}