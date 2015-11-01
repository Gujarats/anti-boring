package santana.tebaktebakan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import santana.tebaktebakan.R;
import santana.tebaktebakan.adapter.MainActivityAdapter;

/**
 * Created by AdrianEkaFikri on 11/1/2015.
 */
public class MainActivity extends AppCompatActivity{

    protected RecyclerView list_level;
    AppCompatButton btnMenu1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);


        initUI();
//        buttonAction();
    }

    private void buttonAction() {
        btnMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        list_level = (RecyclerView)findViewById(R.id.list_level);
        btnMenu1 = (AppCompatButton) findViewById(R.id.btnMenu1);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list_level.setLayoutManager(llm);
        list_level.setAdapter(new MainActivityAdapter(getApplicationContext(),this,R.layout.layout_item_ganjil));
    }
}
