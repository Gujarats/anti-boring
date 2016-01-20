package antiboring.game.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import antiboring.game.R;
import antiboring.game.controller.UIManager.LogicInterfaceManager;
import antiboring.game.controller.appBilling.AppBillingManager;
import antiboring.game.controller.socialMedia.FacebookManager;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init Facebook
        FacebookManager.getInstance().InitFacebook(getApplicationContext());

        setContentView(R.layout.layout_setting_activity);

        initUi();
        initAction();

        //init app billing
        AppBillingManager.getInstance().initBillingMainActivity(getApplicationContext(),this);


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookManager.getInstance().getCallbackManager().onActivityResult(requestCode, resultCode, data);
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
        list.setAdapter(new SettingsAdapter(getApplicationContext(),SettingActivity.this,FacebookManager.getInstance()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppBillingManager.getInstance().onDestroy(this);
    }
}
