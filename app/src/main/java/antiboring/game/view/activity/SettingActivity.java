package antiboring.game.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import antiboring.game.R;
import antiboring.game.controller.UIManager.LogicInterfaceManager;
import antiboring.game.view.adapter.SettingsAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by AdrianEkaFikri on 11/2/2015.
 */
public class SettingActivity extends AppCompatActivity {

    @Bind(R.id.list) RecyclerView list;
    @Bind(R.id.btnBack) LinearLayout btnBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_activity);

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
        list.setAdapter(new SettingsAdapter(SettingActivity.this));
    }

}
