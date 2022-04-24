package com.jyh.bookreview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class History(
    @PrimaryKey val uid:Int?,
    @ColumnInfo(name = "keyword") val keyword:String?
):Serializable