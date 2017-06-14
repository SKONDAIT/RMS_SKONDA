package in.skonda.rms_skonda;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import in.skonda.rms_skonda.dummy.DummyContent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Log.d("skondad: ", "item id is: " + getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(String.valueOf(mItem.admissionNumber) );
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem == null) {
            return rootView;
        }

        ((TextView) rootView.findViewById(R.id.item_detail)).setText("details goes here. . ");
        ((TextView) rootView.findViewById(R.id.item_contact)).setText("7702571000");

        Request request = new Request.Builder().url("http://ioca.in/rms/fetchstudentdetails.php?device_id=1234567890&admissionNumber=" + mItem.admissionNumber).build();
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("skondad: ", "Call Failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("skondad: ", "response success IN FRAGMENT? " + response.isSuccessful() + ", response is: "  );

                try {
                    final JSONObject itemDetail = new JSONObject(response.body().string());
                    Log.d("skondad: ", "contact is: " + itemDetail.getString("Contact"));
                    response.body().close();
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((TextView) rootView.findViewById(R.id.item_contact)).setText(itemDetail.getString("Contact"));
                                ((TextView) rootView.findViewById(R.id.item_course)).setText(itemDetail.getString("Course"));
                                ((TextView) rootView.findViewById(R.id.item_doe)).setText(itemDetail.getString("DateOfEnquiry"));
                                ((TextView) rootView.findViewById(R.id.item_channel)).setText(itemDetail.getString("Channel"));
                                ((TextView) rootView.findViewById(R.id.item_address)).setText(itemDetail.getString("Address"));
                                ((TextView) rootView.findViewById(R.id.item_feepaid)).setText(itemDetail.getString("fee_paid"));
                                ((TextView) rootView.findViewById(R.id.item_outstanding)).setText(itemDetail.getString("due_amount"));
                                ((TextView) rootView.findViewById(R.id.item_email)).setText(itemDetail.getString("Email"));
                                ((TextView) rootView.findViewById(R.id.item_education)).setText(itemDetail.getString("EducationDetails"));
                                ((TextView) rootView.findViewById(R.id.item_comments)).setText(itemDetail.getString("comments"));
                                ((TextView) rootView.findViewById(R.id.item_detail)).setText(itemDetail.getString("Name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


        return rootView;
    }
}
