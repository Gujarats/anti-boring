package santana.tebaktebakan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import santana.tebaktebakan.R;
import santana.tebaktebakan.loadTebakan.LoadTebakan;
import santana.tebaktebakan.object.TebakanGambarObject;

/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class ChooseImageTebakanActivity extends AppCompatActivity {

    private ImageView m1,m2,m3;
    private AppCompatTextView txtLevel;
    private int lvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tebak_semua_gambar);
        initUI();
        getValuefromBundle(txtLevel);
        initGambar();
    }

    private void getValuefromBundle(AppCompatTextView txtLevel){
        Intent intent = getIntent();
        try {
            if(intent.getExtras()!=null){
                lvl = intent.getExtras().getInt("LEVEL_STAGE");
                if (lvl<10){
                    txtLevel.setText("0"+lvl);
                } else {
                    txtLevel.setText(""+lvl);
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }


    }

    private void initUI(){
        txtLevel = (AppCompatTextView)findViewById(R.id.txtLvl);
        m1 = (ImageView)findViewById(R.id.gambar1);
        m2 = (ImageView)findViewById(R.id.gambar2);
        m3 = (ImageView)findViewById(R.id.gambar3);
    }

    private void initGambar(){
        List<TebakanGambarObject> tebakanGambarObjects=LoadTebakan.getInstance().getImageLevel(lvl, this);
        for(int i=0;i<tebakanGambarObjects.size();i++){
            if(!tebakanGambarObjects.get(i).getGambarUrl().isEmpty()){
                switch (i){
                    case 0 :
                        LoadTebakan.getInstance().loadImageToImageView(m1, tebakanGambarObjects.get(i).getGambarUrl(), getApplicationContext());
                        break;
                    case 1:
                        LoadTebakan.getInstance().loadImageToImageView(m2, tebakanGambarObjects.get(i).getGambarUrl(), getApplicationContext());
                        break;
                    case 2:
                        LoadTebakan.getInstance().loadImageToImageView(m3, tebakanGambarObjects.get(i).getGambarUrl(), getApplicationContext());
                        break;
                    default:
                        break;
                }
            }else{
                Toast.makeText(ChooseImageTebakanActivity.this, "Sorry coulnt load image", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
