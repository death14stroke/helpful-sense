package com.andruid.magic.helpfulsense.util;

import androidx.databinding.BindingAdapter;

import com.github.pavlospt.roundedletterview.RoundedLetterView;

public class DataBindingAdapter {
    @BindingAdapter("firstLetter")
    public static void firstLetter(RoundedLetterView roundedLetterView, String name){
        roundedLetterView.setTitleText(name.substring(0, 1));
        roundedLetterView.setTitleSize(50f);
    }
}