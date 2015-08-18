package santana.tebaktebakan.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import santana.tebaktebakan.AppController;
import santana.tebaktebakan.R;
import santana.tebaktebakan.RegistrationIntentService;
import santana.tebaktebakan.adapter.TebakanListAdapterCompat;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.common.ServerConstants;
import santana.tebaktebakan.requestNetwork.CostumRequestString;
import santana.tebaktebakan.session.SessionManager;
import santana.tebaktebakan.utilityUI.RecyclerScrollListener;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class TebakanListActivity extends AppCompatActivity implements Response.Listener<String>,Response.ErrorListener{
    /**
     * gcm variable
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    SessionManager sessionManager;
    private String regGcmID;
    private int WidthPhone;

    /*menu option*/
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /**
         * before init the ui check whether user is registered or loggon
         */
        sessionManager = new SessionManager(getApplicationContext());
        if(sessionManager.getUidUser().isEmpty() || sessionManager.getToken().isEmpty()){
            sessionManager.clearAllSession();
//            Intent intent = new Intent(this,RegisterActivity.class);
//            finish();
//            startActivity(intent);


            /*
                if the first time opening the apps regsiter the user using generated user
             */
            if(checkPlayServices()){
                Intent intent = new Intent(this, RegistrationIntentService.class);
                intent.putExtra(ApplicationConstants.FromActivity,TebakanListActivity.class.toString());
                startService(intent);
            }else{
                Toast.makeText(TebakanListActivity.this, "Please install google play service", Toast.LENGTH_SHORT).show();
                return;
            }

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.InsertTebakan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(TebakanListActivity.this, UploadTebakanActivity.class);
                startActivity(intent);
//                menu.findItem(R.id.Point).setTitle("250");


            }
        });

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

         /*get widht of the phone*/
        if(currentapiVersion >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            /**
             * merah di bawah memang sengaja, buat api 13 keatas
             * itu warning doank
             */
            display.getSize(size);
            WidthPhone =  size.x;
        }else{
            Display display = getWindowManager().getDefaultDisplay();
            WidthPhone = display.getWidth();  // deprecated
        }


        /**
         * List view untuk gambar dan tebakan
         */
        RecyclerView rv = (RecyclerView) findViewById(R.id.ListTebakan);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new TebakanListAdapterCompat(getApplicationContext(), this, WidthPhone));




        /*slide floating button*/
        if (currentapiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            // Do something for froyo and above versions
            rv.addOnScrollListener(new RecyclerScrollListener() {
                @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                @Override
                public void show() {
                    fab.animate().translationY(0).setInterpolator(
                            new DecelerateInterpolator(2)).start();
                }

                @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                @Override
                public void hide() {
                    fab.animate().translationY(fab.getHeight() +
                            getResources().getDimension(R.dimen.fab_margin))
                            .setInterpolator(new AccelerateInterpolator(2)).start();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tebakan_activity, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Point :
                Toast.makeText(TebakanListActivity.this, "bangkai", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void RegisterGenereteUser(){

        // Add custom implementation, as needed.
        Map<String,String> mParams = new HashMap<String,String>();

        //token gcm
        mParams.put(ServerConstants.mParamsGcmID,"token");
        mParams.put(ServerConstants.mParamsEmail,"bray@gmail.com");
        mParams.put(ServerConstants.mParamsPassword,"password");
        mParams.put(ServerConstants.mParamsUsername,"new player");

        //token server
        CostumRequestString myreq = new CostumRequestString(com.android.volley.Request.Method.POST, ServerConstants.registerUser , mParams, TebakanListActivity.this,TebakanListActivity.this);

        myreq.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myreq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(TebakanListActivity.this, "Error Bray", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getString(ServerConstants.statusBeckend).equalsIgnoreCase(ServerConstants.statusBeckendOk)){
                switch (jsonObject.getInt(ServerConstants.resultType)){
                    case ServerConstants.registerResult:
                        //login result
                        sessionManager.setUidUser(jsonObject.getString(ServerConstants._idUser));
                        sessionManager.setToken(jsonObject.getString(ServerConstants.getTokenBeckend));
                        sessionManager.setUsername(jsonObject.getString(ServerConstants.username));
                        sessionManager.setIsGcmRegistered(false);
                        sessionManager.setAppVersion(ApplicationConstants.getAppVersion(this));
                        sessionManager.setHint(3);
                        sessionManager.setCoins(Integer.parseInt(jsonObject.getString(ServerConstants.point)));
                        sessionManager.setIdTebakan(jsonObject.getString(ServerConstants.mParamlastIdTebakan));
                        sessionManager.setLevel(jsonObject.getString(ServerConstants.mParamlevel));

                        //email and password
                        sessionManager.setEmail("bray@gmail.com");
                        sessionManager.setPassword("password");
                        break;
                }
            }else{
                //not ok
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
