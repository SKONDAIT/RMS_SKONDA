package in.skonda.rms_skonda;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class ReceiveSMS extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        SmsMessage smsMessage[] = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        int otpRec = Integer.parseInt(smsMessage[0].getMessageBody().toString().substring(16));
        SharedPreferences sharedPreferences = context.getSharedPreferences("skonda", Context.MODE_PRIVATE);
        int otp = sharedPreferences.getInt("otp", 0);
        Toast.makeText(context, "both values: " + otpRec + otp, Toast.LENGTH_SHORT).show();
    }
}
