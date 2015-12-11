package santana.tebaktebakan.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;
import santana.tebaktebakan.controller.tebakanManager.Tebakan;
import santana.tebaktebakan.model.object.TebakanGambarObject;

/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class ChooseImageTebakanActivity extends AppCompatActivity {

    @Bind(R.id.btnBack)
    LinearLayout btnBack;
    @Bind(R.id.gambar1) ImageView m1;
    @Bind(R.id.gambar2) ImageView m2;
    @Bind(R.id.gambar3) ImageView m3;
    @Bind(R.id.txtLvl) AppCompatTextView txtLevel;
    private int lvl;

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

    private void initAction() {
        LogicInterfaceManager.getInstance().backAction(this, btnBack);
    }



    private void initUI() {
        //finding id from resources
        ButterKnife.bind(this);

        //set effect onclick and action
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnBack);
        //get level value and set it to textView
        lvl = LogicInterfaceManager.getInstance().getLevel(this);
        LogicInterfaceManager.getInstance().setTextViewLevel(lvl,txtLevel);
    }

    private void initGambar() {
        List<TebakanGambarObject> tebakanGambarObjects = Tebakan.getInstance().getImageLevel(lvl, this);
        for (int i = 0; i < tebakanGambarObjects.size(); i++) {
            if (!tebakanGambarObjects.get(i).getGambarUrl().isEmpty()) {
                switch (i) {
                    case 0:
                        Tebakan.getInstance().loadImageToImageView(m1, tebakanGambarObjects.get(i).getGambarUrl(), getApplicationContext());
                        Tebakan.getInstance().setOnClickTebakGambar(ChooseImageTebakanActivity.this, m1, tebakanGambarObjects.get(i));
                        break;
                    case 1:
                        Tebakan.getInstance().loadImageToImageView(m2, tebakanGambarObjects.get(i).getGambarUrl(), getApplicationContext());
                        Tebakan.getInstance().setOnClickTebakGambar(ChooseImageTebakanActivity.this, m2, tebakanGambarObjects.get(i));
                        break;
                    case 2:
                        Tebakan.getInstance().loadImageToImageView(m3, tebakanGambarObjects.get(i).getGambarUrl(), getApplicationContext());
                        Tebakan.getInstance().setOnClickTebakGambar(ChooseImageTebakanActivity.this, m3, tebakanGambarObjects.get(i));
                        break;
                    default:
                        break;
                }
            } else {
                Toast.makeText(ChooseImageTebakanActivity.this, "Sorry couldnt load image", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
