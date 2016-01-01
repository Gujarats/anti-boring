package antiboring.game.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import antiboring.game.R;
import antiboring.game.controller.UIManager.LogicInterfaceManager;
import antiboring.game.view.adapter.BuyCoinsAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Gujarat Santana on 01/01/16.
 */
public class BuyCoinsActivity extends AppCompatActivity {
    @Bind(R.id.list) RecyclerView list;
    @Bind(R.id.btnBack) LinearLayout btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_buy_coins_activity);

        initUi();
        initAction();
    }

    private void initAction(){
        LogicInterfaceManager.getInstance().backAction(this, btnBack);
    }

    private void initUi(){
        ButterKnife.bind(this);

        //set effect onclick and action
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnBack);

        //init ui list settings
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);
        list.setAdapter(new BuyCoinsAdapter(BuyCoinsActivity.this));
    }
}
