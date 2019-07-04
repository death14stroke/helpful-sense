package com.wafflecopter.multicontactpicker.RxContacts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

class ColumnMapper {

    private ColumnMapper () {}

    static void mapInVisibleGroup (Cursor cursor, Contact contact, int columnIndex) {
        contact.setInVisibleGroup(cursor.getInt(columnIndex));
    }

    static void mapDisplayName (Cursor cursor, Contact contact, int columnIndex) {
        String displayName = cursor.getString(columnIndex);
        if (displayName != null && !displayName.isEmpty())
            contact.setDisplayName(displayName);
    }

    static void mapEmail (Cursor cursor, Contact contact, int columnIndex) {
        String email = cursor.getString(columnIndex);
        if (email != null && !email.trim().isEmpty())
            contact.getEmails().add(email);
    }

    static void mapPhoneNumber (Context con, Cursor cursor, Contact contact, int noColumnIndex, int typeColIndex, int labelColIndex) {
        String phoneNumber = cursor.getString(noColumnIndex);
        int phonetype = cursor.getInt(typeColIndex);
        String customLabel = cursor.getString(labelColIndex);
        String phoneLabel = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(con.getResources(), phonetype, customLabel);
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Remove all whitespaces
            phoneNumber = phoneNumber.replaceAll("\\s+","");
            contact.getPhoneNumbers().add(new PhoneNumber(phoneLabel, phoneNumber.trim()));
        }
    }

    static void mapPhoto (Cursor cursor, Contact contact, int columnIndex) {
        String uri = cursor.getString(columnIndex);
        if (uri != null && !uri.isEmpty())
            contact.setPhoto(Uri.parse(uri));
    }

    static void mapStarred (Cursor cursor, Contact contact, int columnIndex) {
        contact.setStarred(cursor.getInt(columnIndex) != 0);
    }

    static void mapThumbnail (Cursor cursor, Contact contact, int columnIndex) {
        String uri = cursor.getString(columnIndex);
        if (uri != null && !uri.isEmpty())
            contact.setThumbnail( Uri.parse(uri));
    }
}