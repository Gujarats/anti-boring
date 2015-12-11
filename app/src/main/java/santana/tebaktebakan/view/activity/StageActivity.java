package santana.tebaktebakan.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;

/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class StageActivity extends AppCompatActivity {

    @Bind(R.id.TebakKata) AppCompatTextView tebakKata;
    @Bind(R.id.txtLvl) AppCompatTextView txtLevel;
    @Bind(R.id.TebakGambar) AppCompatTextView tebakGambar;
    @Bind(R.id.btnBack) LinearLayout btnBack;
    private int lvl;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_stage_activity);
        //initial Interface
        initUI();
        // initial for action button or widget
        initAction();
    }

    private void initAction() {

        LogicInterfaceManager.getInstance().backAction(this, btnBack);
        tebakKata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnswerTebakKataActivity.class);
                startActivity(intent);
            }
        });
        tebakGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ChooseImageTebakanActivity.class);
                intent.putExtra("LEVEL_STAGE",lvl);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        ButterKnife.bind(this);
        txtLevel = (AppCompatTextView) findViewById(R.id.txtLvl);
        tebakKata = (AppCompatTextView) findViewById(R.id.TebakKata);
        tebakGambar = (AppCompatTextView) findViewById(R.id.TebakGambar);

        //set effect onclick and action
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnBack);

        //get level value and set it to textView
        lvl = LogicInterfaceManager.getInstance().getLevel(this);
        LogicInterfaceManager.getInstance().setTextViewLevel(lvl,txtLevel);
    }
}
