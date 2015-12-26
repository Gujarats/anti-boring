package santana.tebaktebakan.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;
import santana.tebaktebakan.controller.tebakanManager.Tebakan;
import santana.tebaktebakan.model.object.TebakanKataObject;

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

    private String jawabanTebakan;
    private int level;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer_tebak_kata);

        //initial Interface
        initUI();
        // initial for action button or widget
        initAction();
    }

    private void initAction() {
        LogicInterfaceManager.getInstance().backAction(this, btnBack);
        Tebakan.getInstance().checkAnswerTebakKata(AnswerTebakKataActivity.this, this, level,jawabanTebakanEditText, jawabanTebakan, level,btnCek);

        TebakanKataObject tebakanKataObject = new TebakanKataObject();
        tebakanKataObject.setJawabanTebakKata(jawabanTebakan);
        tebakanKataObject.setTebakKata(TebakKata.getText().toString());
        Tebakan.getInstance().setOnClickHintTebakKata(this, btnHelp, tebakanKataObject);

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
}
