package santana.tebaktebakan.view.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;
import santana.tebaktebakan.controller.UIManager.UIAnimationManager;
import santana.tebaktebakan.controller.tebakanManager.CoinsManager;
import santana.tebaktebakan.controller.tebakanManager.UserPlayManager;
import santana.tebaktebakan.view.adapter.MainActivityAdapter;

/**
 * Created by AdrianEkaFikri on 11/1/2015.
 */
public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    /**
     * UI variable
     */
    @Bind(R.id.list_level) RecyclerView list_level;
    @Bind(R.id.coins) AppCompatTextView coins;
    @Bind(R.id.btnSetting)
    LinearLayout btnSetting;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    InterstitialAd mInterstitialAd;
    private RecyclerView.LayoutManager mLayoutManager;
    /**
     * Adapater
     */
    private MainActivityAdapter mainActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        // initial ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();

        //initial Interface
        initUI();
        // initial for action button or widget
        initAction();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //set Coins to UI
        CoinsManager.getInstance().setCoinForUI(MainActivity.this, coins);
        // load progress user
        onLoadProgressUser();
        //load ads
        if(mInterstitialAd.isLoaded()) {
            Log.i(TAG, "onLoadAds: "+UserPlayManager.getInstance().isUserPlayed(MainActivity.this));
            if(UserPlayManager.getInstance().isUserPlayed(MainActivity.this)){
                mInterstitialAd.show();
                UserPlayManager.getInstance().setUserPlayed(MainActivity.this, false);
            }

        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }


    private void onLoadProgressUser(){
        mainActivityAdapter.updateLevelJson();

    }


    private void initAction() {
        // set User Played to false for showing ads
        UserPlayManager.getInstance().setUserPlayed(MainActivity.this,false);

        LogicInterfaceManager.getInstance().startActivityAction(btnSetting,MainActivity.this,SettingActivity.class);
    }

    private void initUI() {
        //finding id from resources
        ButterKnife.bind(this);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView headerIcon = (ImageView) findViewById(R.id.headerIcon);

        mainActivityAdapter = new MainActivityAdapter(getApplicationContext(),this,R.layout.item_level);




        // set onClick Effect
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnSetting);

        //initial header bar animation
        UIAnimationManager.getInstance().setAnimationHeader(getApplicationContext(), this, collapsingToolbarLayout, headerIcon, R.drawable.icon_utama);

        //layout for item view
        mLayoutManager = new GridLayoutManager(this, 2);
        list_level.setLayoutManager(mLayoutManager);
        list_level.setAdapter(mainActivityAdapter);

    }

}
