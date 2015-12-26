package santana.tebaktebakan.controller.tebakanManager;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import santana.tebaktebakan.controller.SessionManager.SessionStage;

/**
 * Created by Gujarat Santana on 26/12/15.
 */
public class StageManger {
    public static StageManger instance;
    private String tebakKata = "tebakKata";
    private String tebakGambar = "tebakGambar";


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
                    starStageTebakKata.setVisibility(View.INVISIBLE);
                }

                if(stage.has(tebakGambar)){
                    starStageTebakGambar.setVisibility(View.VISIBLE);
                }else{
                    starStageTebakGambar.setVisibility(View.INVISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            starStageTebakKata.setVisibility(View.INVISIBLE);
            starStageTebakGambar.setVisibility(View.INVISIBLE);
        }

    }
}
