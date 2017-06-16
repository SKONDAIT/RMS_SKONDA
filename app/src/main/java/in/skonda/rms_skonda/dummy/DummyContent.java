package in.skonda.rms_skonda.dummy;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    public static String deviceId = null;
    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

/*    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }*/


    public static void skondaAdd(String admissionNumber, String name, String contact, String course, String due, String dateOfEnquiry, String status )
    {
        DummyItem dummyItem = new DummyItem(admissionNumber, name, contact, course, due, dateOfEnquiry, status );
        addItem(dummyItem);
        Log.d("skondad: ", "this is from skonda method");
    }

    private static void addItem(DummyItem item) {
        Log.d("skondad: ", "adding item");
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.admissionNumber), item);
    }

/*    private static DummyItem createDummyItem(final int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }*/

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String admissionNumber;
        public final String name;
        public final String contact;
        public final String course;
        public final String due;
        public final String dateOfEnquiry;
        public final String status;

        public DummyItem(String admissionNumber, String name, String contact, String course, String due, String dateOfEnquiry, String status) {
            this.admissionNumber = admissionNumber;
            this.name = name;
            this.contact= contact;
            this.course = course;
            this.due = due;
            this.dateOfEnquiry = dateOfEnquiry;
            this.status = status;
        }

/*        @Override
        public String toString() {
            return content;
        }*/
    }
}
