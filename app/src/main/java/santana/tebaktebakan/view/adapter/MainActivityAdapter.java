package santana.tebaktebakan.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;

import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.common.JsonConstantKey;
import santana.tebaktebakan.controller.SessionManager.SessionStars;
import santana.tebaktebakan.controller.tebakanManager.Tebakan;
import santana.tebaktebakan.model.object.LevelTebakanObject;
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
    private List<LevelTebakanObject> levelTebakanObjects = new ArrayList<LevelTebakanObject>();

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

        }else{
            disableLevel(holder.layoutLevel);
        }
        setStarsAtLevel(holder.bintang1,holder.bintang2,starsAtLevel);
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

    private void disableLevel(LinearLayout layoutCompat){
        layoutCompat.setBackgroundColor(Color.parseColor("#999999"));
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

    private void loadLevelGambarFromJson(){
        try {
            String jsonTebakGambar = Tebakan.getInstance().loadTebakGambarJSONFromAsset(activity);
            JSONObject jsonObject = new JSONObject(jsonTebakGambar);
            for (int i = 0; i <jsonObject.length() ; i++) {
                LevelTebakanObject levelTebakanObject = new LevelTebakanObject();
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
