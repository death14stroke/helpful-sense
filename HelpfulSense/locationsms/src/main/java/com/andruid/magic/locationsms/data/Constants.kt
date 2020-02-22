package com.andruid.magic.locationsms.data

import com.andruid.magic.locationsms.service.SmsService

/** Intent action when SMS is sent successfully */
const val ACTION_SMS_SENT = "com.andruid.magic.locationsms.SMS_SENT"
/** Intent action to send SMS with location
 * @see EXTRA_MESSAGE
 * @see EXTRA_PHONE_NUMBERS
 * @see EXTRA_CLASS
 * @see EXTRA_ICON_RES */
const val ACTION_LOC_SMS = "com.andruid.magic.locationsms.LOCATION_SMS"
/** Intent action to stop [SmsService] */
const val ACTION_STOP_SERVICE = "com.andruid.magic.locationsms.STOP_SERVICE"
/** Intent action to start [SmsService]
 * @see EXTRA_PHONE_NUMBERS */
const val ACTION_START_SERVICE = "com.andruid.magic.locationsms.START_SERVICE"

/** Message to be sent with location */
const val EXTRA_MESSAGE = "MESSAGE"
/** Selected phone numbers */
const val EXTRA_PHONE_NUMBERS = "PHONE_NUMBERS"
/** Activity to launch on notification click */
const val EXTRA_CLASS = "CLASS"
/** Small icon shown in notification */
const val EXTRA_ICON_RES = "ICON_RES"