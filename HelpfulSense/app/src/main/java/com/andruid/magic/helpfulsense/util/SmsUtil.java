package com.andruid.magic.helpfulsense.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.andruid.magic.helpfulsense.service.SensorService;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.RxContacts.PhoneNumber;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.andruid.magic.helpfulsense.data.Constants.INTENT_SMS_SENT;

public class SmsUtil {

    private static String getMapsUrl(Location loc){
        return " http://maps.google.com/?q=" + loc.getLatitude() + "," + loc.getLongitude();
    }

    public static void sendSMS(Context context, Location location, String message){
        message = message + getMapsUrl(location);
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        List<ContactResult> contacts = FileUtil.readContactsFromFile(context);
        Intent intent = new Intent(context, SensorService.class);
        intent.setAction(INTENT_SMS_SENT);
        PendingIntent sentIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            sentIntent= PendingIntent.getForegroundService(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        else
            sentIntent= PendingIntent.getService(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        ArrayList<PendingIntent> pendingIntents = new ArrayList<>();
        pendingIntents.add(sentIntent);
        Timber.d("before sending sms");
        for(ContactResult contact : contacts){
            List<PhoneNumber> phoneNumbers = contact.getPhoneNumbers();
            for(PhoneNumber phone : phoneNumbers){
                smsManager.sendMultipartTextMessage(phone.getNumber(), null, parts,
                        pendingIntents, null);
            }
        }
    }

    public static void sendBootSms(Context context, String message){
        SmsManager smsManager = SmsManager.getDefault();
        List<ContactResult> contacts = FileUtil.readContactsFromFile(context);
        for(ContactResult contact : contacts){
            List<PhoneNumber> phoneNumbers = contact.getPhoneNumbers();
            for(PhoneNumber phone : phoneNumbers){
                smsManager.sendTextMessage(phone.getNumber(), null, message, null,
                        null);
            }
        }
        Timber.d("sendBootSms method end");
    }
}