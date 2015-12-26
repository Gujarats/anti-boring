package santana.tebaktebakan.controller.adsManager;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdAvailabilityListener;
import com.jirbo.adcolony.AdColonyAdListener;
import com.jirbo.adcolony.AdColonyV4VCAd;
import com.jirbo.adcolony.AdColonyV4VCListener;
import com.jirbo.adcolony.AdColonyV4VCReward;

/**
 * Created by Gujarat Santana on 26/12/15.
 */
public class AdColonyManager {
    public static AdColonyManager instance;
    final private String APP_ID  = "app843102734e634b7595";
    final private String ZONE_ID = "vzf3420409b7024beebc";
    private AdColonyManager() {}

    public static AdColonyManager getInstance() {
        if (instance == null) instance = new AdColonyManager();
        return instance;
    }

    public void setUpAdColony(final Activity activity, final ImageView gift){
        AdColony.configure(activity, "version:1.0,store:google", APP_ID, ZONE_ID);

        //Register an AdColonyAdAvailabilityListener to be notified of changes in a zone's
        //ad availability.
        AdColony.addAdAvailabilityListener(new AdColonyAdAvailabilityListener() {
            @Override
            public void onAdColonyAdAvailabilityChange(final boolean available, String zone_id) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //If zone has ads available, enable the button
                        if (available) {
                            gift.setVisibility(View.INVISIBLE);
                        } else {
                            gift.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        //Register an AdColonyV4VCListener to be notified of reward events
        AdColony.addV4VCListener(new AdColonyV4VCListener() {
            @Override
            public void onAdColonyV4VCReward(AdColonyV4VCReward reward) {
                if (reward.success())
                {
                    //Reward was successful, reward your user here
                    int amount  = reward.amount();
                    String name = reward.name();
                    Toast.makeText(activity, amount + " " + name + " awarded!", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (AdColony.statusForZone( ZONE_ID ).equals( "active" ))
        {
            gift.setVisibility(View.VISIBLE);
        }
    }

    public void setOnClickGfit(Activity activity,ImageView gift){
        gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdColonyV4VCAd ad = new AdColonyV4VCAd().withConfirmationDialog().withResultsDialog().withListener(new AdColonyAdListener() {
                    @Override
                    public void onAdColonyAdAttemptFinished(AdColonyAd adColonyAd) {

                    }

                    @Override
                    public void onAdColonyAdStarted(AdColonyAd adColonyAd) {

                    }
                });
                ad.show();
            }
        });
    }

}
