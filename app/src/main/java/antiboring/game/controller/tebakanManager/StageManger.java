package antiboring.game.controller.tebakanManager;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import antiboring.game.controller.Session.SessionStage;

/**
 * Created by Gujarat Santana on 26/12/15.
 */
public class StageManger {
    public static final String tebakKata = "tebakKata";
    public static final String tebakGambar = "tebakGambar";
    public static StageManger instance;


    private StageManger() {}

    public static StageManger getInstance() {
        if (instance == null) instance = new StageManger();
        return instance;
    }

    public void setTebakKataStageComplete(Activity activity,int level){
        try {
            SessionStage sessionStage = new SessionStage(activity);
            String jsonStage = sessionStage.getKeyStageComplete(level);

            if(!jsonStage.isEmpty()){
                JSONObject stage  = new JSONObject(jsonStage);
                stage.put(tebakKata,true);

                sessionStage.setKeyStageComplete(level,stage.toString());
            }else{
                JSONObject stage  = new JSONObject();
                stage.put(tebakKata,true);

                sessionStage.setKeyStageComplete(level,stage.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public boolean isAllStageClear(Activity activity,int level){
        try {
            SessionStage sessionStage = new SessionStage(activity);
            String jsonStage = sessionStage.getKeyStageComplete(level);

            if(!jsonStage.isEmpty()){
                JSONObject stage  = new JSONObject(jsonStage);
                if(stage.has(tebakGambar) && stage.has(tebakKata)){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    

    public void setTebakGambarStageComplete(Activity activity,int level){
        try {
            SessionStage sessionStage = new SessionStage(activity);
            String jsonStage = sessionStage.getKeyStageComplete(level);

            if(!jsonStage.isEmpty()){
                JSONObject stage  = new JSONObject(jsonStage);
                stage.put(tebakGambar, true);

                sessionStage.setKeyStageComplete(level,stage.toString());
            }else{
                JSONObject stage  = new JSONObject();
                stage.put(tebakGambar,true);

                sessionStage.setKeyStageComplete(level,stage.toString());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setStarsStageVisibility(Activity activity,int level,ImageView starStageTebakKata,ImageView starStageTebakGambar){
        SessionStage sessionStage = new SessionStage(activity);
        String jsonStage = sessionStage.getKeyStageComplete(level);

        if(!jsonStage.isEmpty()){
            try {
                JSONObject stage = new JSONObject(jsonStage);
                if(stage.has(tebakKata)){
                    starStageTebakKata.setVisibility(View.VISIBLE);
                }else{
                    starStageTebakKata.setVisibility(View.GONE);
                }

                if(stage.has(tebakGambar)){
                    starStageTebakGambar.setVisibility(View.VISIBLE);
                    //save progress stars at level
                    Tebakan.getInstance().saveProgressLevel(activity, level);
                }else{
                    starStageTebakGambar.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            starStageTebakKata.setVisibility(View.GONE);
            starStageTebakGambar.setVisibility(View.GONE);
        }

    }

    public JSONObject getStageJson(Activity activity,int level){

        try {
            SessionStage sessionStage = new SessionStage(activity);
            String stageJsonString = sessionStage.getKeyStageComplete(level);
            JSONObject jsonStage = new JSONObject(stageJsonString);
            return jsonStage;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
