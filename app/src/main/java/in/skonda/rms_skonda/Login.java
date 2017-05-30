package in.skonda.rms_skonda;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

// login activity - sudarsan.konda
public class Login extends AppCompatActivity implements TextWatcher {

    EditText mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int i;
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

        Log.d("skondad", "thie is after text changed");
        Log.d("skondad", "" + mobile.getText().length() );
        if (mobile.getText().length() == 10) {
            SmsManager smsManager = SmsManager.getDefault();
            double rand = Math.random();
            long otp = Math.round(rand * 1000000);
            smsManager.sendTextMessage(mobile.getText().toString(), null, "OTP - " + otp, null, null );
            Toast.makeText(this, "number received: " + mobile.getText().toString(), Toast.LENGTH_SHORT).show();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "no permissions", Toast.LENGTH_SHORT).show();

            Log.d("skondad", "again: " + mobile.getText().toString());
        }
    }
}
