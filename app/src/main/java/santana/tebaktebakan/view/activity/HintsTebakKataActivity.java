package santana.tebaktebakan.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;
import santana.tebaktebakan.controller.tebakanManager.HintsManager;
import santana.tebaktebakan.controller.tebakanManager.Tebakan;

/**
 * Created by Gujarat Santana on 21/12/15.
 */
public class HintsTebakKataActivity extends AppCompatActivity {
    @Bind(R.id.btnBack) LinearLayout btnBack;
    @Bind(R.id.layoutTebakKata) LinearLayout layoutTebakKata;
    @Bind(R.id.hint) AppCompatTextView hint;
    @Bind(R.id.tebakKata) AppCompatTextView tebakKataText;



    /*List keyboard button*/
    @Bind({ R.id.key1, R.id.key2, R.id.key3,R.id.key4,R.id.key5,R.id.key6,R.id.key7,R.id.key8,R.id.key9,
            R.id.key10,R.id.key11,R.id.key12,R.id.key13,R.id.key14,R.id.key15,R.id.key16})
    List<AppCompatButton> keyboardKeys;
    @Bind(R.id.keyDelete) AppCompatButton keyDelete;

    /*varible global*/
    private String tebakKata;
    private String jawabanTebakan;
    private int level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hint_tebak_kata);

        initUi();
        initAction();
    }

    private void initAction(){
        LogicInterfaceManager.getInstance().backAction(this, btnBack);

        for(int i=0;i<keyboardKeys.size();i++){

            final String keyboard = keyboardKeys.get(i).getText().toString();
            keyboardKeys.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String source = hint.getText().toString();
                    String result = HintsManager.getInstance().setKeyboardValue(keyboard,source);
                    hint.setText(result);

                    HintsManager.getInstance().checkAnswerHintTebakKata(HintsTebakKataActivity.this, result, jawabanTebakan, level);

                }
            });

        }

        HintsManager.getInstance().setDeleteAction(hint, keyDelete);

    }

    private void initUi(){
        ButterKnife.bind(this);

        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnBack);

        // set size imageVIew for TebakGambar
        Tebakan.getInstance().setSizeLinearLayout(this, layoutTebakKata);


        /*1. get data from previous intent*/
        Map<String,String> getDataIntent = LogicInterfaceManager.getInstance().getDataFromIntentTebakKata(this);
        tebakKata = getDataIntent.get(ApplicationConstants.tebakanKata);
        jawabanTebakan = getDataIntent.get(ApplicationConstants.jawabanTebakan);
        level = Integer.parseInt(getDataIntent.get(ApplicationConstants.level));
        tebakKataText.setText(tebakKata);


        /*2. init keyboardView*/
        HintsManager.getInstance().setKeyboard(keyboardKeys,jawabanTebakan);

        /*3. set hint for the first time*/
        HintsManager.getInstance().setHintFirstTime(hint,jawabanTebakan);

    }
}
