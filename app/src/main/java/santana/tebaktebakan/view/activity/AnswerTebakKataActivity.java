package santana.tebaktebakan.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;

/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class AnswerTebakKataActivity extends AppCompatActivity {

    protected ImageView  btnCek, btnHelp, btnShare;
    @Bind(R.id.btnBack)
    LinearLayout btnBack;


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
        LogicInterfaceManager.getInstance().backAction(this,btnBack);
    }

    private void initUI() {
        ButterKnife.bind(this);
        btnCek = (ImageView) findViewById(R.id.btnCek);
        btnHelp = (ImageView) findViewById(R.id.btnHelp);
        btnShare = (ImageView) findViewById(R.id.btnShare);

        //set Effect on widget
        LogicInterfaceManager.getInstance().setOnClickEffect(this,btnBack);
        LogicInterfaceManager.getInstance().setOnClickEffect(this,btnCek);
        LogicInterfaceManager.getInstance().setOnClickEffect(this,btnHelp);
        LogicInterfaceManager.getInstance().setOnClickEffect(this,btnShare);
    }
}
