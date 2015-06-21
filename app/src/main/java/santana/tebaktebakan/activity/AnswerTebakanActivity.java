package santana.tebaktebakan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import santana.tebaktebakan.AppController;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class AnswerTebakanActivity extends AppCompatActivity {

    protected AppCompatEditText AnswerTebakan;
    protected AppCompatTextView TextTebakan;
    protected NetworkImageView gambarTebakan;

    private String _idTebakan,_idUser,textTebakan,gambarUrl,gcmID,kunciTebakan;

    //image Loader
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer_tebakan_activity);
        /***
         * get value from intent
         */
        Bundle intent = getIntent().getExtras();
        if(intent.getString(ApplicationConstants.FromActivity).equals(TebakanListActivity.class.toString())){
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

            }else{
                //Wrong answer give minus point if zero still zero
                Toast.makeText(getApplicationContext(),"Wrong Answer",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Please Answer",Toast.LENGTH_LONG).show();
        }
    }


}
