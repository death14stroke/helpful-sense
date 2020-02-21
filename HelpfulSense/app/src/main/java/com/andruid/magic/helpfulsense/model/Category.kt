package com.andruid.magic.helpfulsense.model

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
        val name: String,
        @DrawableRes
        val icon: Int,
        @ColorRes
        val iconColor: Int
) : Parcelable