package santana.tebaktebakan.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import santana.tebaktebakan.R;

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

        initUI();
        buttonAction();
        getValuefromBundle();
    }

    private void getValuefromBundle(){
        Intent intent = getIntent();
        try {
            if(intent.getExtras()!=null){
                lvl = intent.getExtras().getInt("LEVEL_STAGE");
                if (lvl<10){
                    txtLevel.setText("0"+lvl);
                } else {
                    txtLevel.setText(""+lvl);
                }
                Log.d("LVL BRo ", String.valueOf(lvl));
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }


    }

    private void buttonAction() {
        tebakKata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StageActivity.this, AnswerTebakKataActivity.class);
                startActivity(intent);
            }
        });
        tebakGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StageActivity.this, ChooseImageTebakanActivity.class);
                intent.putExtra("LEVEL_STAGE",lvl);
                startActivity(intent);
            }
        });
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

    private void initUI() {
        txtLevel = (AppCompatTextView) findViewById(R.id.txtLvl);
        tebakKata = (AppCompatTextView) findViewById(R.id.TebakKata);
        tebakGambar = (AppCompatTextView) findViewById(R.id.TebakGambar);
        btnBack = (ImageView) findViewById(R.id.btnBack);
    }
}
