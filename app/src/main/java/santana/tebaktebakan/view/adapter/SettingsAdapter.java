package santana.tebaktebakan.view.adapter;

import android.app.Activity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import santana.tebaktebakan.R;
import santana.tebaktebakan.model.object.SettingsObject;

/**
 * Created by Gujarat Santana on 26/12/15.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private int layout;
    private Activity activity;
    private List<SettingsObject> settingsObjects = new ArrayList<SettingsObject>();

    public SettingsAdapter (Activity activity){
        this.activity = activity;
        layout = R.layout.item_settings;
        initSettingObjects();

    }

    private void initSettingObjects(){


        // add login item
        SettingsObject login = new SettingsObject();
        login.setSourceImage(R.drawable.anti_boring);
        login.setTextSettings("Login");
        settingsObjects.add(login);

        //add buy coins item
        SettingsObject buyCoins = new SettingsObject();
        buyCoins.setSourceImage(R.drawable.anti_boring);
        buyCoins.setTextSettings("Buy Coins");
        settingsObjects.add(buyCoins);

        //add become premium
        SettingsObject premium = new SettingsObject();
        premium.setSourceImage(R.drawable.anti_boring);
        premium.setTextSettings("Become Premium");
        settingsObjects.add(premium);

        //add become premium
        SettingsObject rate = new SettingsObject();
        rate.setSourceImage(R.drawable.anti_boring);
        rate.setTextSettings("Rate");
        settingsObjects.add(rate);

        //add become premium
        SettingsObject share = new SettingsObject();
        share.setSourceImage(R.drawable.anti_boring);
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
        SettingsObject settingsObject = settingsObjects.get(position);
        // set icon
        Picasso.with(activity).load(settingsObject.getSourceImage()).into(holder.iconSetting);
        //set text
        holder.textStting.setText(settingsObject.getTextSettings());
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