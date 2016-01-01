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
import antiboring.game.model.object.BuyCoinsObject;

/**
 * Created by Gujarat Santana on 01/01/16.
 */
public class BuyCoinsAdapter extends RecyclerView.Adapter<BuyCoinsAdapter.ViewHolder>{

    private int layout;
    private Activity activity;
    private List<BuyCoinsObject> buyCoinsObjects = new ArrayList<BuyCoinsObject>();
    
    public BuyCoinsAdapter(Activity activity){
        this.activity = activity;
        this.layout = R.layout.item_coins_buy;
        initSettingObjects();
    }

    private void initSettingObjects(){

        // first item
        BuyCoinsObject first = new BuyCoinsObject();
        first.setIcon(R.drawable.coins);
        first.setTitle("290 coins");
        first.setDescription("Hot Offer");
        first.setPrice("IDR 6000");
        buyCoinsObjects.add(first);

        BuyCoinsObject premiumUser = new BuyCoinsObject();
        premiumUser.setIcon(R.drawable.coins);
        premiumUser.setTitle("Premium Pack");
        premiumUser.setDescription("Premium User Free Access Hint, no Ads + 400 coins");
        premiumUser.setPrice("IDR 24.000");
        buyCoinsObjects.add(premiumUser);

        BuyCoinsObject thirdCoins = new BuyCoinsObject();
        thirdCoins.setIcon(R.drawable.coins);
        thirdCoins.setTitle("700 coins");
        thirdCoins.setDescription("Regular");
        thirdCoins.setPrice("IDR 14.000");
        buyCoinsObjects.add(thirdCoins);

        BuyCoinsObject fourthCoins = new BuyCoinsObject();
        fourthCoins.setIcon(R.drawable.coins);
        fourthCoins.setTitle("2000 coins");
        fourthCoins.setDescription("Double Regular");
        fourthCoins.setPrice("IDR 30.000");
        buyCoinsObjects.add(fourthCoins);

        BuyCoinsObject fifthCoins = new BuyCoinsObject();
        fifthCoins.setIcon(R.drawable.coins);
        fifthCoins.setTitle("4500 coins");
        fifthCoins.setDescription("Awesome Pack");
        fifthCoins.setPrice("IDR 50.000");
        buyCoinsObjects.add(fifthCoins);

        BuyCoinsObject sixCoins = new BuyCoinsObject();
        sixCoins.setIcon(R.drawable.coins);
        sixCoins.setTitle("20.000 coins");
        sixCoins.setDescription("Best Offer");
        sixCoins.setPrice("IDR 100.000");
        buyCoinsObjects.add(sixCoins);



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
        BuyCoinsObject buyCoinsObject = buyCoinsObjects.get(position);
        // set icon
        Picasso.with(activity).load(buyCoinsObject.getIcon()).into(holder.icon);
        //setTitle
        holder.title.setText(buyCoinsObject.getTitle());
        //setDesc and visibiltiy
        if(!buyCoinsObject.getDescription().isEmpty()){
            holder.desc.setText(buyCoinsObject.getDescription());
            holder.desc.setVisibility(View.VISIBLE);
        }else{
            holder.desc.setVisibility(View.GONE);
        }

        holder.price.setText(buyCoinsObject.getPrice());

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
