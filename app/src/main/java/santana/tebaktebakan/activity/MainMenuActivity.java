package santana.tebaktebakan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import santana.tebaktebakan.R;
import santana.tebaktebakan.RegistrationIntentService;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.session.SessionManager;

/**
 * Created by Gujarat Santana on 05/08/15.
 */
public class MainMenuActivity extends AppCompatActivity {
    /**
     * gcm variable
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    SessionManager sessionManager;
    private String regGcmID;


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

        setContentView(R.layout.layout_main_menu_activity);
        initUI();
    }

    private void initUI(){

    }

    public void MainMenuClick (View v){
        Intent intent;
        switch (v.getId()){
            case R.id.menu1 :
                intent = new Intent(MainMenuActivity.this,AnswerTebakanActivity.class);
                startActivity(intent);

                break;
            case R.id.menu2 :
                intent = new Intent(MainMenuActivity.this,RankingActivity.class);
                startActivity(intent);

                break;
            case R.id.menu3 :
                intent = new Intent(MainMenuActivity.this,SettingsActivity.class);
                startActivity(intent);

                break;
            case R.id.menu4 :
                intent = new Intent(MainMenuActivity.this,MoreCoinsActivity.class);
                startActivity(intent);

                break;
            case R.id.menu5 :
                intent = new Intent(MainMenuActivity.this,RulesActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }
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
}
