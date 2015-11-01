package santana.tebaktebakan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import santana.tebaktebakan.R;

/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class AnswerTebakKataActivity extends AppCompatActivity {

    protected ImageView btnBack, btnCek, btnHelp, btnShare;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer_tebak_kata);

        initUI();
        buttonAction();
    }

    private void buttonAction() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initUI() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnCek = (ImageView) findViewById(R.id.btnCek);
        btnHelp = (ImageView) findViewById(R.id.btnHelp);
        btnShare = (ImageView) findViewById(R.id.btnShare);
    }
}
