package com.andruid.magic.helpfulsense.ui.util

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.andruid.magic.helpfulsense.model.Category
import com.github.pavlospt.roundedletterview.RoundedLetterView
import com.wafflecopter.multicontactpicker.ColorUtils

@BindingAdapter("firstLetter")
fun RoundedLetterView.firstLetter(name: String) {
    val color = ColorUtils.getRandomMaterialColor()
    apply {
        titleText = name.substring(0, 1)
        titleSize = 50f
        backgroundColor = color
    }
}

@BindingAdapter("category")
fun TextView.setCategory(category: Category) {
    text = category.name
    setTextColor(ContextCompat.getColor(context, category.iconColor))
    setCompoundDrawablesWithIntrinsicBounds(context.drawable(category.icon), null, null, null)
    for (drawable in compoundDrawables)
        drawable?.colorFilter = PorterDuffColorFilter(context.color(category.iconColor), PorterDuff.Mode.SRC_IN)
}