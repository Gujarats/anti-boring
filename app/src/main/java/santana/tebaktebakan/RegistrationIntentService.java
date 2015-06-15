/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package santana.tebaktebakan;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import santana.tebaktebakan.activity.RegisterActivity;
import santana.tebaktebakan.activity.TebakanListActivity;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.common.ServerConstants;
import santana.tebaktebakan.requestNetwork.CostumRequestString;
import santana.tebaktebakan.session.SessionManager;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    private SessionManager sessionManager;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String fromActivity = intent.getExtras().getString(ApplicationConstants.FromActivity);

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // [START get_token]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.

                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(ApplicationConstants.SENDER_ID_GCM,
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                // TODO: Implement this method to send any registration to your app's servers.
                sessionManager = new SessionManager(this);
                sessionManager.setGcmID(token);
                sessionManager.setAppVersion(ApplicationConstants.getAppVersion(this));

                if(fromActivity.equals(RegisterActivity.class.toString())){
                    sendRegistrationToServer(token);
                }else if(fromActivity.equals(TebakanListActivity.class.toString())){
                    sendRegistrationToServer2(token);
                }

                // Subscribe to topic channels
                subscribeTopics(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                // [END get_token]
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        // Add custom implementation, as needed.
        Map<String,String> mParams = new HashMap<String,String>();

        //token gcm
        mParams.put(ServerConstants.mParamsGcmID,token);
        mParams.put(ServerConstants.mParamsEmail,sessionManager.getEmail());
        mParams.put(ServerConstants.mParamsPassword,sessionManager.getPassword());
        mParams.put(ServerConstants.mParamsPassword,sessionManager.getUsername());

        //token server
        CostumRequestString myreq = new CostumRequestString(com.android.volley.Request.Method.POST, ServerConstants.registerUser , mParams, new Response.Listener<String> (){

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    JSONObject jsonObject =  new JSONObject(response);
                    if(jsonObject.getString(ServerConstants.statusBeckend).equalsIgnoreCase(ServerConstants.statusBeckendOk)){
                        switch (jsonObject.getInt(ServerConstants.resultType)){

                            case ServerConstants.registerResult :

                                break;
                            case ServerConstants.registerGcmResult :

                                break;
                        }
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error ", error.getMessage());
            }
        });

        myreq.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myreq);

    }

    /**
     * register gcm second time
     */

    private void sendRegistrationToServer2(String token) {

        // Add custom implementation, as needed.
        Map<String,String> mParams = new HashMap<String,String>();

        //token gcm
        mParams.put(ServerConstants.mParamsGcmID,token);
        mParams.put(ServerConstants.mParamsToken,sessionManager.getToken());
        mParams.put(ServerConstants.mParams_idUser,sessionManager.getUidUser());

        //token server
        CostumRequestString myreq = new CostumRequestString(com.android.volley.Request.Method.POST, ServerConstants.registerGcm , mParams, new Response.Listener<String> (){

            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    JSONObject jsonObject =  new JSONObject(response);
                    if(jsonObject.getString(ServerConstants.statusBeckend).equalsIgnoreCase(ServerConstants.statusBeckendOk)){
                        switch (jsonObject.getInt(ServerConstants.resultType)){
                            case ServerConstants.registerResult :

                                break;
                            case ServerConstants.registerGcmResult :

                                break;
                        }
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error ", error.getMessage());
            }
        });

        myreq.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myreq);

    }
    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }

    // [END subscribe_topics]

}
