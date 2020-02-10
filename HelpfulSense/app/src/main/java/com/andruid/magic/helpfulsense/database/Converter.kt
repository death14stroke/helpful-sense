package com.andruid.magic.helpfulsense.database

import androidx.room.TypeConverter
import com.andruid.magic.helpfulsense.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wafflecopter.multicontactpicker.RxContacts.PhoneNumber

object Converter {
    @JvmStatic
    @TypeConverter
    fun catToString(category: Category): String = Gson().toJson(category)

    @JvmStatic
    @TypeConverter
    fun stringToCat(cat: String): Category = Gson().fromJson(cat, Category::class.java)

    @JvmStatic
    @TypeConverter
    fun numbersToString(phoneNumbers: List<PhoneNumber>): String = Gson().toJson(phoneNumbers)

    @JvmStatic
    @TypeConverter
    fun stringToNumbers(numbers: String): List<PhoneNumber> =
            Gson().fromJson(numbers, object : TypeToken<List<PhoneNumber>>() {}.type)
}