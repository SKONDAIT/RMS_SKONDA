package in.skonda.rms_skonda;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class EditStudent extends AppCompatActivity implements  View.OnClickListener {
    String id;
    private EditText EditTextdoj;
    private EditText EditTextdob;
    private EditText EditTextName;
    private EditText EditTextContact;
    private EditText EditTextCourse;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        id = getIntent().getStringExtra("admissionNumber");
        Toast.makeText(this, "admission received is: " + id, Toast.LENGTH_SHORT).show();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
       // findViewsById();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addListenerOnSpinnerItemSelection();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new insert().execute(EditTextName.getText().toString(),EditTextContact.getText().toString(),EditTextAddress.getText().toString(),spinner2.getSelectedItem().toString(),spinner1.getSelectedItem().toString(),EditTextEducation.getText().toString(),EditTextdoj.getText().toString(),EditTextEmail.getText().toString(),EditTextdob.getText().toString(),EditTextStatus.getText().toString(),EditTextDiscount.getText().toString(),EditTextComments.getText().toString());
                Context context = view.getContext();
                Intent intent=new Intent(context,ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
                context.startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                //    Log.d("skondad:",response.body().string());
                    Handler handler = new Handler(Looper.getMainLooper());
                        final JSONObject c = new JSONObject(response.body().string());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String name=c.getString("Name");
                                    String contact=c.getString("Contact");
                                    String course=c.getString("Course");
                                    String channel=c.getString("Channel");
                                    String education=c.getString("EducationDetails");
                                    String doj=c.getString("DateOfJoining");
                                    String email=c.getString("Email");
                                    String status=c.getString("Status");
                                    String comments=c.getString("Comments");
                                    String address=c.getString("Address");
                                    String discount=c.getString("Discount");
                                    EditTextName=(EditText)findViewById(R.id.edit_name);
                                    EditTextName.setText(name);
                                    EditTextContact=(EditText)findViewById(R.id.edit_contact);
                                    EditTextContact.setText(contact);
                                    EditTextAddress=(EditText)findViewById(R.id.edit_address);
                                    EditTextAddress.setText(address);
                                    EditTextEmail=(EditText)findViewById(R.id.edit_email);
                                    EditTextEmail.setText(email);
                                    EditTextdoj=(EditText)findViewById(R.id.edit_dateJ);
                                    EditTextdoj.setText(doj);
                                    EditTextdob=(EditText)findViewById(R.id.edit_dateB);
                                    EditTextDiscount=(EditText)findViewById(R.id.edit_discount);
                                    EditTextDiscount.setText(discount);
                                    EditTextStatus=(EditText)findViewById(R.id.edit_status);
                                    EditTextStatus.setText(status);
                                    EditTextEducation=(EditText)findViewById(R.id.edit_education);
                                    EditTextEducation.setText(education);
                                    EditTextComments=(EditText)findViewById(R.id.edit_comments);
                                    EditTextComments.setText(comments);
                                    spinner1.setSelection(getIndex(spinner1,course));
                                    spinner2.setSelection(getIndex(spinner2,channel));
                                    setDateTimeField();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(EditStudent.this, "there is exception ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                ;
                response.body().close();
            }
        });
    }
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner_channel);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spinner2 = (Spinner) findViewById(R.id.spinner_course);
        spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
    private void findViewsById() {
        EditTextdoj = (EditText) findViewById(R.id.input_doj);
        EditTextdoj.setInputType(InputType.TYPE_NULL);

        EditTextdob = (EditText) findViewById(R.id.input_dob);
        EditTextdob.setInputType(InputType.TYPE_NULL);

    }
    private void setDateTimeField() {

        EditTextdoj.setOnClickListener(this);
        EditTextdob.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();


        dojDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                EditTextdoj.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dobDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                EditTextdob.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }


    @Override
    public void onClick(View view) {
        if (view == EditTextdoj) {
            dojDatePickerDialog.show();
        } else if (view == EditTextdob) {
            dobDatePickerDialog.show();
        }

    }
    private class insert extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],params[10],params[11]);
            return null;
        }

        protected void onPostExecute(String result) {

            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();

        }
        protected void onProgressUpdate(Integer... progress){
            // pb.setProgress(progress[0]);
        }

        public String postData(String name,String contact,String address,String course,String channel,String education,String dateJ,String email,String dateB,String status,String discount,String comments ) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ioca.in/rms/updateStudentDetails.php?device_id=1234567890&admissionNumber="+id+"");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name",name));
                nameValuePairs.add(new BasicNameValuePair("contact",contact));
                nameValuePairs.add(new BasicNameValuePair("address",address));
                nameValuePairs.add(new BasicNameValuePair("course",course));
                nameValuePairs.add(new BasicNameValuePair("channel",channel));
                nameValuePairs.add(new BasicNameValuePair("education",education));
                nameValuePairs.add(new BasicNameValuePair("dateJ",dateJ));
                nameValuePairs.add(new BasicNameValuePair("email",email));
                nameValuePairs.add(new BasicNameValuePair("dateB",dateB));
                nameValuePairs.add(new BasicNameValuePair("status",status));
                nameValuePairs.add(new BasicNameValuePair("discount",discount));
                nameValuePairs.add(new BasicNameValuePair("comments",comments));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                return response.toString();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return "";
        }}
}
