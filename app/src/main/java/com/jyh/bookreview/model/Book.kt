package com.jyh.bookreview.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    @SerializedName("itemId")val id:Long,
    @SerializedName("title")val title:String,
    @SerializedName("description")val description:String,
    @SerializedName("coverSmallUrl")val coverSmallUrl:String
) : Parcelable