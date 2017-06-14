package in.skonda.rms_skonda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


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
import java.text.NumberFormat;
import java.util.Locale;

import static in.skonda.rms_skonda.R.id.status;

public class course extends AppCompatActivity {

    int al=0;
    int fee_paid;
    int javafee;
    int  dotnetfee;
    int bigdatafee;
    int afee;
    TextView java;
    TextView dotnet;
    TextView bigdata;
    TextView android;
    String course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        //  new report().execute();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.course_appbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String url = "http://ioca.in/rms/feeReport.php?deviceID=1234567890";
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

                try {
                    final JSONArray jsonArray = new JSONArray(response.body().string());
                    Handler handler = new Handler(Looper.getMainLooper());
                    al=jsonArray.length();
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String admissionno=jsonObject.getString("admissionNumber");
                                    course=jsonObject.getString("course");
                                    fee_paid=Integer.parseInt(jsonObject.getString("feePaid"));
                                    Log.d("skondad: ","channel is" + course);
                                    Log.d("skondad: ","Fee paid is" + course);
                                    switch (course) {
                                        case "java":
                                            javafee+=fee_paid;
                                            break;
                                        case "dotnet":
                                            dotnetfee+=fee_paid;
                                            break;
                                        case "bigdata":
                                            bigdatafee+=fee_paid;
                                            break;
                                        case "Android":
                                            afee+=fee_paid;
                                            break;


                                    }
                                    java = (TextView) findViewById(R.id.javaamount);
                                    java.setText("Rs " + String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(javafee)));
                                    dotnet = (TextView) findViewById(R.id.dotnetamount);
                                    dotnet.setText("Rs " + String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(dotnetfee)));
                                    bigdata = (TextView) findViewById(R.id.bd);
                                    bigdata.setText("Rs " + String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(bigdatafee)));
                                    android= (TextView) findViewById(R.id.andr);
                                    android.setText("Rs " + String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(afee)));



                                } catch (JSONException e) {
                                    Toast.makeText(course.this, "there is exception ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    Log.d("skondad: ", "number of elements are: " + jsonArray.length() );
                } catch (JSONException e) {
                    e.printStackTrace();
                };





                response.body().close();

            }



        });



    }

}

