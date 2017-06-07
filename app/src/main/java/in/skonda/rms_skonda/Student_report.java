package in.skonda.rms_skonda;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import static in.skonda.rms_skonda.R.id.status;

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

      //  new report().execute();

        String url = "http://ioca.in/rms/reportEnrolmentService.php?deviceID=1234567890";
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
                //final String result1 = response.body().string();
               // System.out.println(result1);
                try {
                    final JSONArray jsonArray = new JSONArray(response.body().string());
                    Handler handler = new Handler(Looper.getMainLooper());
                    al=jsonArray.length();
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //Log.d("skondad: ", jsonObject.getString("name") ) ;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                  String admissionNo=jsonObject.getString("AdmissionNumber");
                                    String status=jsonObject.getString("Status");
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
                                } catch (JSONException e) {
                                    Toast.makeText(Student_report.this, "there is exception ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    Log.d("skondad: ", "number of elements are: " + jsonArray.length() );
                } catch (JSONException e) {
                    e.printStackTrace();
                };

//                Log.d("skondad: ", "response success? " + response.isSuccessful() + ", response is: " + response.body().string() );



                response.body().close();

                    }



        });



    }

}
