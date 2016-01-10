package antiboring.game.controller.tebakanManager;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatTextView;

import antiboring.game.R;
import antiboring.game.controller.Session.SessionCoin;

/**
 * Created by Gujarat Santana on 24/12/15.
 */
public class CoinsManager {
    public static CoinsManager instance;
    private int onHintActivity = 10;
    private int onDisplayChar = 60;
    private int onRighAnswer = 5;
    private int onCompleteAllStars = 10;

    //logic buy coins
    private int hotOffer = 290;
    private int regular = 700;
    private int doubleRegular = 2000;
    private int awesomePack = 4500;
    private int bestOffer = 20000;



    private CoinsManager() {}

    public static CoinsManager getInstance() {
        if (instance == null) instance = new CoinsManager();
        return instance;
    }

    public void setHotOffer(Activity activity){
        SessionCoin sessionCoin = new SessionCoin(activity);
        int currentCoins = sessionCoin.getCoins();
        currentCoins +=hotOffer;
        sessionCoin.setCoins(currentCoins);
    }

    public void setRegular(Activity activity){
        SessionCoin sessionCoin = new SessionCoin(activity);
        int currentCoins = sessionCoin.getCoins();
        currentCoins +=regular;
        sessionCoin.setCoins(currentCoins);
    }

    public void setDoubleRegular(Activity activity){
        SessionCoin sessionCoin = new SessionCoin(activity);
        int currentCoins = sessionCoin.getCoins();
        currentCoins +=doubleRegular;
        sessionCoin.setCoins(currentCoins);
    }

    public void setAwesomePack(Activity activity){
        SessionCoin sessionCoin = new SessionCoin(activity);
        int currentCoins = sessionCoin.getCoins();
        currentCoins +=awesomePack;
        sessionCoin.setCoins(currentCoins);
    }

    public void setBestOffer(Activity activity){
        SessionCoin sessionCoin = new SessionCoin(activity);
        int currentCoins = sessionCoin.getCoins();
        currentCoins +=bestOffer;
        sessionCoin.setCoins(currentCoins);
    }

    public boolean isEnoughCoinHint(Activity activity){
        SessionCoin sessionCoin = new SessionCoin(activity);
        int currentCoins = sessionCoin.getCoins();
        if(currentCoins>=onHintActivity){
            return true;
        }else
            return false;
    }

    public boolean isEnoughCoinDislplayChat(Activity activity){
        SessionCoin sessionCoin = new SessionCoin(activity);
        int currentCoins = sessionCoin.getCoins();
        if(currentCoins>=onDisplayChar){
            return true;
        }else
            return false;
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
        if(result>=0){
            result = result+onRighAnswer;
            if(result <0){
                result=0;
            }
        }
        sessionCoin.setCoins(result);
    }

    public void setOnCompleteAllStars(Activity activity){
        SessionCoin sessionCoin = new SessionCoin(activity);
        int result = sessionCoin.getCoins();
        if(result>=0){
            result = result+onCompleteAllStars;
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

    public void showDialogZeroCoin(Activity activity){
        AppCompatDialog dialog = new AppCompatDialog(activity);
        dialog.setContentView(R.layout.dialog_zero_coin);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
