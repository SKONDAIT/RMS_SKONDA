package in.skonda.rms_skonda;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
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

// login activity - sudarsan.konda
// added interface OnKeyListener. .
public class Login extends AppCompatActivity implements TextWatcher {

    EditText mobile;

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
            String mob = mobile.getText().toString();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED)
            {
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, 1);
            }
            else {
                sendMessage();
            }
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
        smsManager.sendTextMessage("7702571000", null, "OTP by SKONDA - " + otp, null, null);
    }
}
