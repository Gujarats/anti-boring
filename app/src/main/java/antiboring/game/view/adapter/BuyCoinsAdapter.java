package antiboring.game.view.adapter;

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

import antiboring.game.R;
import antiboring.game.controller.appBilling.AppBillingManager;
import antiboring.game.model.object.MBuyCoins;

/**
 * Created by Gujarat Santana on 01/01/16.
 */
public class BuyCoinsAdapter extends RecyclerView.Adapter<BuyCoinsAdapter.ViewHolder>{

    private int layout;
    private Activity activity;
    private List<MBuyCoins> buyCoinsObjects = new ArrayList<MBuyCoins>();
    
    public BuyCoinsAdapter(Activity activity){
        this.activity = activity;
        this.layout = R.layout.item_coins_buy;
        initSettingObjects();
    }

    private void initSettingObjects(){

        // first item
        MBuyCoins first = new MBuyCoins();
        first.setIcon(R.drawable.coin_700);
        first.setTitle("290 coins");
        first.setDescription("Hot Offer");
        first.setPrice("IDR 6000");
        buyCoinsObjects.add(first);

        MBuyCoins premiumUser = new MBuyCoins();
        premiumUser.setIcon(R.drawable.premium_pack);
        premiumUser.setTitle("Premium Pack");
        premiumUser.setDescription("Premium User Free Access Hint, no Ads + 400 coins");
        premiumUser.setPrice("IDR 24.000");
        buyCoinsObjects.add(premiumUser);

        MBuyCoins thirdCoins = new MBuyCoins();
        thirdCoins.setIcon(R.drawable.coin_700);
        thirdCoins.setTitle("700 coins");
        thirdCoins.setDescription("Regular");
        thirdCoins.setPrice("IDR 14.000");
        buyCoinsObjects.add(thirdCoins);

        MBuyCoins fourthCoins = new MBuyCoins();
        fourthCoins.setIcon(R.drawable.coin_2000);
        fourthCoins.setTitle("2000 coins");
        fourthCoins.setDescription("Double Regular");
        fourthCoins.setPrice("IDR 30.000");
        buyCoinsObjects.add(fourthCoins);

        MBuyCoins fifthCoins = new MBuyCoins();
        fifthCoins.setIcon(R.drawable.coin_4500);
        fifthCoins.setTitle("4500 coins");
        fifthCoins.setDescription("Awesome Pack");
        fifthCoins.setPrice("IDR 50.000");
        buyCoinsObjects.add(fifthCoins);

        MBuyCoins sixCoins = new MBuyCoins();
        sixCoins.setIcon(R.drawable.coin_20000);
        sixCoins.setTitle("20.000 coins");
        sixCoins.setDescription("Best Offer");
        sixCoins.setPrice("IDR 100.000");
        buyCoinsObjects.add(sixCoins);



        notifyDataSetChanged();
    }

    public void setPrice(String priceHotOffer,String pricePremium, String priceRegular,String priceDoubleReg, String priceAwesomePack, String priceBestOffer){
        if(buyCoinsObjects.size()>0){
            // set hotOffer
            buyCoinsObjects.get(0).setPrice(priceHotOffer);
            buyCoinsObjects.get(1).setPrice(pricePremium);
            buyCoinsObjects.get(2).setPrice(priceRegular);
            buyCoinsObjects.get(3).setPrice(priceDoubleReg);
            buyCoinsObjects.get(4).setPrice(priceAwesomePack);
            buyCoinsObjects.get(5).setPrice(priceBestOffer);
        }

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
        MBuyCoins buyCoinsObject = buyCoinsObjects.get(position);
        // set icon
        Picasso.with(activity).load(buyCoinsObject.getIcon()).into(holder.icon);
        //setTitle
        holder.title.setText(buyCoinsObject.getTitle());
        holder.price.setText(buyCoinsObject.getPrice());

        // set onClickListener
        switch (buyCoinsObject.getTitle()){
            case "290 coins":
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppBillingManager.getInstance().buyCoinsHotOffer(activity);
                    }
                });

                break;
            case "Premium Pack":
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppBillingManager.getInstance().buyPremium(activity);
                    }
                });

                break;
            case "700 coins":
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppBillingManager.getInstance().buyRegular(activity);
                    }
                });

                break;
            case "2000 coins":
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppBillingManager.getInstance().buyCoinsDoubleRegular(activity);
                    }
                });

                break;
            case "4500 coins":
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppBillingManager.getInstance().buyCoinsAwesomePack(activity);
                    }
                });

                break;
            case "20.000 coins":
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppBillingManager.getInstance().buyCoinsBestOffer(activity);
                    }
                });

                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return buyCoinsObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView icon;
        public AppCompatTextView title;
        public AppCompatTextView desc;
        public AppCompatTextView price;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            icon = (ImageView) view.findViewById(R.id.icon);
            title = (AppCompatTextView) view.findViewById(R.id.title);
            desc = (AppCompatTextView) view.findViewById(R.id.description);
            price = (AppCompatTextView) view.findViewById(R.id.price);

        }

    }
}
