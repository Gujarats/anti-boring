package santana.tebaktebakan.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import santana.tebaktebakan.R;
import santana.tebaktebakan.loadTebakan.Tebakan;
import santana.tebaktebakan.object.TebakanGambarObject;

/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class ChooseImageTebakanActivity extends AppCompatActivity {

    private ImageView m1,m2,m3, btnBack;
    private AppCompatTextView txtLevel;
    private int lvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tebak_semua_gambar);
        initUI();
        getValuefromBundle(txtLevel);
        initGambar();
        buttonAction();
    }

    private void buttonAction() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
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
        btnBack = (ImageView) findViewById(R.id.btnBack);
    }

    private void initGambar(){
        List<TebakanGambarObject> tebakanGambarObjects= Tebakan.getInstance().getImageLevel(lvl, this);
        for(int i=0;i<tebakanGambarObjects.size();i++){
            if(!tebakanGambarObjects.get(i).getGambarUrl().isEmpty()){
                switch (i){
                    case 0 :
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
            }else{
                Toast.makeText(ChooseImageTebakanActivity.this, "Sorry couldnt load image", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
