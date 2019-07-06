package com.andruid.magic.helpfulsense.util;

import android.content.Context;
import android.content.res.TypedArray;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.model.Action;
import com.andruid.magic.helpfulsense.model.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wafflecopter.multicontactpicker.ContactResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String FILE_CONTACTS = "contacts.json", FILE_ACTIONS = "actions.json";

    public static List<ContactResult> readContactsFromFile(Context context){
        List<ContactResult> results = new ArrayList<>();
        try{
            File file = new File(context.getFilesDir(), FILE_CONTACTS);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<ContactResult>>(){}.getType();
            results = gson.fromJson(new FileReader(file), listType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void writeContactsToFile(Context context, List<ContactResult> results){
        Gson gson = new Gson();
        String json = gson.toJson(results);
        File file = new File(context.getFilesDir(), FILE_CONTACTS);
        try{
            FileWriter out = new FileWriter(file);
            out.write(json);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<Action> readActionsFromFile(Context context){
        List<Action> actions = new ArrayList<>();
        try{
            File file = new File(context.getFilesDir(), FILE_ACTIONS);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Action>>(){}.getType();
            actions = gson.fromJson(new FileReader(file), listType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return actions;
    }

    public static void writeActionsToFile(Context context, List<Action> actions){
        Gson gson = new Gson();
        String json = gson.toJson(actions);
        File file = new File(context.getFilesDir(), FILE_ACTIONS);
        try{
            FileWriter out = new FileWriter(file);
            out.write(json);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Category> getCategoryFromRes(Context context){
        String[] categoryNames = context.getResources().getStringArray(R.array.category_names);
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.category_icons);
        int[] icons = new int[typedArray.length()];
        for(int i=0; i<typedArray.length(); i++)
            icons[i] = typedArray.getResourceId(i, -1);
        typedArray.recycle();
        typedArray = context.getResources().obtainTypedArray(R.array.category_colors);
        int[] colors = new int[typedArray.length()];
        for(int i=0; i<typedArray.length(); i++)
            colors[i] = typedArray.getResourceId(i, -1);
        typedArray.recycle();
        List<Category> categories = new ArrayList<>();
        for(int i=0; i<icons.length; i++)
            categories.add(new Category(categoryNames[i], icons[i], colors[i]));
        return categories;
    }
}