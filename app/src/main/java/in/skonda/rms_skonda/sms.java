package in.skonda.rms_skonda;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class sms extends AppCompatActivity implements View.OnClickListener, Callback {


    FloatingActionButton fab;
    EditText mobileNumber;
    EditText message;

    String receivedNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        receivedNumbers = null;
        Intent receiveContactNumbers = getIntent();
        receivedNumbers = receiveContactNumbers.getStringExtra("contactNumbers");

        fab = (FloatingActionButton) findViewById(R.id.fabSMS);
        fab.setOnClickListener(this);

        mobileNumber = (EditText) findViewById(R.id.input_MobileNumber);
        message = (EditText) findViewById(R.id.input_MessageToSend);

        if (receivedNumbers != null)
            mobileNumber.setText(receivedNumbers);

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "lcicked", Toast.LENGTH_SHORT).show();
        String mobile = mobileNumber.getText().toString();
        String msg = message.getText().toString();
        String url = "http://bulksms.skonda.in/api/sendmsg.php?user=7702571000&pass=Admin1&sender=SKONDA&phone="
        + mobile + "&text=" + msg + "&priority=ndnd&stype=normal";

        Log.d("skondav: ", "sms url is: " + url);
        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(this);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(sms.this, "failed: netword issue", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String resp = response.body().string();
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(sms.this, "resp is: " + resp, Toast.LENGTH_SHORT).show();
                Log.d("skondav: ", "resp is: " + resp);
            }
        });
        response.body().close();
    }
}