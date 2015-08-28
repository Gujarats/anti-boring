package santana.tebaktebakan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
import santana.tebaktebakan.socialMedia.FinishShare;
import santana.tebaktebakan.socialMedia.SocialMediaConstant;
import santana.tebaktebakan.socialMedia.TwitterObject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Gujarat Santana on 05/08/15.
 */
public class MainMenuActivity extends AppCompatActivity implements FinishRegistrationIntentService.Receiver, FinishShare, View.OnClickListener {
    /**
     * gcm variable
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * Twitter variable
     */
    private static Twitter twitter;
    private static RequestToken requestToken;
    /**
     * UI variable
     */
    protected AppCompatTextView coin1;
    protected ProgressBar Loading;
    protected LinearLayout layout1;
    protected AppCompatTextView coin2,infoLoading;
    protected ImageView shareTwitter,shareFacebook;
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
//        FacebookSdk.sdkInitialize(getApplicationContext());


        setContentView(R.layout.layout_main_menu_activity);
        /**
         * connection detector
         */
        cd = new ConnectionDetector(getApplicationContext());

        /**
         *  Enabling strict mode for twitter
         *  */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



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


        loadAds();
        printKeyhashDevelopment();
    }

    private void initUI(){
        layout1 = (LinearLayout)findViewById(R.id.layout1);
        infoLoading = (AppCompatTextView)findViewById(R.id.infoLoading);
        Loading = (ProgressBar)findViewById(R.id.Loading);
        coin1 = (AppCompatTextView)findViewById(R.id.coinSaya);
        coin2 = (AppCompatTextView)findViewById(R.id.coin1);
        shareFacebook = (ImageView)findViewById(R.id.share1);
        shareTwitter = (ImageView)findViewById(R.id.share2);

        /**
         * set onClikc Listter
         */
        shareTwitter.setOnClickListener(MainMenuActivity.this);
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

    private void loadAds(){
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4")  // My Galaxy Nexus test phone
                .build();
        mAdView.loadAd(request);
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

                if(sessionManager.getCoins()==0){
                    Toast.makeText(MainMenuActivity.this, "Please Help Us to Share, and you'll get 200 coins", Toast.LENGTH_SHORT).show();
                }else{
                    intent = new Intent(MainMenuActivity.this,AnswerTempTebakanActivity.class);
                    startActivity(intent);
                }

//                if(cd.isConnectingToInternet()){
//                    if(sessionManager.getModeUser()){
//                        intent = new Intent(MainMenuActivity.this,AnswerTempTebakanActivity.class);
//                        startActivity(intent);
//                    }else{
//                        intent = new Intent(MainMenuActivity.this,AnswerTebakanActivity.class);
//                        startActivity(intent);
//                    }
//                }
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

    public void ShareTwitter(){
        TwitterObject twitterObject = new TwitterObject(MainMenuActivity.this,getApplicationContext(),MainMenuActivity.this);
        if(sessionManager.getLoginTwitter()){

            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.share_image_zero);

            twitterObject.setBitmap(((BitmapDrawable)drawable).getBitmap());
            twitterObject.execute("#AntiBoring Gambar Apa itu?");
        }else{
            loginToTwitter();
        }


    }

    public void loginToTwitter() {
        //boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

        boolean isLoggedIn = sessionManager.getLoginTwitter();
        if (!isLoggedIn) {
            final ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(SocialMediaConstant.consumerKeyTwitter);
            builder.setOAuthConsumerSecret(SocialMediaConstant.consumerSecretTwitter);

            final Configuration configuration = builder.build();
            final TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();
            try {
                requestToken = twitter.getOAuthRequestToken(SocialMediaConstant.callbackTwitter);

                /**
                 *  Loading twitter login page on webview for authorization
                 *  Once authorized, results are received at onActivityResult
                 *  */
                final Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
                startActivityForResult(intent, SocialMediaConstant.WEBVIEW_REQUEST_CODE_Twitter);

            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }else{
            /**
             * twitter is Login
             */
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SocialMediaConstant.WEBVIEW_REQUEST_CODE_Twitter) {
            if (resultCode == Activity.RESULT_OK) {
                String verifier = data.getExtras().getString(SocialMediaConstant.oAuthVerifier);
                try {
                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

                    long userID = accessToken.getUserId();
                    final User user = twitter.showUser(userID);
                    String username = user.getName();

//                    saveTwitterInfo(accessToken);
                    sessionManager.saveTwitterInfodong(accessToken.getToken(),accessToken.getTokenSecret(),true,user.getName());

//                    Fragment fragment = getFragmentManager()
//                            .findFragmentByTag(ApplicationConstants.tagEmojiFragmet);
//                    if (fragment != null) {
//                        fragment.onActivityResult(requestCode, resultCode, data);
//                    }

                    ShareTwitter();


                } catch (Exception e) {
                    //Log.e("Twitter Login Failed", e.getMessage());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finishShare() {
        sessionManager.setCoins(sessionManager.getCoins()+200);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.share1 :
                break;

            case R.id.share2 :
                ShareTwitter();
                break;

            default:
                break;
        }
    }
}
