package antiboring.game.controller.tebakanManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import antiboring.game.R;
import antiboring.game.controller.Session.SessionCoin;
import antiboring.game.view.activity.BuyCoinsActivity;

/**
 * Created by Gujarat Santana on 24/12/15.
 */
public class CoinsManager {
    public static CoinsManager instance;
    //logic buy coins
    public static int hotOffer = 290;
    public static int premium = 400;
    public static int regular = 700;
    public static int doubleRegular = 2000;
    public static int awesomePack = 4500;
    public static int bestOffer = 20000;
    private int onHintActivity = 10;
    private int onDisplayChar = 60;
    private int onRighAnswer = 5;
    private int onCompleteAllStars = 10;

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

    public void setPremium(Activity activity){
        SessionCoin sessionCoin = new SessionCoin(activity);
        int currentCoins = sessionCoin.getCoins();
        currentCoins +=premium;
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

    public void showDialogZeroCoin(final Activity activity){
        AppCompatDialog dialog = new AppCompatDialog(activity);
        dialog.setContentView(R.layout.dialog_zero_coin);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout buyCoins = (LinearLayout)dialog.findViewById(R.id.buyCoins);
        buyCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(activity, BuyCoinsActivity.class);
                activity.startActivity(intent);
            }
        });
        dialog.show();
    }
}
