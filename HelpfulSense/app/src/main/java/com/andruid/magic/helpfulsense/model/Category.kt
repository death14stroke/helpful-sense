package com.andruid.magic.helpfulsense.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
        val name: String,
        val icon: Int,
        val iconColor: Int
) : Parcelable