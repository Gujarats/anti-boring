package santana.tebaktebakan.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import santana.tebaktebakan.R;
import santana.tebaktebakan.controller.SessionManager.SessionStars;
import santana.tebaktebakan.view.activity.StageActivity;

/**
 * Created by AdrianEkaFikri on 11/1/2015.
 */
public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    private Context context;
    private Activity activity;
    private int layout;
    private SessionStars sessionStars;

    public MainActivityAdapter(Context context, Activity activity, int layout) {
        this.context = context;
        this.activity = activity;
        this.layout = layout;
        this.sessionStars  = new SessionStars(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        initPosition(position, holder.txtLevel);
        setLevel(position,holder.layoutLevel);
        setBintangLvl(position,holder.bintang1,holder.bintang2);


    }

    private void initPosition(int position, AppCompatTextView txtLevel){
        String lvl = String.valueOf(position+1);
        if (position<9){
            txtLevel.setText("0"+lvl);
        } else {
            txtLevel.setText(""+lvl);
        }
    }

    private void setBintangLvl(int position,ImageView bintang1,ImageView bintang2){
        int positionLvlUser = sessionStars.getKeyLevel();
        int starsComplete = sessionStars.getKeyStars();

        if(position == positionLvlUser-1){
            if(starsComplete==1){
                // bintang 1
                bintang1.setVisibility(View.VISIBLE);
                bintang2.setVisibility(View.INVISIBLE);
            }else if(starsComplete==2){
                // bintang 2
                bintang1.setVisibility(View.VISIBLE);
                bintang2.setVisibility(View.VISIBLE);
            }
        }else{
            bintang1.setVisibility(View.VISIBLE);
            bintang2.setVisibility(View.VISIBLE);
        }

    }

    private void setLevel(int position,LinearLayout layoutCompat){
        int positionLvlUser = sessionStars.getKeyLevel();
        if(position < positionLvlUser){
            final int pos = position+1;
            layoutCompat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StageActivity.class);
                    intent.putExtra("LEVEL_STAGE" , pos);
                    ActivityCompat.startActivity(activity, intent, Bundle.EMPTY);
                }
            });
            layoutCompat.setBackgroundColor(Color.parseColor("#303F9F"));
        }else{
            layoutCompat.setBackgroundColor(Color.parseColor("#999999"));
            layoutCompat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 40;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public AppCompatTextView txtLevel;
        public ImageView bintang1, bintang2;
        public LinearLayout layoutLevel;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            layoutLevel = (LinearLayout) view.findViewById(R.id.layoutLevel);
            txtLevel = (AppCompatTextView) view.findViewById(R.id.txtLvl);
            bintang1 = (ImageView) view.findViewById(R.id.bintang1);
            bintang2 = (ImageView) view.findViewById(R.id.bintang2);

        }

    }


}
