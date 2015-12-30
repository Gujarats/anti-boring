package antiboring.game.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Map;

import antiboring.game.R;
import antiboring.game.common.ApplicationConstants;
import antiboring.game.controller.UIManager.LogicInterfaceManager;
import antiboring.game.controller.tebakanManager.HintsManager;
import antiboring.game.controller.tebakanManager.Tebakan;
import antiboring.game.controller.tebakanManager.UserPlayManager;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Gujarat Santana on 16/12/15.
 */
public class HintsTebakGambarActivity extends AppCompatActivity {


    private static final String TAG = "Hints";
    @Bind(R.id.layoutKeyBoard)
    LinearLayout layoutKeyBoard;
    @Bind(R.id.btnBack) LinearLayout btnBack;
    @Bind(R.id.hintDisplay) LinearLayout hintDisplay;
    @Bind(R.id.TebakGambar)
    ImageView TebakGambar;
    @Bind(R.id.hint)
    AppCompatTextView hint;
    /*
    * List keyboard button
    * */
    @Bind({ R.id.key1, R.id.key2, R.id.key3,R.id.key4,R.id.key5,R.id.key6,R.id.key7,R.id.key8,R.id.key9,
            R.id.key10,R.id.key11,R.id.key12,R.id.key13,R.id.key14,R.id.key15,R.id.key16})
    List<AppCompatButton> keyboardKeys;
        @Bind(R.id.keyDelete) LinearLayout keyDelete;


    private String jawabanTebakan,imageUrl,idGambar;
    private int level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hint_activity);

        initUi();
        initAction();
    }

    private void initUi(){
        ButterKnife.bind(this);
        // setOnClick effect
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnBack);

        // set size imageVIew for TebakGambar
        Tebakan.getInstance().setSizeImageView(this, TebakGambar);


        /*1. get data from previous intent*/
        Map<String,String> getDataIntent = LogicInterfaceManager.getInstance().getDataFromIntent(this);
        imageUrl = getDataIntent.get(ApplicationConstants.imageUrl);
        jawabanTebakan = getDataIntent.get(ApplicationConstants.jawabanTebakan);
        level = Integer.parseInt(getDataIntent.get(ApplicationConstants.level));
        idGambar = getDataIntent.get(ApplicationConstants.keyGambar);
        Tebakan.getInstance().loadImageToImageView2(TebakGambar, imageUrl, getApplicationContext());

        /*2. init keyboardView*/
        HintsManager.getInstance().setKeyboard(keyboardKeys,jawabanTebakan);

        /*3. set hint for the first time*/
        HintsManager.getInstance().setHintFirstTime(hint,jawabanTebakan);

        /*4. set session for index hint to second char in sour answer*/
        HintsManager.getInstance().setIndexHint(this,2);



    }

    private void initAction(){
        LogicInterfaceManager.getInstance().backAction(this, btnBack);

        for(int i=0;i<keyboardKeys.size();i++){

            final String keyboard = keyboardKeys.get(i).getText().toString();
            keyboardKeys.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String hintDisplayText = hint.getText().toString();
                    String result = HintsManager.getInstance().setKeyboardValue(keyboard,hintDisplayText);
                    Log.i(TAG, "onClick: " + result);
                    hint.setText(result);

                    HintsManager.getInstance().checkAnswerHintTebakGambar(HintsTebakGambarActivity.this, result, jawabanTebakan, level, idGambar);

                    // set User Played to true for showing ads
                    UserPlayManager.getInstance().setUserPlayed(HintsTebakGambarActivity.this, true);

                }
            });

        }

        HintsManager.getInstance().setDeleteAction(hint,keyDelete);


        HintsManager.getInstance().setOnDisplayDialogCharHintTebakGambar(this, hintDisplay, hint, jawabanTebakan,level,idGambar);



    }





}
