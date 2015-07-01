package santana.tebaktebakan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

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
 * Created by Gujarat Santana on 12/06/15.
 */
public class AnswerTebakanActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    protected AppCompatEditText AnswerTebakan;
    protected AppCompatTextView TextTebakan;
    protected NetworkImageView gambarTebakan;

    private String _idTebakan,_idUser,textTebakan,gambarUrl,gcmID,kunciTebakan;
    private SessionManager sessionManager;

    //image Loader
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer_tebakan_activity);
        sessionManager = new SessionManager(getApplicationContext());
        /***
         * get value from intent
         */
        Bundle intent = getIntent().getExtras();
        if(intent.getString(ApplicationConstants.FromActivity).equals(ApplicationConstants.TebakanListActivity)){
            _idTebakan = intent.getString(ApplicationConstants._idTebakan);
            _idUser = intent.getString(ApplicationConstants._idUserTebakan);
            textTebakan = intent.getString(ApplicationConstants.textTebakan);
            kunciTebakan = intent.getString(ApplicationConstants.kunciTebakan);
            gambarUrl = intent.getString(ApplicationConstants.imageUrl);
            gcmID = intent.getString(ApplicationConstants.gcmID);
        }
        initUi();

    }

    private void initUi(){
        AnswerTebakan = (AppCompatEditText) findViewById(R.id.TextAnswer_Answer);
        TextTebakan = (AppCompatTextView) findViewById(R.id.TextTebakan_Answer);
        gambarTebakan = (NetworkImageView) findViewById(R.id.GambarTebakan_Answer);

        //set value
        TextTebakan.setText(textTebakan);
        if(imageLoader==null)
            imageLoader = AppController.getInstance().getImageLoader();
        gambarTebakan.setImageUrl(gambarUrl, imageLoader);
    }

    public void AnswerTebakan(View v){
        String answerTemp = AnswerTebakan.getText().toString();
        if(!answerTemp.trim().isEmpty()){
            if(answerTemp.equalsIgnoreCase(kunciTebakan)){
                //Answer is right give point and send messege to user who has tebakan
                Toast.makeText(getApplicationContext(),"Yeaahhh You Got Points !!!",Toast.LENGTH_LONG).show();
                /**
                 * request penambahan point untuk yang menjawab dengan benar.
                 */
                Map<String,String> mParams = new HashMap<String,String>();
                mParams.put(ServerConstants.mParams_idUser,sessionManager.getUidUser());
                mParams.put(ServerConstants.mParamsToken,sessionManager.getToken());
                CostumRequestString myReq = new CostumRequestString(Request.Method.POST,ServerConstants.jawabanBenar,mParams,AnswerTebakanActivity.this,AnswerTebakanActivity.this);
                myReq.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getInstance().addToRequestQueue(myReq);

            }else{
                //Wrong answer give minus point if zero still zero
                Toast.makeText(getApplicationContext(),"Wrong Answer",Toast.LENGTH_LONG).show();

            }
        }else{
            Toast.makeText(getApplicationContext(),"Please Answer",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error", error.getMessage());
        Toast.makeText(getApplicationContext(),"Wooww, Error man",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(String response) {
        Log.d("response",response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getString(ServerConstants.statusBeckend).equalsIgnoreCase(ServerConstants.statusBeckendOk)){

            }else{
                switch (jsonObject.getInt(ServerConstants.resultType)){
                    case ServerConstants.ErrorEmptyParams :
                        Toast.makeText(getApplicationContext(),"Oops Sorry, Somethings Wrong",Toast.LENGTH_LONG).show();
                        break;
                    case ServerConstants.ErrorDataNotFound :
                        Toast.makeText(getApplicationContext(),"Oops Sorry, Somethings Wrong",Toast.LENGTH_LONG).show();
                        break;
                    case ServerConstants.ErrorQuery:
                        Toast.makeText(getApplicationContext(),"Oops Sorry, Somethings Wrong",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
