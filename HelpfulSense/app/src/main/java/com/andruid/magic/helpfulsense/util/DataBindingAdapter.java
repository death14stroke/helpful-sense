package com.andruid.magic.helpfulsense.util;

import android.graphics.Color;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.model.Category;
import com.github.pavlospt.roundedletterview.RoundedLetterView;

import java.util.Random;

import timber.log.Timber;

public class DataBindingAdapter {
    @BindingAdapter("firstLetter")
    public static void firstLetter(RoundedLetterView roundedLetterView, String name){
        roundedLetterView.setTitleText(name.substring(0, 1));
        roundedLetterView.setTitleSize(50f);
        Random rand = new Random();
        int color = Color.argb(255, rand.nextInt(256), rand.nextInt(256),
                rand.nextInt(256));
        roundedLetterView.setBackgroundColor(color);
    }

    @BindingAdapter("category")
    public static void setCategory(TextView textView, Category category){
        textView.setText(category.getName());
        switch (category.getBgColor()){
            case R.color.colorTab1:
                Timber.tag("bindlog").d("color tab 1");
                break;
            case R.color.colorTab2:
                Timber.tag("bindlog").d("color tab 2");
                break;
            case R.color.colorTab3:
                Timber.tag("bindlog").d("color tab 3");
                break;
            default:
                Timber.tag("bindlog").d("no id match");
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(textView.getContext(),
                category.getIcon()), null, null, null);
        textView.setBackgroundColor(ContextCompat.getColor(textView.getContext(),
                category.getBgColor()));
    }
}