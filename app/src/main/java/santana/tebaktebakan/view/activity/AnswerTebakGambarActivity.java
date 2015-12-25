package santana.tebaktebakan.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;
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


    private String jawabanTebakan;
    private String imageUrl;
    private int level;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer_tebak_gambar);

        //initial Interface
        initUI();
        // initial for action button or widget
        initAction();
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

        // set animation hint
        LogicInterfaceManager.getInstance().setAnimtaionEffectonHint(btnHelp);

        Map<String,String> dataIntent = LogicInterfaceManager.getInstance().getDataFromIntent(this);
        if(dataIntent.size()>0){
            imageUrl = dataIntent.get(ApplicationConstants.imageUrl);
            jawabanTebakan = dataIntent.get(ApplicationConstants.jawabanTebakan);
            level = Integer.parseInt(dataIntent.get(ApplicationConstants.level));
            Tebakan.getInstance().loadImageToImageView2(TebakGambar, imageUrl,getApplicationContext());

        }
    }


    private void initAction() {

        LogicInterfaceManager.getInstance().backAction(this, btnBack);
        LogicInterfaceManager.getInstance().showDialogForHint(this, btnHelp, jawabanTebakan, imageUrl);

        Tebakan.getInstance().checkAnswer(AnswerTebakGambarActivity.this, this, jawabanTebakanEditText, jawabanTebakan,level, btnCek);



    }


}
