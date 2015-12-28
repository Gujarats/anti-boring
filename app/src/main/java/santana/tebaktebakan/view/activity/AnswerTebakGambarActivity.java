package santana.tebaktebakan.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jirbo.adcolony.AdColony;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;
import santana.tebaktebakan.controller.adsManager.AdColonyManager;
import santana.tebaktebakan.controller.tebakanManager.CoinsManager;
import santana.tebaktebakan.controller.tebakanManager.DisableAnswerManager;
import santana.tebaktebakan.controller.tebakanManager.Tebakan;


/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class AnswerTebakGambarActivity extends AppCompatActivity {


    @Bind(R.id.btnBack) LinearLayout btnBack;
    @Bind(R.id.btnCek) ImageView btnCek;
    @Bind(R.id.btnHelp) ImageView btnHelp;
    @Bind(R.id.btnShare) ImageView btnShare;
    @Bind(R.id.TebakGambar) ImageView TebakGambar;
    @Bind(R.id.layoutAnim) LinearLayout layoutAnim;
    @Bind(R.id.jawabanTebakan) EditText jawabanTebakanEditText;
    @Bind(R.id.coins) AppCompatTextView coins;
    @Bind(R.id.gift) ImageView gift;



    private String jawabanTebakan;
    private String imageUrl;
    private int level;
    private String keyGambar;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer_tebak_gambar);


        //initial Interface
        initUI();
        // initial for action button or widget
        initAction();
        // init ads
        initAdColony();
    }

    private void initAdColony(){
        AdColonyManager.getInstance().setUpAdColony(AnswerTebakGambarActivity.this,gift,coins);
        AdColonyManager.getInstance().setOnClickGfit(AnswerTebakGambarActivity.this,gift);
    }



    private void initUI() {
        ButterKnife.bind(this);

        // set size imageVIew for TebakGambar
        Tebakan.getInstance().setSizeImageView(this, TebakGambar);

        //set Effect on widget
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnBack);
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnCek);
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnHelp);
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnShare);
        LogicInterfaceManager.getInstance().setOnClickEffect(this, gift);

        // set animation hint
        LogicInterfaceManager.getInstance().setAnimtaionEffectonHint(btnHelp);

        //get Data from intent for UI
        Map<String,String> dataIntent = LogicInterfaceManager.getInstance().getDataFromIntent(this);
        if(dataIntent.size()>0){
            imageUrl = dataIntent.get(ApplicationConstants.imageUrl);
            jawabanTebakan = dataIntent.get(ApplicationConstants.jawabanTebakan);
            level = Integer.parseInt(dataIntent.get(ApplicationConstants.level));
            keyGambar = dataIntent.get(ApplicationConstants.keyGambar);
            Tebakan.getInstance().loadImageToImageView2(TebakGambar, imageUrl,getApplicationContext());

        }


    }




    private void initAction() {

        // set setaction back and showing dialong
        LogicInterfaceManager.getInstance().backAction(this, btnBack);
        LogicInterfaceManager.getInstance().showDialogForHint(this, btnHelp, jawabanTebakan, imageUrl,level, keyGambar);

        //set action for check answer
        Tebakan.getInstance().checkAnswer(AnswerTebakGambarActivity.this, this, jawabanTebakanEditText, jawabanTebakan,level, btnCek, keyGambar);

    }

    @Override
    public void onResume(){
        super.onResume();
        AdColony.resume(this);
        DisableAnswerManager.getInstance().disableAnswerforTebakGambar(this, level, btnCek, btnHelp, keyGambar);
        CoinsManager.getInstance().setCoinForUI(AnswerTebakGambarActivity.this, coins);
    }

    @Override
    public void onPause(){
        super.onPause();
        AdColony.pause();
    }
}
