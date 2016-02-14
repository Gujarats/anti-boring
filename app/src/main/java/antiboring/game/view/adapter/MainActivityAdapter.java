package antiboring.game.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import antiboring.game.R;
import antiboring.game.common.ApplicationConstants;
import antiboring.game.common.JsonConstantKey;
import antiboring.game.controller.Session.SessionStars;
import antiboring.game.controller.tebakanManager.Tebakan;
import antiboring.game.model.object.MLevelTebakan;
import antiboring.game.view.activity.StageActivity;

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
    private List<MLevelTebakan> levelTebakanObjects = new ArrayList<MLevelTebakan>();

    public MainActivityAdapter(Context context, Activity activity, int layout) {
        this.context = context;
        this.activity = activity;
        this.layout = layout;
        this.sessionStars  = new SessionStars(context);
        keyLevelJson =sessionStars.getKeyLevelJson();
        Log.i(TAG, "MainActivityAdapter: "+keyLevelJson);
        loadLevelGambarFromJson();
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
        int starsAtLevel = levelTebakanObjects.get(position).getStars();
        if(!levelTebakanObjects.get(position).isLocked()){
            enableLevel(level, holder.layoutLevel);
            setStarsAtLevelEnable(holder.txtLevel, holder.bintang1, holder.bintang2, holder.locked, starsAtLevel);
        }else{
            disableLevel(holder.layoutLevel);
            setStarsAtLevelDisable(holder.txtLevel, holder.bintang1, holder.bintang2, holder.locked);
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
        layoutCompat.setBackgroundResource(R.drawable.level_active_selector);
    }

    private void disableLevel(LinearLayout layoutCompat){
//        layoutCompat.setBackgroundColor(Color.parseColor("#999999"));
        layoutCompat.setBackgroundResource(R.drawable.level_inactive);
        layoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initLevelNumber(int position, AppCompatTextView txtLevel){
        String lvl = String.valueOf(position + 1);
        if (position<9){
            txtLevel.setText("0"+lvl);
        } else {
            txtLevel.setText("" + lvl);
        }
    }

    public void setStarsAtLevelDisable(AppCompatTextView level, ImageView bintang1, ImageView bintang2, ImageView locked){
        bintang1.setVisibility(View.GONE);
        bintang2.setVisibility(View.GONE);
        level.setVisibility(View.GONE);
        locked.setVisibility(View.VISIBLE);

    }

    private void setStarsAtLevelEnable(AppCompatTextView level,ImageView bintang1, ImageView bintang2, ImageView locked,int starsComplete){
        level.setVisibility(View.VISIBLE);
        locked.setVisibility(View.GONE);
        if(starsComplete==1){
            // bintang 1
            Picasso.with(activity).load(R.drawable.finish_stars).into(bintang1);
            Picasso.with(activity).load(R.drawable.unfinish_stars).into(bintang2);
            bintang1.setVisibility(View.VISIBLE);
            bintang2.setVisibility(View.VISIBLE);
        }else if(starsComplete==2){
            // bintang 2
            Picasso.with(activity).load(R.drawable.finish_stars).into(bintang1);
            Picasso.with(activity).load(R.drawable.finish_stars).into(bintang2);
            bintang1.setVisibility(View.VISIBLE);
            bintang2.setVisibility(View.VISIBLE);
        } else if(starsComplete==0){
            Picasso.with(activity).load(R.drawable.unfinish_stars).into(bintang1);
            Picasso.with(activity).load(R.drawable.unfinish_stars).into(bintang2);
            bintang1.setVisibility(View.VISIBLE);
            bintang2.setVisibility(View.VISIBLE);
        }
    }

    private void loadLevelGambarFromJson(){
        try {
            String jsonTebakGambar = Tebakan.getInstance().loadTebakGambarJSONFromAsset(activity);
            JSONObject jsonObject = new JSONObject(jsonTebakGambar);
            for (int i = 0; i <jsonObject.length() ; i++) {
                MLevelTebakan levelTebakanObject = new MLevelTebakan();
                levelTebakanObject.setLevel(i+1);
                levelTebakanObjects.add(levelTebakanObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateLevelJson(){
        keyLevelJson = sessionStars.getKeyLevelJson();
        for (int i = 0; i < levelTebakanObjects.size(); i++) {
            int level = i+1;
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

                            levelTebakanObjects.get(i).setStars(starsAtLevel);
                            levelTebakanObjects.get(i).setIsLocked(false);
                            levelTebakanObjects.get(i).setLevel(level);

                        }else{
                            // the level doesn't exist on json
                            levelTebakanObjects.get(i).setStars(0);
                            levelTebakanObjects.get(i).setIsLocked(true);
                            levelTebakanObjects.get(i).setLevel(level);

                        }


                    }else{
                        // the level that still locked
                        levelTebakanObjects.get(i).setStars(0);
                        levelTebakanObjects.get(i).setIsLocked(true);
                        levelTebakanObjects.get(i).setLevel(level);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

                // this is level 1 and no stars starts very beginning
                if(i==0){
                    levelTebakanObjects.get(0).setStars(0);
                    levelTebakanObjects.get(0).setIsLocked(false);
                    levelTebakanObjects.get(0).setLevel(1);
                }else{
                    // the level that still locked
                    levelTebakanObjects.get(i).setStars(0);
                    levelTebakanObjects.get(i).setIsLocked(true);
                    levelTebakanObjects.get(i).setLevel(level);
                }

            }
        }
        notifyDataSetChanged();
    }

    public void loadProgressUser(){

    }

    @Override
    public int getItemCount() {
        return levelTebakanObjects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public AppCompatTextView txtLevel;
        public ImageView bintang1, bintang2,locked;
        public LinearLayout layoutLevel;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            layoutLevel = (LinearLayout) view.findViewById(R.id.layoutLevel);
            txtLevel = (AppCompatTextView) view.findViewById(R.id.txtLvl);
            bintang1 = (ImageView) view.findViewById(R.id.bintang1);
            bintang2 = (ImageView) view.findViewById(R.id.bintang2);
            locked = (ImageView) view.findViewById(R.id.locked);

        }

    }


}
