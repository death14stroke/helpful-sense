package com.andruid.magic.helpfulsense.util;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.andruid.magic.helpfulsense.model.Category;
import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.wafflecopter.multicontactpicker.ColorUtils;

public class DataBindingAdapter {
    @BindingAdapter("firstLetter")
    public static void firstLetter(RoundedLetterView roundedLetterView, String name) {
        roundedLetterView.setTitleText(name.substring(0, 1));
        roundedLetterView.setTitleSize(50f);
        int color = ColorUtils.getRandomMaterialColor();
        roundedLetterView.setBackgroundColor(color);
    }

    @BindingAdapter("category")
    public static void setCategory(TextView textView, Category category) {
        textView.setText(category.getName());
        textView.setTextColor(ContextCompat.getColor(textView.getContext(), category.getIconColor()));
        textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(
                textView.getContext(), category.getIcon()), null, null, null);
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null)
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(
                        textView.getContext(), category.getIconColor()), PorterDuff.Mode.SRC_IN));
        }
    }
}