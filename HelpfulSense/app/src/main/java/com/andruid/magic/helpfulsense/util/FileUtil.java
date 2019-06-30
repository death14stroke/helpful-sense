package com.andruid.magic.helpfulsense.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wafflecopter.multicontactpicker.ContactResult;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static com.andruid.magic.helpfulsense.data.Constants.FILE_CONTACTS;

public class FileUtil {
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

    public static List<ContactResult> readContactsFromFile(Context context){
        List<ContactResult> results = null;
        try{
            File file = new File(context.getFilesDir(), FILE_CONTACTS);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<ContactResult>>(){}.getType();
            results = gson.fromJson(new FileReader(file), listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }
}