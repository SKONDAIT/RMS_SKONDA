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
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;


import java.util.logging.LogRecord;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        OkHttpClient okHttpClient = new OkHttpClient();
        DummyContent.ITEMS.clear();
        Request request = new Request.Builder().url("http://ioca.in/rms/fetchallstudentinfo.php?deviceID=1234567890").build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("skondad: ", "Call Failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    final JSONArray jsonArray = new JSONArray(response.body().string());
                    Handler handler = new Handler(Looper.getMainLooper());
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.d("skondad: ", jsonObject.getString("name") ) ;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    DummyContent.skondaAdd(
                                            jsonObject.getString("admissionNumber"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("contact"),
                                            jsonObject.getString("course.xml"),
                                            jsonObject.getString("due"),
                                            jsonObject.getString("DateOfEnquiry"),
                                            jsonObject.getString("status")
                                            );
                                } catch (JSONException e) {
                                    Toast.makeText(ItemListActivity.this, "there is exception ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            View recyclerView = findViewById(R.id.item_list);
                            assert recyclerView != null;
                            setupRecyclerView((RecyclerView) recyclerView);
                        }
                    });
                    Log.d("skondad: ", "number of elements are: " + jsonArray.length() );
                } catch (JSONException e) {
                    e.printStackTrace();
                };

//                Log.d("skondad: ", "response success? " + response.isSuccessful() + ", response is: " + response.body().string() );



                response.body().close();
            }
        });

        Log.d("skondad: ", "main thread name is: " + Thread.currentThread().getName() );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setTitle(getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        View recyclerView = findViewById(R.id.item_list);
//        assert recyclerView != null;
//        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
            Log.d("skondad: ", "number of items are: " + mValues.size());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mNameView.setText(mValues.get(position).name);
            holder.mContactView.setText(mValues.get(position).contact);
//            holder.mContactView.setText("7702571000");
            holder.mCourseView.setText(mValues.get(position).course);
//            holder.mDueView.setText("1000");
            holder.mDueView.setText(mValues.get(position).due);
            holder.mDateOfEnquiryView.setText(mValues.get(position).dateOfEnquiry);
            holder.mStatusView.setText(mValues.get(position).status);
            holder.mAdmissionNumber.setText(mValues.get(position).admissionNumber);
//            holder.mAdmissionNumber.setText("1");

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.admissionNumber) );
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.admissionNumber));

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mNameView;
            public final TextView mContactView;
            public final TextView mCourseView;
            public final TextView mDueView;
            public final TextView mDateOfEnquiryView;
            public final TextView mStatusView;

            public final TextView mAdmissionNumber;

            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.name);
                mContactView = (TextView) view.findViewById(R.id.contact);
                mCourseView = (TextView) view.findViewById(R.id.course);
                mDueView = (TextView) view.findViewById(R.id.due);
                mDateOfEnquiryView = (TextView) view.findViewById(R.id.dateOfEnquiry);
                mStatusView = (TextView) view.findViewById(R.id.status);
                mAdmissionNumber = (TextView) view.findViewById(R.id.admissionNumber);
            }

/*            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }*/
        }
    }
}
