package santana.tebaktebakan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import santana.tebaktebakan.R;
import santana.tebaktebakan.RegistrationIntentService;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.common.ServerConstants;
import santana.tebaktebakan.email.EmailValidator;
import santana.tebaktebakan.session.SessionManager;

/**
 * Created by Gujarat Santana on 15/06/15.
 */
public class RegisterActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    /**
     * gcm variable
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    protected AppCompatEditText Email,password,rePassword,username;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register_activity);
        sessionManager = new SessionManager(getApplicationContext());
        initUI();
    }

    private void initUI(){
        Email = (AppCompatEditText) findViewById(R.id.EmailRegister);
        password = (AppCompatEditText) findViewById(R.id.PasswordRegister);
        rePassword = (AppCompatEditText) findViewById(R.id.rePaswordRegister);
        username = (AppCompatEditText) findViewById(R.id.UserNameRegister);
    }

    public void Register(View v){
        String password1 = password.getText().toString();
        String password2 = rePassword.getText().toString();
        String email = Email.getText().toString();
        String userNameBrow = username.getText().toString();

        if(password1.equals(password2)){

            if(email!=null && email.trim().length()!=0){
                EmailValidator emailValidator = new EmailValidator();

                if(emailValidator.validate(email)){

                    if(userNameBrow!=null && userNameBrow.trim().length()>0){
                        sessionManager.setEmail(email);
                        sessionManager.setPassword(password1);
                        sessionManager.setUsername(userNameBrow);
                        if (checkPlayServices()) {
                            Log.d("RegistGCm","start");
                            Intent intent = new Intent(this, RegistrationIntentService.class);
                            intent.putExtra(ApplicationConstants.FromActivity,RegisterActivity.class.toString());
                            startService(intent);

                        }else{
                            Toast.makeText(getApplicationContext(),"Your Phone Doesnt Have GooglePlay Service",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Please Enter Uniqe Username",Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(getApplicationContext(),"Email is not Valid",Toast.LENGTH_LONG).show();
                }
            }

        }else{
            Toast.makeText(getApplicationContext(),"Pssword not match",Toast.LENGTH_LONG).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error", error.getMessage());
    }

    @Override
    public void onResponse(String response) {
        Log.d("response",response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getString(ServerConstants.statusBeckend).equalsIgnoreCase(ServerConstants.statusBeckendOk)){

                switch (jsonObject.getInt(ServerConstants.resultType)){
                    case ServerConstants.registerResult :
                        break;
                    case ServerConstants.registerGcmResult :
                        break;

                }

                sessionManager.setUidUser(jsonObject.getString(ServerConstants._idUser));

            }else{

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
