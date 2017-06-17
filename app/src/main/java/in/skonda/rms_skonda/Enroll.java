package in.skonda.rms_skonda;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static in.skonda.rms_skonda.R.id.contact;

public class Enroll extends AppCompatActivity implements View.OnClickListener {
    private EditText doe;
    private EditText doj;
    private EditText dob;
    private EditText EditTextName;
    private EditText EditTextContact;
    private EditText EditTextEducation;
    private EditText EditTextAddress;
    private EditText EditTextEmail;
    private EditText EditTextStatus;
    private EditText EditTextDiscount;
    private EditText EditTextComments;
    private DatePickerDialog doeDatePickerDialog;
    private DatePickerDialog dojDatePickerDialog;
    private DatePickerDialog dobDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    private Spinner spinner1;
    private Spinner spinner2;
    InputStream is=null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        findViewsById();
        setDateTimeField();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addListenerOnSpinnerItemSelection();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setMessage("Inserting Data");
                progressDialog.setTitle("Status");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                new insert().execute(EditTextName.getText().toString(),EditTextContact.getText().toString(),EditTextAddress.getText().toString(),spinner2.getSelectedItem().toString(),spinner1.getSelectedItem().toString(),EditTextEducation.getText().toString(),doe.getText().toString(),doj.getText().toString(),EditTextEmail.getText().toString(),dob.getText().toString(),EditTextStatus.getText().toString(),EditTextDiscount.getText().toString(),EditTextComments.getText().toString());
                Intent intent=new Intent(view.getContext(),Dashboard.class);
                startActivity(intent);
                }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner_channel);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spinner2 = (Spinner) findViewById(R.id.spinner_course);
        spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    private void findViewsById() {
        doe = (EditText) findViewById(R.id.input_doe);
        doe.setInputType(InputType.TYPE_NULL);
        doj = (EditText) findViewById(R.id.input_doj);
        doj.setInputType(InputType.TYPE_NULL);

        dob = (EditText) findViewById(R.id.input_dob);
        dob.setInputType(InputType.TYPE_NULL);
        EditTextName = (EditText) findViewById(R.id.input_name);
        EditTextContact = (EditText) findViewById(R.id.input_contact);
        EditTextEducation = (EditText) findViewById(R.id.input_education);
        EditTextAddress = (EditText) findViewById(R.id.input_address);
        EditTextEmail = (EditText) findViewById(R.id.input_email);
        EditTextStatus = (EditText) findViewById(R.id.input_status);
        EditTextDiscount = (EditText) findViewById(R.id.input_discount);
        EditTextComments = (EditText) findViewById(R.id.input_comments);
    }

    private void setDateTimeField() {
        doe.setOnClickListener(this);
        doj.setOnClickListener(this);
        dob.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        doeDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                doe.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dojDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                doj.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dobDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dob.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }


    @Override
    public void onClick(View view) {
        if (view == doe) {
            doeDatePickerDialog.show();
        } else if (view == doj) {
            dojDatePickerDialog.show();
        } else if (view == dob) {
            dobDatePickerDialog.show();
        }

    }
    private class insert extends AsyncTask<String, Integer, String> implements Callback {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ioca.in/rms/insert.php?deviceID=1234567890");
            HttpResponse response = null;
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name", params[0]));
                nameValuePairs.add(new BasicNameValuePair("contact", params[1]));
                nameValuePairs.add(new BasicNameValuePair("address", params[2]));
                nameValuePairs.add(new BasicNameValuePair("course", params[3]));
                nameValuePairs.add(new BasicNameValuePair("channel", params[4]));
                nameValuePairs.add(new BasicNameValuePair("education", params[5]));
                nameValuePairs.add(new BasicNameValuePair("dateE", params[6]));
                nameValuePairs.add(new BasicNameValuePair("dateJ", params[7]));
                nameValuePairs.add(new BasicNameValuePair("email", params[8]));
                nameValuePairs.add(new BasicNameValuePair("dateB", params[9]));
                nameValuePairs.add(new BasicNameValuePair("status", params[10]));
                nameValuePairs.add(new BasicNameValuePair("discount", params[11]));
                nameValuePairs.add(new BasicNameValuePair("comments", params[12]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                 response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return response.toString();
        }

        protected void onPostExecute(String result) {
            progressDialog.hide();
            Log.d("enroll:",result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            //if(result==)
            String url = "http://bulksms.skonda.in/api/sendmsg.php?user=7702571000&pass=Admin1&sender=SKONDA&phone="
                    + contact + "&text=" + "you have been enrolled" + "&priority=ndnd&stype=normal";

            Log.d("skondav: ", "sms url is: " + url);
            Request request = new Request.Builder().url(url).build();
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(this);
        }

        protected void onProgressUpdate(Integer... progress) {
            // pb.setProgress(progress[0]);
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Handler handler = new Handler(getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Enroll.this, "failed: netword issue", Toast.LENGTH_SHORT).show();
                }
            });
            Toast.makeText(Enroll.this, "Network Error", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String resp = response.body().string();
            Handler handler = new Handler(getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Enroll.this, "resp is: " + resp, Toast.LENGTH_SHORT).show();
                    Log.d("skondav: ", "resp is: " + resp);
                }
            });
            response.body().close();
        }
    }
    }
