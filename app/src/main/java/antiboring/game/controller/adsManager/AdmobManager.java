package antiboring.game.controller.adsManager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import antiboring.game.controller.appBilling.AppBillingManager;
import antiboring.game.controller.tebakanManager.UserPlayManager;

/**
 * Created by Gujarat Santana on 09/01/16.
 */
public class AdmobManager {
    private static final String TAG = "AdmobManager";
    public static AdmobManager instance;
    private InterstitialAd mInterstitialAd;

    private AdmobManager() {}

    public static AdmobManager getInstance() {
        if (instance == null) instance = new AdmobManager();
        return instance;
    }

    public void initialAds(Context context){
        // initial ads
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
    }

    public void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void onResume(Activity activity){
        if(mInterstitialAd.isLoaded()) {
            Log.i(TAG, "onLoadAds: " + UserPlayManager.getInstance().isUserPlayed(activity));
            //check if user has played or not
            if(UserPlayManager.getInstance().isUserPlayed(activity)){
                // check if user is premium or not
                if(!AppBillingManager.getInstance().isPremiumUser(activity)){
                    mInterstitialAd.show();
                }
                UserPlayManager.getInstance().setUserPlayed(activity, false);
            }

        }
    }
}
