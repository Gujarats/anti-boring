package santana.tebaktebakan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import santana.tebaktebakan.AppController;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.common.ServerConstants;
import santana.tebaktebakan.requestNetwork.CostumRequestString;
import santana.tebaktebakan.session.SessionManager;

/**
 * Created by Gujarat Santana on 15/06/15.
 */
public class LoginActivity extends AppCompatActivity implements Response.Listener<String>,Response.ErrorListener {

    protected AppCompatEditText Email,Password;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_activity);
        sessionManager = new SessionManager(getApplicationContext());
        initUI();
    }

    private void initUI(){
        Email = (AppCompatEditText)findViewById(R.id.EmailLogin);
        Password = (AppCompatEditText)findViewById(R.id.PasswordLogin);
    }

    public void Login(View v){
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if(email!=null && email.trim().length()>0 && password!=null && password.trim().length()>0){
            Map<String,String> mParams = new HashMap<String,String>();
            mParams.put(ServerConstants.mParamsPassword,password);
            mParams.put(ServerConstants.mParamsEmail,email);

            CostumRequestString myReq = new CostumRequestString(Request.Method.POST,ServerConstants.loginTebakan,mParams,LoginActivity.this,LoginActivity.this);
            myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(myReq);

        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error",error.getMessage());
    }

    @Override
    public void onResponse(String response) {
        Log.d("respone",response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getString(ServerConstants.statusBeckend).equalsIgnoreCase(ServerConstants.statusBeckendOk)){
                if(jsonObject.getInt(ServerConstants.resultType) == ServerConstants.loginResult){

                    //login result
                    sessionManager.setUidUser(jsonObject.getString(ServerConstants._idUser));
                    sessionManager.setToken(jsonObject.getString(ServerConstants.getTokenBeckend));
                    sessionManager.setGcmID(jsonObject.getString(ServerConstants.gcmID));
                    sessionManager.setUsername(jsonObject.getString(ServerConstants.username));
                    sessionManager.setIsGcmRegistered(true);
                    sessionManager.setAppVersion(ApplicationConstants.getAppVersion(this));

                    Intent intent = new Intent(this,TebakanListActivity.class);
                    startActivity(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
