package com.andruid.magic.helpfulsense.ui.util

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.andruid.magic.helpfulsense.model.Category
import com.andruid.magic.eezetensions.color
import com.andruid.magic.eezetensions.drawable
import com.github.pavlospt.roundedletterview.RoundedLetterView
import com.wafflecopter.multicontactpicker.ColorUtils

/**
 * Set first letter of the string with random color as icon in textView
 * @param name input string
 * @receiver rounded letter textView
 */
@BindingAdapter("firstLetter")
fun RoundedLetterView.firstLetter(name: String) {
    val color = ColorUtils.getRandomMaterialColor()
    apply {
        titleText = name.substring(0, 1)
        titleSize = 50f
        backgroundColor = color
    }
}

/**
 * Set drawable resource for textView based on [Category]
 * @param category type of the action
 * @receiver textView
 */
@BindingAdapter("category")
fun TextView.setCategory(category: Category) {
    text = category.name
    setTextColor(context.color(category.iconColor))
    setCompoundDrawablesWithIntrinsicBounds(context.drawable(category.icon), null, null, null)
    for (drawable in compoundDrawables)
        drawable?.colorFilter = PorterDuffColorFilter(context.color(category.iconColor), PorterDuff.Mode.SRC_IN)
}