package santana.tebaktebakan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import santana.tebaktebakan.FinishRegistrationIntentService;
import santana.tebaktebakan.R;
import santana.tebaktebakan.RegistrationIntentService;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.common.ConnectionDetector;
import santana.tebaktebakan.session.SessionManager;

/**
 * Created by Gujarat Santana on 05/08/15.
 */
public class MainMenuActivity extends AppCompatActivity implements FinishRegistrationIntentService.Receiver{
    /**
     * gcm variable
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * UI variable
     */
    protected AppCompatTextView coin1;
    protected ProgressBar Loading;
    protected LinearLayout layout1;
    protected AppCompatTextView coin2,infoLoading;
    SessionManager sessionManager;
    /**
     * variabel result receiver for get progress from intentService
     */
    private FinishRegistrationIntentService mReceiver;
    /*
    connection detector
     */
    private ConnectionDetector cd;
    private String regGcmID;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * init facebook
         */
        FacebookSdk.sdkInitialize(getApplicationContext());


        setContentView(R.layout.layout_main_menu_activity);
        /**
         * connection detector
         */
        cd = new ConnectionDetector(getApplicationContext());


        initUI();
        LoadingUI();

        /**
         *  check whether user is registered or loggon
         */
        sessionManager = new SessionManager(getApplicationContext());
        if(sessionManager.getUidUser().isEmpty() || sessionManager.getToken().isEmpty()){
            sessionManager.clearAllSession();
//            Intent intent = new Intent(this,RegisterActivity.class);
//            finish();
//            startActivity(intent);
//
            /*
                if the first time opening the apps regsiter the user using generated user
             */
            if(checkPlayServices()){
//                Intent intent = new Intent(this, RegistrationIntentService.class);
//                intent.putExtra(ApplicationConstants.FromActivity,ApplicationConstants.FirtTime);
//                startService(intent);

                //test starting intent service
                mReceiver = new FinishRegistrationIntentService(new Handler());
                mReceiver.setReceiver(MainMenuActivity.this);
                Intent intent = new Intent(Intent.ACTION_SYNC, null, this, RegistrationIntentService.class);

                intent.putExtra("receiver", mReceiver);
                intent.putExtra("requestId", 101);
                intent.putExtra(ApplicationConstants.FromActivity, ApplicationConstants.FirtTime);

                startService(intent);

            }else{
                Toast.makeText(MainMenuActivity.this, "Please install google play service", Toast.LENGTH_SHORT).show();
                return;
            }
        }else {
            finishLoading();
        }

        /**
         * pricn key hash
         */
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.gapcer.personaworld",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        /**
         * checking the gcmID for this session
         */
//        if (checkPlayServices()) {
//            regGcmID = getRegistrationId(getApplicationContext());
//            if(regGcmID.isEmpty() && !sessionManager.getIsGCmRegistered()){
//
//                Log.d("RegistGCm", "start");
////                 Start IntentService to register this application with GCM.
//                Intent intent = new Intent(this, RegistrationIntentService.class);
//                intent.putExtra(ApplicationConstants.FromActivity,TebakanListActivity.class.toString());
//                startService(intent);
//
//            }
//        }


        printKeyhashDevelopment();
    }

    private void initUI(){
        layout1 = (LinearLayout)findViewById(R.id.layout1);
        infoLoading = (AppCompatTextView)findViewById(R.id.infoLoading);
        Loading = (ProgressBar)findViewById(R.id.Loading);
        coin1 = (AppCompatTextView)findViewById(R.id.coinSaya);
        coin2 = (AppCompatTextView)findViewById(R.id.coin1);
    }

    private void LoadingUI(){
        layout1.setVisibility(View.GONE);
        Loading.setVisibility(View.VISIBLE);
        infoLoading.setVisibility(View.VISIBLE);
        coin2.setVisibility(View.GONE);
        coin1.setVisibility(View.GONE);
    }

    private void printKeyhashDevelopment(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.gapcer.personaworld",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void finishLoading(){
        layout1.setVisibility(View.VISIBLE);
        Loading.setVisibility(View.GONE);
        infoLoading.setVisibility(View.GONE);
        coin2.setVisibility(View.VISIBLE);
        coin1.setVisibility(View.VISIBLE);
        coin1.setText(String.valueOf(sessionManager.getCoins()));
    }

    public void MainMenuClick (View v){
        Intent intent;
        switch (v.getId()){
            case R.id.menu1 :
                if(cd.isConnectingToInternet()){
                    if(sessionManager.getModeUser()){
                        intent = new Intent(MainMenuActivity.this,AnswerTempTebakanActivity.class);
                        startActivity(intent);
                    }else{
                        intent = new Intent(MainMenuActivity.this,AnswerTebakanActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.menu2 :
                Toast.makeText(getApplicationContext(),"This Feature is Coming Soon",Toast.LENGTH_LONG).show();
//                intent = new Intent(MainMenuActivity.this,RankingActivity.class);
//                startActivity(intent);

                break;
            case R.id.menu3 :
                Toast.makeText(getApplicationContext(),"This Feature is Coming Soon",Toast.LENGTH_LONG).show();
//                intent = new Intent(MainMenuActivity.this,SettingsActivity.class);
//                startActivity(intent);

                break;
            case R.id.menu4 :
                Toast.makeText(getApplicationContext(),"This Feature is Coming Soon",Toast.LENGTH_LONG).show();
                intent = new Intent(MainMenuActivity.this, UploadTebakanActivity.class);
                startActivity(intent);

                break;
            case R.id.menu5 :
                Toast.makeText(getApplicationContext(),"This Feature is Coming Soon",Toast.LENGTH_LONG).show();
//                intent = new Intent(MainMenuActivity.this,RulesActivity.class);
//                startActivity(intent);

                break;
            case R.id.menu6 :
                intent = new Intent(MainMenuActivity.this,RegisterActivity.class);
                startActivity(intent);
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

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case RegistrationIntentService.STATUS_RUNNING:
//                LoadingUI();
                infoLoading.setText("Register Temporary Account");
                setProgressBarIndeterminateVisibility(true);
                break;
            case RegistrationIntentService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                setProgressBarIndeterminateVisibility(false);
                finishLoading();
                Log.d("coin", String.valueOf(sessionManager.getCoins()));

                break;
            case RegistrationIntentService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                break;
        }
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
