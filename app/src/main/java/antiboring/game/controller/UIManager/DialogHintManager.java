package antiboring.game.controller.UIManager;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import antiboring.game.R;
import antiboring.game.controller.appBilling.AppBillingManager;

/**
 * Created by Gujarat Santana on 25/12/15.
 */
public class DialogHintManager {
    public static DialogHintManager instance;
    private AppCompatDialog dialog;
    private CallBackDialog callBackDialog;

    private DialogHintManager() {}

    public static DialogHintManager getInstance() {
        if (instance == null) instance = new DialogHintManager();
        return instance;
    }

    public void setDialogHint(final Activity activity,int layout, final CallBackDialog callBackDialog){
        this.callBackDialog = callBackDialog;
        dialog = new AppCompatDialog(activity);
        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /**
         * init UI
         */
        ImageView yes = (ImageView) dialog.findViewById(R.id.yes);
        ImageView no = (ImageView) dialog.findViewById(R.id.no);
        LinearLayout premiumUser = (LinearLayout)dialog.findViewById(R.id.premiumUser);

        /**
         * init effect
         */
        LogicInterfaceManager.getInstance().setOnClickEffect(activity,yes);
        LogicInterfaceManager.getInstance().setOnClickEffect(activity,no);
        LogicInterfaceManager.getInstance().setOnClickEffect(activity,premiumUser);

        premiumUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppBillingManager.getInstance().buyPremium(activity);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // minus coins
                // go to hint activity
                callBackDialog.yes(dialog);

            }
        });

        dialog.show();
    }

    public interface CallBackDialog{
        void yes(AppCompatDialog dialod);
    }
}
