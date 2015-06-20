package santana.tebaktebakan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import santana.tebaktebakan.R;
import santana.tebaktebakan.RegistrationIntentService;
import santana.tebaktebakan.adapter.TebakanListAdapter;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.session.SessionManager;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class TebakanListActivity extends AppCompatActivity {
    /**
     * gcm variable
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    SessionManager sessionManager;
    private String regGcmID;

    //listview and adapter
    private ListView listView;
    private TebakanListAdapter tebakanListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * before init the ui check whether user is registered or loggon
         */
        sessionManager = new SessionManager(getApplicationContext());
        if(sessionManager.getUidUser().isEmpty() || sessionManager.getToken().isEmpty()){
            sessionManager.clearAllSession();
            Intent intent = new Intent(this,RegisterActivity.class);
            finish();
            startActivity(intent);

        }

        /**
         * checking the gcmID for this session
         */
        if (checkPlayServices()) {
            regGcmID = getRegistrationId(getApplicationContext());
            if(regGcmID.isEmpty() && !sessionManager.getIsGCmRegistered()){

                Log.d("RegistGCm", "start");
//                 Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                intent.putExtra(ApplicationConstants.FromActivity,TebakanListActivity.class.toString());
                startService(intent);

            }
        }

        setContentView(R.layout.layout_tebakan_list_activity);

        initUI();
    }

    public void InsertTebakan(View v){
        Intent intent = new Intent(this,UploadTebakanActivity.class);
        startActivity(intent);
    }

    private String getRegistrationId(Context context) {
//        final SharedPreferences prefs = getGcmPreferences(context);
//        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        String registrationId  = sessionManager.getGcmID();
        if (registrationId.isEmpty()) {
            Log.i("GCM", "Registration not found.");
            return "";
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.

        int currentVersion = ApplicationConstants.getAppVersion(context);
        int registeredVersion = sessionManager.getAppVersion();

        if (registeredVersion != currentVersion) {
            Log.i("GCM", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "Please Install GooglePlay Service", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }


    private void initUI(){

        tebakanListAdapter = new TebakanListAdapter(getApplicationContext(),this,R.layout.item_list_tebakan);
        listView = (ListView)findViewById(R.id.ListTebakan);
        listView.setAdapter(tebakanListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
