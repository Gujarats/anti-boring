package santana.tebaktebakan.view.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;
import santana.tebaktebakan.controller.UIManager.UIAnimationManager;
import santana.tebaktebakan.view.adapter.MainActivityAdapter;

/**
 * Created by AdrianEkaFikri on 11/1/2015.
 */
public class MainActivity extends AppCompatActivity{
    @Bind(R.id.list_level) RecyclerView list_level;
    @Bind(R.id.btnSetting)
    LinearLayout btnSetting;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        //initial Interface
        initUI();
        // initial for action button or widget
        initAction();
    }

    private void initAction() {
        LogicInterfaceManager.getInstance().startActivityAction(btnSetting,MainActivity.this,SettingActivity.class);
    }

    private void initUI() {
        //finding id from resources
        ButterKnife.bind(this);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView headerIcon = (ImageView) findViewById(R.id.headerIcon);


        // set onClick Effect
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnSetting);

        //initial header bar animation
        UIAnimationManager.getInstance().setAnimationHeader(getApplicationContext(),this,collapsingToolbarLayout,headerIcon,R.drawable.anti_boring);

        //layout for item view
        mLayoutManager = new GridLayoutManager(this, 2);
        list_level.setLayoutManager(mLayoutManager);
        list_level.setAdapter(new MainActivityAdapter(getApplicationContext(), this, R.layout.layout_grid_level));
    }

}
