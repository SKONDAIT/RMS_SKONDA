package in.skonda.rms_skonda;

import android.*;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import in.skonda.rms_skonda.dummy.DummyContent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReceiveSMS extends BroadcastReceiver {

    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(final Context context, Intent intent) {
        Toast.makeText(context, "message received in onreceive", Toast.LENGTH_SHORT).show();
        Log.d("skondad: ", "on receive method");
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Status");
        progressDialog.setMessage("Validating OTP");
//        progressDialog.show();
        SmsMessage smsMessage[] = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        int otp_received = Integer.parseInt(smsMessage[0].getMessageBody().toString().substring(16));
        SharedPreferences sharedPreferences = context.getSharedPreferences("skonda", Context.MODE_PRIVATE);
        int otp = sharedPreferences.getInt("otp", 0);
        if(otp_received == otp) {
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putBoolean("Authenticated", true);
            editor.commit();
            DeviceId deviceId = new DeviceId();
            deviceId.onTokenRefresh();
            Log.d("skondad: ", "actual de is: " + DummyContent.deviceId);
            String url = "http://ioca.in/rms/updateDeviceId.php?deviceId="
                    + DummyContent.deviceId
                    + "&mobileNumber="
                    + sharedPreferences.getString("mobileNumber","123");
            Log.d("skondae: ", "url is: " + url);
            Request request = new Request.Builder().url(url).build();
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("skondad: ", "Call Failed: " + e.getMessage() );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    if (result.indexOf("success") != -1)
                    {
                        Handler mainHandler = new Handler(Looper.getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Device Registerd", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }
                    response.body().close();
                }
            });
            Intent dashboard = new Intent(context, Dashboard.class);
            dashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(dashboard);
        }
        else
            Toast.makeText(context, "OTP validation failure", Toast.LENGTH_SHORT).show();
        progressDialog.hide();
    }
}
