package com.sesingkat.githubuserappsub3

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val avatar: String? = "",
    val username: String? = "",
    val name: String? = "",
    val location: String? = "",
    val company: String? = "",
    val repository: String? = "",
    val followers: String? = "",
    val following: String? = "",
    val isFav: String? = ""
) : Parcelable