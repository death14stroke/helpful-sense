package com.andruid.magic.helpfulsense.util

import android.Manifest
import android.content.Context
import android.telephony.SubscriptionInfo
import androidx.annotation.RequiresPermission
import androidx.core.content.res.use
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.model.Category
import splitties.systemservices.subscriptionManager

/**
 * Get all categories from resources folder
 * @return list of categories
 * @receiver context of the calling component
 */
fun Context.getCategoryFromRes(): List<Category> {
    val categoryNames = resources.getStringArray(R.array.category_names)
    var typedArray = resources.obtainTypedArray(R.array.category_icons)
    val icons = IntArray(typedArray.length())
    typedArray.use {
        for (i in 0 until typedArray.length())
            icons[i] = typedArray.getResourceId(i, -1)
    }
    typedArray = resources.obtainTypedArray(R.array.category_colors)
    val colors = IntArray(typedArray.length())
    typedArray.use {
        for (i in 0 until typedArray.length())
            colors[i] = typedArray.getResourceId(i, -1)
    }
    val categories = mutableListOf<Category>()
    for (i in icons.indices)
        categories.add(Category(categoryNames[i], icons[i], colors[i]))
    return categories.toList()
}

/**
 * Get all SIM info of the device as [SubscriptionInfo] objects
 * @return list of subscriptionInfo objects
 */
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun getSimCards(): List<SubscriptionInfo> = subscriptionManager.activeSubscriptionInfoList