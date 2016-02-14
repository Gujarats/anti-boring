package antiboring.game.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import antiboring.game.R;
import antiboring.game.controller.UIManager.LogicInterfaceManager;
import antiboring.game.controller.tebakanManager.GambarCompleteManager;
import antiboring.game.controller.tebakanManager.Tebakan;
import antiboring.game.model.object.MTebakanGambar;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class ChooseImageTebakanActivity extends AppCompatActivity {

    @Bind(R.id.btnBack)
    LinearLayout btnBack;
    @Bind(R.id.gambar1) ImageView gambar1;
    @Bind(R.id.gambar2) ImageView gambar2;
    @Bind(R.id.gambar3) ImageView gambar3;
    @Bind(R.id.answered1) RelativeLayout answered1;
    @Bind(R.id.answered2) RelativeLayout answered2;
    @Bind(R.id.answered3) RelativeLayout answered3;
    @Bind(R.id.txtLvl) AppCompatTextView txtLevel;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tebak_semua_gambar);
        //initial Interface
        initUI();
        initGambar();
        // initial for action button or widget
        initAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //load progress tebak gambar in this level
        GambarCompleteManager.getInstance().setGambarAnswerVisibility(this,level,answered1,answered2,answered3);

//        //check progress and save it to StageManger giving stars
//        if(GambarCompleteManager.getInstance().isAllTebakGambarComplete(this,level)){
//            StageManger.getInstance().setTebakGambarStageComplete(this,level);
//        }
    }

    private void initAction() {
        LogicInterfaceManager.getInstance().backAction(this, btnBack);
    }



    private void initUI() {
        //finding id from resources
        ButterKnife.bind(this);

        //set effect onclick and action
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnBack);
        //get level value and set it to textView
        level = LogicInterfaceManager.getInstance().getLevel(this);
        LogicInterfaceManager.getInstance().setTextViewLevel(level, txtLevel);
    }

    private void initGambar() {
        List<MTebakanGambar> tebakanGambarObjects = Tebakan.getInstance().getImageLevel(level, this);
        for (int i = 0; i < tebakanGambarObjects.size(); i++) {
            if (!tebakanGambarObjects.get(i).getGambarUrl().isEmpty()) {
                switch (i) {
                    case 0:
                        Tebakan.getInstance().loadImageToImageView(gambar1, tebakanGambarObjects.get(i).getGambarUrl(), getApplicationContext());
                        Tebakan.getInstance().setOnClickTebakGambar(ChooseImageTebakanActivity.this, gambar1, tebakanGambarObjects.get(i), GambarCompleteManager.keyGambar1);
                        break;
                    case 1:
                        Tebakan.getInstance().loadImageToImageView(gambar2, tebakanGambarObjects.get(i).getGambarUrl(), getApplicationContext());
                        Tebakan.getInstance().setOnClickTebakGambar(ChooseImageTebakanActivity.this, gambar2, tebakanGambarObjects.get(i),GambarCompleteManager.keyGambar2);
                        break;
                    case 2:
                        Tebakan.getInstance().loadImageToImageView(gambar3, tebakanGambarObjects.get(i).getGambarUrl(), getApplicationContext());
                        Tebakan.getInstance().setOnClickTebakGambar(ChooseImageTebakanActivity.this, gambar3, tebakanGambarObjects.get(i),GambarCompleteManager.keyGambar3);
                        break;
                    default:
                        break;
                }
            } else {
                Toast.makeText(ChooseImageTebakanActivity.this, "Sorry couldn't load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
