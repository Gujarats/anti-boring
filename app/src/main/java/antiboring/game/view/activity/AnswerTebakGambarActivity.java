package antiboring.game.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jirbo.adcolony.AdColony;

import java.util.Map;

import antiboring.game.R;
import antiboring.game.common.ApplicationConstants;
import antiboring.game.controller.UIManager.LogicInterfaceManager;
import antiboring.game.controller.adsManager.AdColonyManager;
import antiboring.game.controller.appBilling.AppBillingManager;
import antiboring.game.controller.socialMedia.FacebookManager;
import antiboring.game.controller.socialMedia.twitter.TwitterManager;
import antiboring.game.controller.tebakanManager.CoinsManager;
import antiboring.game.controller.tebakanManager.DisableAnswerManager;
import antiboring.game.controller.tebakanManager.Tebakan;
import butterknife.Bind;
import butterknife.ButterKnife;


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

    //twitter
    private TwitterManager twitterManager;



    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init facebook
        FacebookManager.getInstance().InitFacebook(getApplicationContext());

        setContentView(R.layout.layout_answer_tebak_gambar);


        //initial Interface
        initUI();
        // initial for action button or widget
        initAction();
        // init ads
        initAdColony();

        //init Twitter
        TwitterManager.getInstance().initTwitterTebakGambar(this, getApplicationContext(), imageUrl);

        //init appbilling
        AppBillingManager.getInstance().initBillingMainActivity(getApplicationContext(),this);
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

        // set action back and showing dialong
        LogicInterfaceManager.getInstance().backAction(this, btnBack);
        LogicInterfaceManager.getInstance().showDialogForHint(this, btnHelp, jawabanTebakan, imageUrl, level, keyGambar);
        LogicInterfaceManager.getInstance().showDialogSocialMediaTebakGambar(getApplicationContext(), this, btnShare, imageUrl);


        //set action for check answer
        Tebakan.getInstance().checkAnswer(AnswerTebakGambarActivity.this, this, jawabanTebakanEditText, jawabanTebakan,level, btnCek, keyGambar);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookManager.getInstance().getCallbackManager().onActivityResult(requestCode, resultCode, data);
        TwitterManager.getInstance().onActivityResult(this, requestCode, resultCode, data);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppBillingManager.getInstance().onDestroy(this);
    }
}
