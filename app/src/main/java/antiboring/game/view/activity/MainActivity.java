package antiboring.game.view.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.FacebookSdk;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import antiboring.game.R;
import antiboring.game.controller.UIManager.LogicInterfaceManager;
import antiboring.game.controller.UIManager.UIAnimationManager;
import antiboring.game.controller.tebakanManager.CoinsManager;
import antiboring.game.controller.tebakanManager.UserPlayManager;
import antiboring.game.view.adapter.MainActivityAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

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
        initFacebook();
        setContentView(R.layout.layout_main);

//        pritinKeyHashDevelopment();



        initialAds();
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

    private void initFacebook(){
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    private void pritinKeyHashDevelopment(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "antiboring.game",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void initialAds(){
        // initial ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7786975749587909/7517791075");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
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
        UIAnimationManager.getInstance().setAnimationHeader(getApplicationContext(), this, collapsingToolbarLayout, headerIcon, R.drawable.header_);

        //layout for item view
        mLayoutManager = new GridLayoutManager(this, 2);
        list_level.setLayoutManager(mLayoutManager);
        list_level.setAdapter(mainActivityAdapter);

    }

}
