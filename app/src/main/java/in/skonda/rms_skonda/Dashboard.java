package in.skonda.rms_skonda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    ImageView enroll;
    ImageView list;
    ImageView student;
    ImageView fee;
    ImageView sms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        enroll = (ImageView) findViewById(R.id.enroll);
        list = (ImageView) findViewById(R.id.list);
        student = (ImageView) findViewById(R.id.student);
        fee = (ImageView) findViewById(R.id.fee);
        sms = (ImageView) findViewById(R.id.sms);

        enroll.setOnClickListener(this);
        list.setOnClickListener(this);
        student.setOnClickListener(this);
        fee.setOnClickListener(this);
        sms.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.enroll:
                Toast.makeText(this, "enroll clicked", Toast.LENGTH_SHORT).show();
                Intent intentEnroll = new Intent(this, Enroll.class);
                startActivity(intentEnroll);
                break;
            case R.id.list:
                Toast.makeText(this, "list clicked", Toast.LENGTH_SHORT).show();
                Intent intentList = new Intent(this, ItemListActivity.class);
                startActivity(intentList);
                break;
            case R.id.student:
                Toast.makeText(this, "student clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fee:
                Toast.makeText(this, "fee clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sms:
                Toast.makeText(this, "sms clicked", Toast.LENGTH_SHORT).show();
                Intent intentSms = new Intent(this, sms.class);
                startActivity(intentSms);
                break;
        }
    }
}
