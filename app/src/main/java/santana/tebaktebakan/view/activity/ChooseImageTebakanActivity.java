package santana.tebaktebakan.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import santana.tebaktebakan.R;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;
import santana.tebaktebakan.controller.tebakanManager.Tebakan;
import santana.tebaktebakan.model.object.TebakanGambarObject;

/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class ChooseImageTebakanActivity extends AppCompatActivity {

    private ImageView m1, m2, m3, btnBack;
    private AppCompatTextView txtLevel;
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
        txtLevel = (AppCompatTextView) findViewById(R.id.txtLvl);
        m1 = (ImageView) findViewById(R.id.gambar1);
        m2 = (ImageView) findViewById(R.id.gambar2);
        m3 = (ImageView) findViewById(R.id.gambar3);
        btnBack = (ImageView) findViewById(R.id.btnBack);

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
                        break;
                    case 1:
                        Tebakan.getInstance().loadImageToImageView(m2, tebakanGambarObjects.get(i).getGambarUrl(), getApplicationContext());
                        break;
                    case 2:
                        Tebakan.getInstance().loadImageToImageView(m3, tebakanGambarObjects.get(i).getGambarUrl(), getApplicationContext());
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
