package in.skonda.rms_skonda;

import android.Manifest;
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
        System.out.print("before changed");
        Log.d("skondad", "thie is before text changed");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        System.out.print("on changed");
        Log.d("skondad", "thie is on text changed");
    }

    @Override
    public void afterTextChanged(Editable s) {
        System.out.print("after changed");
        Log.d("skondad", "thie is after text changed");
        Log.d("skondad", "" + mobile.getText().length() );
        if (mobile.getText().length() == 10) {
            String mob = mobile.getText().toString();
            Log.d("skondad", "" + mobile.getText().toString());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED)
            {
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, 1);
            }
            else {
                Toast.makeText(this, "sending sms", Toast.LENGTH_SHORT).show();
                sendMessage();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "granted permission", Toast.LENGTH_SHORT).show();
            sendMessage();
        }
        else {
            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    void sendMessage() {
        long otp = Math.round(Math.random() * 1000000);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("7702571000", null, "OTP by SKONDA - " + otp, null, null);
    }
}
