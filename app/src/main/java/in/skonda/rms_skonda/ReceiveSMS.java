package in.skonda.rms_skonda;

import android.app.ProgressDialog;
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

    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Status");
        progressDialog.setMessage("Validating OTP");
        progressDialog.show();
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        SmsMessage smsMessage[] = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        int otp_received = Integer.parseInt(smsMessage[0].getMessageBody().toString().substring(16));
        SharedPreferences sharedPreferences = context.getSharedPreferences("skonda", Context.MODE_PRIVATE);
        int otp = sharedPreferences.getInt("otp", 0);
        Toast.makeText(context, "both values: " + otp_received + otp, Toast.LENGTH_SHORT).show();
        if(otp_received == otp) {
            Toast.makeText(context, "OTP validation success", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "OTP validation failure", Toast.LENGTH_SHORT).show();
        progressDialog.hide();
    }
}
