package santana.tebaktebakan.controller.tebakanManager;

import android.app.Activity;
import android.support.v7.widget.AppCompatTextView;

import santana.tebaktebakan.controller.SessionManager.SessionCoin;

/**
 * Created by Gujarat Santana on 24/12/15.
 */
public class CoinsManager {
    public static CoinsManager instance;
    private int onHintActivity = 10;
    private int onDisplayChar = 60;
    private int onRighAnswer = 15;


    private CoinsManager() {}

    public static CoinsManager getInstance() {
        if (instance == null) instance = new CoinsManager();
        return instance;
    }



    public void setCoinForUI(Activity activity, AppCompatTextView coins){
        SessionCoin sessionCoin = new SessionCoin(activity);
        coins.setText(String.valueOf(sessionCoin.getCoins()));
    }

    public void setCoinsOnHintActivity(Activity activity){
        SessionCoin sessionCoin = new SessionCoin(activity);
        int result = sessionCoin.getCoins();
        if(result>0){
            result = result-onHintActivity;
            if(result <0){
                result=0;
            }
        }
        sessionCoin.setCoins(result);
    }

    public void setCoinsOnDisplayChar(Activity activity){
        SessionCoin sessionCoin = new SessionCoin(activity);
        int result = sessionCoin.getCoins();
        if(result>0){
            result = result-onDisplayChar;
            if(result <0){
                result=0;
            }
        }
        sessionCoin.setCoins(result);
    }

    public void setCoinOnRightAnswer(Activity activity){
        SessionCoin sessionCoin = new SessionCoin(activity);
        int result = sessionCoin.getCoins();
        if(result>0){
            result = result+onRighAnswer;
            if(result <0){
                result=0;
            }
        }
        sessionCoin.setCoins(result);
    }

    public void setCoinsOnAdsResult(Activity activity,int amountCoins){
        SessionCoin sessionCoin =new SessionCoin(activity);
        int resultCoins = sessionCoin.getCoins() + amountCoins;
        sessionCoin.setCoins(resultCoins);
    }

}
