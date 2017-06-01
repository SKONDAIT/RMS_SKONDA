package in.skonda.rms_skonda;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// login activity - sudarsan.konda
// added interface OnKeyListener. .
public class Login extends AppCompatActivity implements TextWatcher {

    EditText mobile;
    String status = "success";
    String mobile_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mobile = (EditText) findViewById(R.id.mobile);
        mobile.addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mobile.getText().length() == 10) {
            mobile_number = mobile.getText().toString();
            String mob = mobile.getText().toString();
            String url = "http://ioca.in/rms/authenticate.php?mob=" + mob;
            Request request = new Request.Builder().url(url).build();

            OkHttpClient okHttpClient = new OkHttpClient();
            System.out.println("before call" + url);
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("skondad: ", "Call Failed: " + e.getMessage() );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("skondad: ", "response success? " + response.isSuccessful()
                            + response.message()   );
                    final String result = response.body().string();

                    if (result.indexOf("success") == -1)
                    {
                        System.out.println("success");
                        status = "failure";
                    }

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Login.this, "result is: " + status, Toast.LENGTH_SHORT).show();
                            if (status == "success")
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                    if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.SEND_SMS)
                                            != PackageManager.PERMISSION_GRANTED)
                                    {
                                        String[] permissions = {Manifest.permission.SEND_SMS};
                                        requestPermissions(permissions, 1);
                                    }
                                    else {
                                        sendMessage();
                                    }
                        }
                    });

                    response.body().close();
                }
            });
            Log.d("skondad: ", "after call");


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendMessage();
        }
        else {
            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    void sendMessage() {
        int otp = (int) Math.round(Math.random() * 1000000);
        SharedPreferences sharedPreferences = getSharedPreferences("skonda", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt("otp", otp);
        editor.commit();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mobile_number, null, "OTP by SKONDA - " + otp, null, null);
    }


}
