package santana.tebaktebakan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import santana.tebaktebakan.R;

/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class StageActivity extends AppCompatActivity {

    protected AppCompatTextView tebakKata, tebakGambar;
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
                lvl = intent.getExtras().getInt("lvl");
                Log.d("LVL BRo ",String.valueOf(lvl));
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
    }

    private void initUI() {
        tebakKata = (AppCompatTextView) findViewById(R.id.TebakKata);
        tebakGambar = (AppCompatTextView) findViewById(R.id.TebakGambar);
    }
}
