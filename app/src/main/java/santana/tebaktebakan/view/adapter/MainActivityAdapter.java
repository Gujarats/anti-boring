package santana.tebaktebakan.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.common.JsonConstantKey;
import santana.tebaktebakan.controller.SessionManager.SessionStars;
import santana.tebaktebakan.view.activity.StageActivity;

/**
 * Created by AdrianEkaFikri on 11/1/2015.
 */
public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    private static final String TAG = "main";
    private Context context;
    private Activity activity;
    private int layout;
    private SessionStars sessionStars;
    private String keyLevelJson="";

    public MainActivityAdapter(Context context, Activity activity, int layout) {
        this.context = context;
        this.activity = activity;
        this.layout = layout;
        this.sessionStars  = new SessionStars(context);
        keyLevelJson =sessionStars.getKeyLevelJson();
        Log.i(TAG, "MainActivityAdapter: "+keyLevelJson);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        initLevelNumber(position, holder.txtLevel);
        int level = position+1;
        if(!keyLevelJson.isEmpty()){
            try {
                JSONObject jsonObject = new JSONObject(keyLevelJson);
                int lengthUserLevelProgress = jsonObject.length();
                Log.i(TAG, "onBindViewHolder panjang KeyLevel: "+lengthUserLevelProgress);
                if(level <= lengthUserLevelProgress){

                    // user has passed the level
                    if(jsonObject.has(String.valueOf(level))){
                        JSONArray levelJson = jsonObject.getJSONArray(String.valueOf(level));
                        int starsAtLevel = levelJson.getJSONObject(0).getInt(JsonConstantKey.key_stars);

                        enableLevel(level, holder.layoutLevel);
                        setStarsAtLevel(holder.bintang1, holder.bintang2, starsAtLevel);
                    }else{
                    // the level doesn't exist on json
                        disableLevel(position,holder.layoutLevel);
                        setStarsAtLevel(holder.bintang1, holder.bintang2, 0);
                    }


                }else{
                    // the level that still locked
                    disableLevel(position,holder.layoutLevel);
                    setStarsAtLevel(holder.bintang1, holder.bintang2, 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{

            // this is level 1 and no stars starts very beginning
            if(level==1){
                enableLevel(level, holder.layoutLevel);
                setStarsAtLevel(holder.bintang1, holder.bintang2, 0);
            }else{
                // the level that still locked
                disableLevel(position,holder.layoutLevel);
                setStarsAtLevel(holder.bintang1, holder.bintang2, 0);
            }

        }

    }

    private void enableLevel(int levelPosition,LinearLayout layoutCompat){
        final int pos = levelPosition;
        layoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StageActivity.class);
                intent.putExtra(ApplicationConstants.level, pos);
                activity.startActivity(intent);
            }
        });
        layoutCompat.setBackgroundColor(Color.parseColor("#303F9F"));
    }

    private void disableLevel(int levelPosition,LinearLayout layoutCompat){
        layoutCompat.setBackgroundColor(Color.parseColor("#999999"));
        layoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initLevelNumber(int position, AppCompatTextView txtLevel){
        String lvl = String.valueOf(position+1);
        if (position<9){
            txtLevel.setText("0"+lvl);
        } else {
            txtLevel.setText(""+lvl);
        }
    }

    private void setStarsAtLevel(ImageView bintang1, ImageView bintang2, int starsComplete){
        if(starsComplete==1){
            // bintang 1
            bintang1.setVisibility(View.VISIBLE);
            bintang2.setVisibility(View.INVISIBLE);
        }else if(starsComplete==2){
            // bintang 2
            bintang1.setVisibility(View.VISIBLE);
            bintang2.setVisibility(View.VISIBLE);
        } else if(starsComplete==0){
            bintang1.setVisibility(View.INVISIBLE);
            bintang2.setVisibility(View.INVISIBLE);
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
