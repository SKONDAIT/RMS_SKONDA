package in.skonda.rms_skonda;

import android.app.DatePickerDialog;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.widget.DatePicker;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.Build.VERSION_CODES.N;
import static in.skonda.rms_skonda.R.id.Adno;
import static in.skonda.rms_skonda.R.id.nam;
import static in.skonda.rms_skonda.R.id.nam;

public class FeePay extends AppCompatActivity implements AdapterView.OnItemSelectedListener ,View.OnClickListener{

    TextView Admin;
    TextView Name;
    TextView Course;
    TextView CourseFee;
    TextView FeePaid;
    TextView due;
    EditText dpaid;
    EditText money;

     DatePickerDialog dop;
     SimpleDateFormat dateFormatter;

    String mode;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_pay);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        findViewsById();
        setDateTimeField();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.feeProceed);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 money=(EditText) findViewById(R.id.input_balance) ;
                String bl=money.getText().toString();
                date=dpaid.getText().toString();


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.mode_spinner);
        spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mode_spinner, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Displaying required data
        String url="http://ioca.in/rms/test2.php?admno=2&device_id=1234567890";
        Request request= new Request.Builder().url(url).build();
        OkHttpClient okHttpClient=new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("skondad: ", "Call Failed: " + e.getMessage());
                Toast.makeText(FeePay.this, "Call Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("skondad: ", "response success for details?  " + response.isSuccessful()
                );
                try {
                    final JSONArray jsonArray = new JSONArray(response.body().string());
                    Handler handler = new Handler(Looper.getMainLooper());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        final JSONObject c = jsonArray.getJSONObject(i);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String name=c.getString("Name");
                                    String course=c.getString("Course");
                                    int fee=Integer.parseInt(c.getString("fee_amount"));
                                    int paid=Integer.parseInt(c.getString("feePaid"));
                                    int bal=Integer.parseInt(c.getString("due"));

                                    Admin=(TextView)findViewById(R.id.Adno);
                                    Admin.setText("2");
                                    Name=(TextView)findViewById(R.id.nam);
                                    Name.setText(name);
                                    Course=(TextView)findViewById(R.id.Course);
                                    Course.setText(course);
                                    CourseFee=(TextView)findViewById(R.id.CFee);
                                    CourseFee.setText("Rs "+String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(fee)));
                                    FeePaid=(TextView)findViewById(R.id.AmtPaid);
                                    FeePaid.setText("Rs "+String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(paid)));
                                    due=(TextView)findViewById(R.id.due);
                                    due.setText("Rs "+String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(bal)));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(FeePay.this, "there is exception ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                };
                response.body().close();
            }
        });
    }
    public void findViewsById(){
        dpaid=(EditText) findViewById(R.id.input_datePaid);
        dpaid.setInputType(InputType.TYPE_NULL);
    }

    public void setDateTimeField() {
        dpaid.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        dop=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dpaid.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position>0) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                    Toast.LENGTH_SHORT).show();
            mode = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if(v == dpaid) {
            dop.show();
            date=dpaid.getText().toString();
            Log.d("skondad: ","" +date);
        }

    }
}
