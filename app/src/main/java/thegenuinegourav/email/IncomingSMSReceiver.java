package thegenuinegourav.email;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by shashikiranms on 13/03/18
 */

public class IncomingSMSReceiver extends BroadcastReceiver {
    public static final String TAG = IncomingSMSReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Bundle b = intent.getExtras();
        int slot = b.getInt("slot", -1);
        int sub = b.getInt("subscription", -1);
        System.out.println(slot  + " - " + sub);
        showToast(context, "Got the message for slot:" + slot);
        if(slot == 0){
            // sim1
        }
        if(slot == 0){
            // sim2
            try {
                if (b != null) {
                    Object[] pdusObj = (Object[]) b.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage currentMessage = SmsMessage
                                .createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage
                                .getDisplayOriginatingAddress();
                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();
                        String messageToBeForwarded =
                                "Message content: " + message + ".\nSMS from: " + senderNum
                                        + "\nMessage received at: " + (new Date().toString()) + "\n\nFrom Shashi Kiran";
                        showNotification(context, messageToBeForwarded, senderNum);
                        System.out.println("phone:" + senderNum + ",message: " + message);
                        sendEmail("pbalchandr@gmail.com","SMS from: " + senderNum, messageToBeForwarded, context);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Show notification
     * @param context
     */
    public void showNotification(Context context, String message, String from) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("whatsapp://send?text=" +
                message));

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);



        // Email pending intent
        Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.fromParts(
                "mailto","pbalchandr@gmail.com", null));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"pbalchandr@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SMS from: " + from);
        emailIntent.putExtra(Intent.EXTRA_TEXT   , message);
        PendingIntent emailPendingIntent = PendingIntent.getActivity(context, 0, emailIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Prepare a notification with vibration, sound and lights
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Message for sim2")
                .setContentText(message)
                .setLights(Color.RED, 1000, 1000)
                .setVibrate(new long[] { 0, 400, 250, 400 })
                .setAutoCancel(true)
                .addAction(R.drawable.ic_action_sms, "Whatsapp", pIntent)
                .addAction(R.drawable.ic_action_email, "E-Mail", emailPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .build();

        // hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;


        // Get an instance of the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(context.NOTIFICATION_SERVICE);


        // Build the notification and display it
        notificationManager.notify(1, notification);
    }

    void showToast(Context context, String message) {
        // Show Alert
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }


    private void sendEmail(String email, String subject, String message, Context context) {
        //Getting content for email

        //Creating SendMail object
        SendMail sm = new SendMail(context, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }
}