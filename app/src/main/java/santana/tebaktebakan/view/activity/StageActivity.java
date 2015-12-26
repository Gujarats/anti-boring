package santana.tebaktebakan.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;
import santana.tebaktebakan.controller.tebakanManager.StageManger;
import santana.tebaktebakan.controller.tebakanManager.Tebakan;
import santana.tebaktebakan.model.object.TebakanKataObject;

/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class StageActivity extends AppCompatActivity {

    @Bind(R.id.TebakKata) RelativeLayout tebakKata;
    @Bind(R.id.txtLvl) AppCompatTextView txtLevel;
    @Bind(R.id.TebakGambar) RelativeLayout tebakGambar;
    @Bind(R.id.btnBack) LinearLayout btnBack;
    @Bind(R.id.starKata) ImageView starKata;
    @Bind(R.id.starGambar) ImageView starGambar;
    private int level;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_stage_activity);
        //initial Interface
        initUI();
        // initial for action button or widget
        initAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StageManger.getInstance().setStarsStageVisibility(this, level, starKata, starGambar);

    }

    private void initAction() {

        LogicInterfaceManager.getInstance().backAction(this, btnBack);
        tebakKata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnswerTebakKataActivity.class);
                TebakanKataObject tebakanKataObject = Tebakan.getInstance().getTebakKata(level,StageActivity.this);
                intent.putExtra(ApplicationConstants.tebakanKata,tebakanKataObject.getTebakKata());
                intent.putExtra(ApplicationConstants.jawabanTebakan,tebakanKataObject.getJawabanTebakKata());
                intent.putExtra(ApplicationConstants.level, level);

                startActivity(intent);
            }
        });
        tebakGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ChooseImageTebakanActivity.class);
                intent.putExtra(ApplicationConstants.level, level);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        ButterKnife.bind(this);

        //set effect onclick and action
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnBack);

        //get level value and set it to textView
        level = LogicInterfaceManager.getInstance().getLevel(this);
        LogicInterfaceManager.getInstance().setTextViewLevel(level, txtLevel);

    }

}
