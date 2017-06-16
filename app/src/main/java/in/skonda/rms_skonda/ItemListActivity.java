package in.skonda.rms_skonda;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import java.util.ArrayList;
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
public class ItemListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, TabLayout.OnTabSelectedListener  {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public SearchView searchView;
    TabLayout listTabLayout;
    List<DummyContent.DummyItem> ItemsTemp;
    String contactNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        ItemsTemp = new ArrayList<DummyContent.DummyItem>();

        listTabLayout = (TabLayout) findViewById(R.id.listTabLayout);
        listTabLayout.addOnTabSelectedListener(this);
        TabLayout.Tab openTab = listTabLayout.newTab();
        openTab.setText("Open");
        TabLayout.Tab closeTab = listTabLayout.newTab();
        closeTab.setText("Closed");
        TabLayout.Tab enrollTab = listTabLayout.newTab();
        enrollTab.setText("Enrolled");

        listTabLayout.addTab(openTab);
        listTabLayout.addTab(closeTab);
        listTabLayout.addTab(enrollTab);
        OkHttpClient okHttpClient = new OkHttpClient();
        DummyContent.ITEMS.clear();
        Request request = new Request.Builder().url("http://ioca.in/rms/fetchallstudentinfo.php?deviceID=1234567890").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONArray jsonArray = new JSONArray(response.body().string());
                    Handler handler = new Handler(Looper.getMainLooper());
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    DummyContent.skondaAdd(
                                            jsonObject.getString("admissionNumber"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("contact"),
                                            jsonObject.getString("course"),
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
                            ItemsTemp.addAll(DummyContent.ITEMS);
                            View recyclerView = findViewById(R.id.item_list);
                            assert recyclerView != null;
                            setupRecyclerView((RecyclerView) recyclerView);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                };
                response.body().close();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactNumbers = "";
                for (DummyContent.DummyItem item: DummyContent.ITEMS) {
                    contactNumbers += item.contact + ",";
                }
                Intent smsIntent = new Intent(getBaseContext(), sms.class);
                smsIntent.putExtra("contactNumbers", contactNumbers);
                startActivity(smsIntent);


            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_list_items, menu);
        MenuItem searchItem = menu.findItem(R.id.searchStudent);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified() )
            searchView.setIconified(true);
        else
            super.onBackPressed();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        DummyContent.ITEMS.clear();
        for (DummyContent.DummyItem itemTemp :ItemsTemp) {
            if (itemTemp.course.contains(query) || itemTemp.name.contains(query) || itemTemp.contact.contains(query))  {
                DummyContent.ITEMS.add(itemTemp);
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        Toast.makeText(this, "test changed", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabStatus = tab.getText().toString();


        DummyContent.ITEMS.clear();

        for (DummyContent.DummyItem itemTemp :ItemsTemp) {
            if ( itemTemp.status.equals(tabStatus) )
            {
                DummyContent.ITEMS.add(itemTemp);
            }
        }
        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
