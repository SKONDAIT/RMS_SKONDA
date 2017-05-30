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

// login activity - sudarsan.konda
// added interface OnKeyListener. .
// adding on 30th May
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
            SmsManager smsManager = SmsManager.getDefault();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED)
                Log.d("skondad", "permission not granted");
//            smsManager.sendTextMessage(mob, null, "random number", null, null );
            Log.d("skondad", "again: " + mobile.getText().toString());
        }
    }
}
