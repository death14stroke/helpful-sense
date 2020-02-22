package com.andruid.magic.helpfulsense.model

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.andruid.magic.helpfulsense.database.entity.Action
import kotlinx.android.parcel.Parcelize

/**
 * Model class for category of the [Action] created by user
 * @property name category name
 * @property icon drawable of the icon
 * @property iconColor tint of the icon
 */
@Parcelize
data class Category(
        val name: String,
        @DrawableRes val icon: Int,
        @ColorRes val iconColor: Int
) : Parcelable