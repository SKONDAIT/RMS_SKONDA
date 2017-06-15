package in.skonda.rms_skonda;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditStudent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String id;
    String mode;
    private EditText doe;
    private EditText doj;
    private EditText dob;
    private EditText EditTextName;
    private EditText EditTextContact;
    private EditText EditTextCourse;
    private EditText EditTextEducation;
    private EditText EditTextAddress;
    private EditText EditTextEmail;
    private EditText EditTextStatus;
    private EditText EditTextDiscount;
    private EditText EditTextComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        id = getIntent().getStringExtra("admissionNumber");
        Toast.makeText(this, "admission received is: " + id, Toast.LENGTH_SHORT).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_course);
        spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.course_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner_channel);
        spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.channel_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter1);
        String url="http://ioca.in/rms/fetchstudentdetails.php?admissionNumber="+id+"&device_id=1234567890";
        Request request= new Request.Builder().url(url).build();
        OkHttpClient okHttpClient=new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("skondad: ", "Call Failed: " + e.getMessage());
                Toast.makeText(EditStudent.this, "Call Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                    String contact=c.getString("Contact");


                                    EditTextName=(EditText)findViewById(R.id.edit_name);
                                    EditTextName.setText(name);
                                    EditTextContact=(EditText)findViewById(R.id.edit_contact);
                                    EditTextContact.setText(contact);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(EditStudent.this, "there is exception ", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
