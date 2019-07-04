package com.andruid.magic.helpfulsense.data;

public class Constants {
    public static final int NO_OF_TABS = 4, POS_ALERT = 0, POS_MESSAGE = 1, POS_CONTACTS = 2,
            POS_SETTINGS = 3;
    public static final String FILE_CONTACTS = "contacts.json", FILE_ACTIONS = "actions.json";
    public static final String ACTION_ADD = "action_add", ACTION_EDIT = "action_edit",
            ACTION_SMS = "action_sms", ACTION_SWIPE = "action_swipe";
    public  static final String INTENT_LOC_SMS = "intent_loc_sms",
            INTENT_SERVICE_STOP = "intent_service_stop", INTENT_SMS_SENT = "intent_sms_sent";
    public static final String CHANNEL_ID = "channel_sensor", CHANNEL_NAME = "Sensor Service";
    public static final String KEY_MESSAGE = "key_message", KEY_ACTION = "key_action",
            KEY_COMMAND = "key_command";
    public static final int CONTACTS_PICKER_REQUEST = 0, MAX_CONTACTS = 5, NOTI_ID = 1,
            SHAKE_THRESHOLD = 15;
}