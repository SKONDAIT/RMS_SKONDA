package in.skonda.rms_skonda;

import android.support.v7.app.AppCompatActivity;
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
import java.text.NumberFormat;
import java.util.Locale;

import static android.R.attr.x;
//import static in.skonda.rms_skonda.R.id.enroll;
//import static in.skonda.rms_skonda.R.id.fee;

public class Fee_report extends AppCompatActivity implements View.OnClickListener{
    ImageView channel_stat;
    ImageView course_stat;

    int courses=0;
    int pending=0;
    int total=0;
    int channel=0;
    int j;
    TextView chn;
    TextView cou;
    TextView mon;
    TextView pen;
    TextView tot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_report);
        Log.d("skondad: ", "Hi lka;sdnf;lsahd g;asdhadghsdkfhjdk: " );
        Log.d("skondad: ", "Hi i am here ");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.fee_report_appbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String url = "http://ioca.in/rms/feeReport.php?deviceID=1234567890";
        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("skondad: ", "Call Failed: " + e.getMessage());
                Toast.makeText(Fee_report.this, "Call Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("skondad: ", "response success? 2 " + response.isSuccessful()
                        );
                try{
                    final JSONArray jsonArray= new JSONArray(response.body().string());
                    Handler handler = new Handler(Looper.getMainLooper());

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        final JSONObject c= jsonArray.getJSONObject(i);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String Admission =c.getString("admissionNumber");
                                    String course= c.getString("course");
                                  // String fee=c.getString("feePaid");
                                    //String due=c.getString("due");
                                    int fee = Integer.parseInt(c.getString("feePaid"));
                                    int balance = Integer.parseInt(c.getString("due"));
                                    String Chel=c.getString("channel");


                                    pending=pending+balance;
                                    courses=courses+fee;
                                    total=total+fee;
                                    channel=channel+fee;
                                   tot = (TextView) findViewById(R.id.rs_total);
                                    tot.setText("Rs "+String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(total)));
                                    cou = (TextView) findViewById(R.id.rs_courses);
                                    cou.setText("Rs "+String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(courses)));
                                    pen = (TextView) findViewById(R.id.rs_pending);
                                    pen.setText("Rs "+String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(pending)));
                                    chn = (TextView) findViewById(R.id.rs_channel);
                                    chn.setText("Rs "+String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(channel)));
                                }

                                catch (JSONException e){
                                    e.printStackTrace();
                                    Toast.makeText(Fee_report.this, "there is exception ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                };

                response.body().close();
            }

        });
        String url1 = "http://ioca.in/rms/perMonth.php?deviceID=1234567890";
        Request request1 = new Request.Builder().url(url1).build();
        OkHttpClient okHttpClient1 = new OkHttpClient();
        okHttpClient1.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("skondad: ", "Call Failed: " + e.getMessage());
                Toast.makeText(Fee_report.this, "Call Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("skondad: ", "response success? 3 " + response.isSuccessful()
                        + response.message());
                try{
                    final JSONArray json= new JSONArray(response.body().string());
                   // final JSONObject c=  new JSONObject(response.body().string());
                    Handler handler = new Handler(Looper.getMainLooper());

                   // for( j=0;j<jsonArray.length();j++)
                    ////{
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                     JSONObject d=json.getJSONObject(0);
                                    int month=Integer.parseInt(d.getString("month"));
                                    mon = (TextView) findViewById(R.id.rs_month);
                                    mon.setText("Rs "+String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(month)));
                                }

                                catch (JSONException e){
                                    e.printStackTrace();
                                    Toast.makeText(Fee_report.this, "there is exception ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    //}
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                };

                response.body().close();

            }
        });
    }

    @Override
    public void onClick(View v) {


    }
}
