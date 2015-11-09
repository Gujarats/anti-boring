package santana.tebaktebakan.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

import santana.tebaktebakan.R;
import santana.tebaktebakan.controller.UIManager.UserInterfaceManager;

/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class StageActivity extends AppCompatActivity {

    protected AppCompatTextView tebakKata, tebakGambar, txtLevel;
    protected ImageView btnBack;
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

        UserInterfaceManager.getInstance().backAction(this, btnBack);
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
        txtLevel = (AppCompatTextView) findViewById(R.id.txtLvl);
        tebakKata = (AppCompatTextView) findViewById(R.id.TebakKata);
        tebakGambar = (AppCompatTextView) findViewById(R.id.TebakGambar);
        btnBack = (ImageView) findViewById(R.id.btnBack);

        //set effect onclick and action
        UserInterfaceManager.getInstance().setOnClickEffect(this, btnBack);

        //get level value and set it to textView
        lvl = UserInterfaceManager.getInstance().getLevel(this);
        UserInterfaceManager.getInstance().setTextViewLevel(lvl,txtLevel);
    }
}
