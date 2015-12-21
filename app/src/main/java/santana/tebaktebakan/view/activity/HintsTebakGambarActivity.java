package santana.tebaktebakan.view.activity;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;
import santana.tebaktebakan.controller.tebakanManager.HintsManager;
import santana.tebaktebakan.controller.tebakanManager.Tebakan;

/**
 * Created by Gujarat Santana on 16/12/15.
 */
public class HintsTebakGambarActivity extends AppCompatActivity {


    private static final String TAG = "Hints";
    @Bind(R.id.layoutKeyBoard)
    LinearLayout layoutKeyBoard;
    @Bind(R.id.btnBack) LinearLayout btnBack;
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
        @Bind(R.id.keyDelete) AppCompatButton keyDelete;


    private String jawabanTebakan,imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hint_activity);

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
                    Log.i(TAG, "onClick: "+result);
                    hint.setText(result);

                }
            });

        }

        HintsManager.getInstance().setDeleteAction(hint,keyDelete);

    }

    private void initUi(){
        ButterKnife.bind(this);

        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnBack);

        // set size imageVIew for TebakGambar
        Tebakan.getInstance().setSizeImageView(this, TebakGambar);


        /*1. get data from previous intent*/
        Map<String,String> getDataIntent = LogicInterfaceManager.getInstance().getDataFromIntent(this);
        imageUrl = getDataIntent.get(ApplicationConstants.imageUrl);
        jawabanTebakan = getDataIntent.get(ApplicationConstants.jawabanTebakan);
        Tebakan.getInstance().loadImageToImageView2(TebakGambar, imageUrl, getApplicationContext());

        /*2. init keyboardView*/
        HintsManager.getInstance().setKeyboard(keyboardKeys,jawabanTebakan);

        /*3. set hint for the first time*/
        HintsManager.getInstance().setHintFirstTime(hint,jawabanTebakan);

    }



}
