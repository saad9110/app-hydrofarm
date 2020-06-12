package com.example.hydrofarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;

public class NotifactionService extends FirebaseMessagingService {
    public NotifactionService() {
    }
    String notificationTitle = null, notificationBody = null;
    String dataTitle = null, dataMessage = null;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
        Log.d("push",remoteMessage.getData().get("title"));

        Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("message"));
        dataTitle = remoteMessage.getData().get("title");
        dataMessage = remoteMessage.getData().get("message");


        if (remoteMessage.getNotification() != null)
        {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notificationTitle = remoteMessage.getData().get("title");
            notificationBody = remoteMessage.getData().get("message");
        }
        sendNotification(notificationTitle, notificationBody, dataTitle, dataMessage);

    }
    private void sendNotification(String notificationTitle, String notificationBody, String dataTitle, String dataMessage)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("title", dataTitle);
        intent.putExtra("message", dataMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(dataTitle)
                .setContentText(dataMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(1)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
    }
}
