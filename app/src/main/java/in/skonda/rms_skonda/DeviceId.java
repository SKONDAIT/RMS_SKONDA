package in.skonda.rms_skonda;

import android.*;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import in.skonda.rms_skonda.dummy.DummyContent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by my pc on 16-06-2017.
 */

public class DeviceId extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.d("skondad: ", "token is: " + FirebaseInstanceId.getInstance().getToken());
        Log.d("skondad: ", "id is: " + FirebaseInstanceId.getInstance().getId());
        String deviceId = FirebaseInstanceId.getInstance().getId();
        DummyContent.deviceId = deviceId;
    }
}
