package antiboring.game.controller.UIManager;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatTextView;

import antiboring.game.R;
import antiboring.game.controller.tebakanManager.CoinsManager;

/**
 * Created by Gujarat Santana on 11/01/16.
 */
public class DialogSuccesBuyCoins {
    public static DialogSuccesBuyCoins instance;
    private AppCompatDialog dialog;

    private DialogSuccesBuyCoins() {}

    public static DialogSuccesBuyCoins getInstance() {
        if (instance == null) instance = new DialogSuccesBuyCoins();
        return instance;
    }

    public void showHotOfferCoins(Activity activity){
        dialog = new AppCompatDialog(activity);
        dialog.setContentView(R.layout.dialog_correct_answer);
        AppCompatTextView coin_desc = (AppCompatTextView)dialog.findViewById(R.id.coin_desc);
        coin_desc.setText("Yeayy "+ CoinsManager.hotOffer+" coins");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showPremium(Activity activity){
        dialog = new AppCompatDialog(activity);
        dialog.setContentView(R.layout.dialog_correct_answer);
        AppCompatTextView coin_desc = (AppCompatTextView)dialog.findViewById(R.id.coin_desc);
        coin_desc.setText("Yeayy You're Premium User "+ CoinsManager.premium+" coins");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showRegular(Activity activity){
        dialog = new AppCompatDialog(activity);
        dialog.setContentView(R.layout.dialog_correct_answer);
        AppCompatTextView coin_desc = (AppCompatTextView)dialog.findViewById(R.id.coin_desc);
        coin_desc.setText("Yeayy "+ CoinsManager.regular+" coins");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showDoubleRegular(Activity activity){
        dialog = new AppCompatDialog(activity);
        dialog.setContentView(R.layout.dialog_correct_answer);
        AppCompatTextView coin_desc = (AppCompatTextView)dialog.findViewById(R.id.coin_desc);
        coin_desc.setText("Yeayy You're Premium User "+ CoinsManager.doubleRegular+" coins");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showAwesomePack(Activity activity){
        dialog = new AppCompatDialog(activity);
        dialog.setContentView(R.layout.dialog_correct_answer);
        AppCompatTextView coin_desc = (AppCompatTextView)dialog.findViewById(R.id.coin_desc);
        coin_desc.setText("Yeayy "+ CoinsManager.awesomePack+" coins");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showBestOffer(Activity activity){
        dialog = new AppCompatDialog(activity);
        dialog.setContentView(R.layout.dialog_correct_answer);
        AppCompatTextView coin_desc = (AppCompatTextView)dialog.findViewById(R.id.coin_desc);
        coin_desc.setText("Yeayy "+ CoinsManager.bestOffer+" coins");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
