package in.skonda.rms_skonda;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.skonda.rms_skonda.dummy.DummyContent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;

import static android.R.attr.switchMinWidth;
import static android.R.attr.value;

public class Student_report extends AppCompatActivity {

int al=0;
    int en=0;
    int cl=0;
    int disc=0;
    int ip=0;
    int op=0;

    TextView all;
    TextView enroll;
    TextView close;
    TextView discontinue;
    TextView inprogress;
    TextView open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new report().execute();


        all = (TextView) findViewById(R.id.count_all);
        all.setText(String.valueOf(al));
        enroll = (TextView) findViewById(R.id.count_enrolled);
        enroll.setText(String.valueOf(en));
        close = (TextView) findViewById(R.id.count_closed);
        close.setText(String.valueOf(cl));
        open = (TextView) findViewById(R.id.count_open);
        open.setText(String.valueOf(op));
        discontinue = (TextView) findViewById(R.id.count_discontinue);
        discontinue.setText(String.valueOf(disc));
        inprogress = (TextView) findViewById(R.id.count_inProgress);
        inprogress.setText(String.valueOf(ip));

    }
     private class report extends AsyncTask<Void ,Void ,Void> {


         @Override
         protected Void doInBackground(Void... params) {

             String url = "http://ioca.in/rms/reportEnrolmentService.php?status=enrolled";
             Request request = new Request.Builder().url(url).build();
             OkHttpClient okHttpClient = new OkHttpClient();
             okHttpClient.newCall(request).enqueue(new Callback() {
                 @Override
                 public void onFailure(Call call, IOException e) {
                     Log.d("skondad: ", "Call Failed: " + e.getMessage());

                 }

                 @Override
                 public void onResponse(Call call, Response response) throws IOException {
                     Log.d("skondad: ", "response success? " + response.isSuccessful()
                             + response.message());
                     final String result1 = response.body().string();
                     System.out.println(result1);
                     try {
                         JSONObject Jobj = new JSONObject(result1);
                         JSONArray report = Jobj.getJSONArray("report");
                         al = report.length();
                         for (int i = 0; i < al; i++) {
                             JSONObject c = report.getJSONObject(i);
                             String AdmissionNo = c.getString("AdmissionNumber");
                             String status = c.getString("Status");
                             switch (status) {
                                 case "enrolled":
                                     en++;
                                     break;
                                 case "closed":
                                     cl++;
                                     break;
                                 case "discontinue":
                                     disc++;
                                     break;
                                 case "inprogress":
                                     ip++;
                                     break;
                                 case "open":
                                     op++;
                                     break;
                             }

                         }
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }


                 }
             });
             return null;
     }
     }
}
