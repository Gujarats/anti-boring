package santana.tebaktebakan.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import santana.tebaktebakan.R;
import santana.tebaktebakan.controller.UIManager.UserInterfaceManager;

/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class AnswerTebakKataActivity extends AppCompatActivity {

    protected ImageView btnBack, btnCek, btnHelp, btnShare;


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
        UserInterfaceManager.getInstance().backAction(this,btnBack);
    }

    private void initUI() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnCek = (ImageView) findViewById(R.id.btnCek);
        btnHelp = (ImageView) findViewById(R.id.btnHelp);
        btnShare = (ImageView) findViewById(R.id.btnShare);

        //set Effect on widget
        UserInterfaceManager.getInstance().setOnClickEffect(this,btnBack);
        UserInterfaceManager.getInstance().setOnClickEffect(this,btnCek);
        UserInterfaceManager.getInstance().setOnClickEffect(this,btnHelp);
        UserInterfaceManager.getInstance().setOnClickEffect(this,btnShare);
    }
}
