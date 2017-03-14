package com.example.svatts.my22;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    StringBuilder sb=new StringBuilder("");
    private String messageTxt;
    private String phoneNum;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();

        Log.d("debugggg","yoooo");
        if (intentExtras != null) {
            /* Get Messages */
            Object[] sms = (Object[]) intentExtras.get("pdus");

            for (int i = 0; i < sms.length; ++i) {
                /* Parse Each Message */
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                 phoneNum = smsMessage.getOriginatingAddress();
                 messageTxt = smsMessage.getMessageBody().toString();
                sb.append(messageTxt+"||\n");

               // Toast.makeText(context, phoneNum + ": " + messageTxt, Toast.LENGTH_LONG).show();
            }
            Toast.makeText(context,"FINAL TOAST"+ phoneNum + ": " + sb, Toast.LENGTH_LONG).show();
            Transaction t = new Transaction(context);
            t.msg=sb.toString();
                fun(t,context);

        }

    }

    public static void fun(Transaction tr, Context context){

        tr.call(context);

    }
}
