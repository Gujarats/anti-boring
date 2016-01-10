package antiboring.game.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class AnswerTebakKataActivity extends AppCompatActivity {
    @Bind(R.id.btnBack) LinearLayout btnBack;
    @Bind(R.id.layoutTebakKata) LinearLayout layoutTebakKata;
    @Bind(R.id.btnCek) ImageView btnCek;
    @Bind(R.id.btnHelp) ImageView btnHelp;
    @Bind(R.id.btnShare) ImageView btnShare;
    @Bind(R.id.TebakKata) TextView TebakKata;
    @Bind(R.id.jawabanTebakanEditText) EditText jawabanTebakanEditText;
    @Bind(R.id.coins) AppCompatTextView coins;
    @Bind(R.id.gift) ImageView gift;
    private String jawabanTebakan;
    private int level;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init facebook
        FacebookManager.getInstance().InitFacebook(getApplicationContext());

        setContentView(R.layout.layout_answer_tebak_kata);

        //initial Interface
        initUI();
        // initial for action button or widget
        initAction();
        // init ads
        initAdColony();

        //init Twitter
        TwitterManager.getInstance().initTwitterTebakKata(this, getApplicationContext(), TebakKata.getText().toString());

        //init appbilling
        AppBillingManager.getInstance().initBillingMainActivity(getApplicationContext(),this);
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookManager.getInstance().getCallbackManager().onActivityResult(requestCode, resultCode, data);
        TwitterManager.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        DisableAnswerManager.getInstance().disableAnswerforTebakKata(this, level, btnCek, btnHelp);
        CoinsManager.getInstance().setCoinForUI(AnswerTebakKataActivity.this, coins);
        AdColony.resume(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        AdColony.pause();
    }

    private void initAdColony(){
        AdColonyManager.getInstance().setUpAdColony(AnswerTebakKataActivity.this, gift, coins);
        AdColonyManager.getInstance().setOnClickGfit(AnswerTebakKataActivity.this, gift);
    }

    private void initAction() {
        LogicInterfaceManager.getInstance().backAction(this, btnBack);
        Tebakan.getInstance().checkAnswerTebakKata(AnswerTebakKataActivity.this, this, level, jawabanTebakanEditText, jawabanTebakan, level, btnCek);

        LogicInterfaceManager.getInstance().showDialogSocialMediaTebakKata(getApplicationContext(), this, btnShare, TebakKata.getText().toString());

        LogicInterfaceManager.getInstance().showDialogForHintTebakKata(this,btnHelp,jawabanTebakan,TebakKata.getText().toString(),level);

    }

    private void initUI() {
        ButterKnife.bind(this);

        //set Effect on widget
        LogicInterfaceManager.getInstance().setOnClickEffect(this,btnCek);
        LogicInterfaceManager.getInstance().setOnClickEffect(this,btnHelp);
        LogicInterfaceManager.getInstance().setOnClickEffect(this,btnShare);
        LogicInterfaceManager.getInstance().setOnClickEffect(this,btnBack);
        // set animation hint
        LogicInterfaceManager.getInstance().setAnimtaionEffectonHint(btnHelp);

        //set size layout
        Tebakan.getInstance().setSizeLinearLayout(this,layoutTebakKata);

        Map<String,String> dataIntent = LogicInterfaceManager.getInstance().getDataFromIntentTebakKata(this);
        if(dataIntent.size()>0){
            TebakKata.setText(dataIntent.get(ApplicationConstants.tebakanKata));
            jawabanTebakan = dataIntent.get(ApplicationConstants.jawabanTebakan);
            level = Integer.parseInt(dataIntent.get(ApplicationConstants.level));
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppBillingManager.getInstance().onDestroy(this);
    }
}
