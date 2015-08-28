/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package santana.tebaktebakan;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import santana.tebaktebakan.activity.MainMenuActivity;

//import santana.tebaktebakan.activity.TebakanListActivity;

public class MyGcmListenerService extends GcmListenerService {

    public static final int NOTIFICATION_ID = 113;
    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
//        Log.d(TAG, "Data: " + data.toString());
//        Log.d(TAG, "Data1: " + data.getString(ApplicationConstants.UserNameFriend));
//        Log.d(TAG, "Data2: " + data.getString(ApplicationConstants.EmailUserFriend));
//        Log.d(TAG, "Data3: " + data.getString(ApplicationConstants.Id_Question));
//        Log.d(TAG, "Data4: " + data.getString(ApplicationConstants.Id_Chat));
//        Log.d(TAG, "Data5: " + data.getString(ApplicationConstants.From));
//        Log.d(TAG, "Data6: " + data.getString(ApplicationConstants.Id_Channel));
//        Log.d(TAG, "Data7: " + data.getString(ApplicationConstants.TextQuestion));
//
//        //set value and get value
//        NotificationObject notificationObject = new NotificationObject();
//        notificationObject.setUserName(data.getString(ApplicationConstants.UserName));
//        notificationObject.setEmailUser(data.getString(ApplicationConstants.EmailUserFriend));
//        notificationObject.setId_Question(data.getString(ApplicationConstants.Id_Question));
//        notificationObject.setId_Chat(data.getString(ApplicationConstants.Id_Chat));
//        notificationObject.setIdChannel(data.getString(ApplicationConstants.Id_Channel));
//        notificationObject.setQuestionText(data.getString(ApplicationConstants.TextQuestion));
//        notificationObject.setGender(data.getString(ApplicationConstants.Gender));
//        notificationObject.setEmoticon(data.getString(ApplicationConstants.ResourceEmoticon));
//        notificationObject.setMessage(data.getString(ApplicationConstants.Message));
//        notificationObject.setChatType(data.getString(ApplicationConstants.ChatType));

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */



        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification();

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     */
    private void sendNotification() {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(this, MainMenuActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = { 0, 100, 200, 300, 400 };

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Hey!")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("You got new Notification in Persona"))
                        .setContentText("You got new Notification in Persona")
                        .setSound(alarmSound)
                        .setVibrate(pattern)
                        .setDefaults(
                                Notification.DEFAULT_LIGHTS
                                        | Notification.FLAG_AUTO_CANCEL)
                        .setAutoCancel(true);


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
