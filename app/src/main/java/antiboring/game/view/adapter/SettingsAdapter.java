package antiboring.game.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import antiboring.game.R;
import antiboring.game.controller.UIManager.LogicInterfaceManager;
import antiboring.game.controller.appBilling.AppBillingManager;
import antiboring.game.controller.socialMedia.FacebookManager;
import antiboring.game.model.object.MSettings;
import antiboring.game.view.activity.BuyCoinsActivity;

/**
 * Created by Gujarat Santana on 26/12/15.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private int layout;
    private Activity activity;
    private Context context;
    private List<MSettings> settingsObjects = new ArrayList<MSettings>();
    private FacebookManager facebookManager;

    public SettingsAdapter (Context context,Activity activity,FacebookManager facebookManager){
        this.activity = activity;
        this.context = context;
        this.facebookManager = facebookManager;
        layout = R.layout.item_settings;
        initSettingObjects();
    }

    private void initSettingObjects(){


        // add login item
        MSettings login = new MSettings();
        login.setSourceImage(R.drawable.login);
        login.setTextSettings("Login");
        settingsObjects.add(login);

        //add buy coins item
        MSettings buyCoins = new MSettings();
        buyCoins.setSourceImage(R.drawable.buy);
        buyCoins.setTextSettings("Buy Coins");
        settingsObjects.add(buyCoins);

        //add become premium
        MSettings premium = new MSettings();
        premium.setSourceImage(R.drawable.premium);
        premium.setTextSettings("Become Premium");
        settingsObjects.add(premium);

        //add become premium
        MSettings rate = new MSettings();
        rate.setSourceImage(R.drawable.rate_us);
        rate.setTextSettings("Rate");
        settingsObjects.add(rate);

        //add become premium
        MSettings share = new MSettings();
        share.setSourceImage(R.drawable.share_setting);
        share.setTextSettings("Share");
        settingsObjects.add(share);

        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MSettings settingsObject = settingsObjects.get(position);
        // set icon
        Picasso.with(activity).load(settingsObject.getSourceImage()).into(holder.iconSetting);
        //set text
        holder.textStting.setText(settingsObject.getTextSettings());
        LogicInterfaceManager.getInstance().setOnClickEffect(activity,holder.mView);
        initAction(holder.mView, settingsObject.getTextSettings());
    }

    private void initAction(View view,String title){
        switch (title){
            case "Rate":
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });

                break;
            case "Buy Coins":
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, BuyCoinsActivity.class);
                        activity.startActivity(intent);
                    }
                });

                break;
            case "Become Premium":
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppBillingManager.getInstance().buyPremium(activity);
                    }
                });

                break;

            case "Share":
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        facebookManager.loginFacebookShareAntiBoring(context, activity);
                    }
                });

                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return settingsObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView iconSetting;
        public AppCompatTextView textStting;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            iconSetting = (ImageView) view.findViewById(R.id.iconSetting);
            textStting = (AppCompatTextView) view.findViewById(R.id.textSetting);

        }

    }
}
