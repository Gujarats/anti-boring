package antiboring.game.controller.tebakanManager;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import antiboring.game.controller.Session.SessionTebakGambar;

/**
 * Created by Gujarat Santana on 26/12/15.
 */
public class GambarCompleteManager {
    public static final String keyGambar1= "gambar1";
    public static final String keyGambar2= "gambar2";
    public static final String keyGambar3= "gambar3";
    private static final String TAG = "GambarCompleteManager";
    public static GambarCompleteManager instance;


    private GambarCompleteManager() {}

    public static GambarCompleteManager getInstance() {
        if (instance == null) instance = new GambarCompleteManager();
        return instance;
    }

    public void setTebakGambarComplete(Activity activity,int level,String idImage){
        Log.d(TAG, "setTebakGambarComplete() called with: " + "activity = [" + activity + "], level = [" + level + "], idImage = [" + idImage + "]");
        try {
            SessionTebakGambar sessionStage = new SessionTebakGambar(activity);
            String jsonStageGambar = sessionStage.getGambarComplete(level);

            if(!jsonStageGambar.isEmpty()){
                JSONObject stageGambar  = new JSONObject(jsonStageGambar);
                stageGambar.put(idImage, true);

                sessionStage.setGambarComplete(level, stageGambar.toString());
            }else{
                JSONObject stage  = new JSONObject();
                stage.put(idImage,true);

                sessionStage.setGambarComplete(level,stage.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setGambarAnswerVisibility(Activity activity,int level,RelativeLayout answerGambar1, RelativeLayout answerGambar2,RelativeLayout answerGambar3){
        SessionTebakGambar sessionStage = new SessionTebakGambar(activity);
        String gambarStageComplete = sessionStage.getGambarComplete(level);

        if(!gambarStageComplete.isEmpty()){
            Log.i(TAG, "setGambarAnswerVisibility: "+gambarStageComplete);
            try {
                JSONObject stage = new JSONObject(gambarStageComplete);

                //set answer 1
                if(stage.has(keyGambar1)){
                    answerGambar1.setVisibility(View.VISIBLE);
                }else{
                    answerGambar1.setVisibility(View.INVISIBLE);
                }

                //set answer 2
                if(stage.has(keyGambar2)){
                    answerGambar2.setVisibility(View.VISIBLE);
                }else{
                    answerGambar2.setVisibility(View.INVISIBLE);
                }

                //set answer 3
                if(stage.has(keyGambar3)){
                    answerGambar3.setVisibility(View.VISIBLE);
                }else{
                    answerGambar3.setVisibility(View.INVISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            answerGambar1.setVisibility(View.INVISIBLE);
            answerGambar2.setVisibility(View.INVISIBLE);
            answerGambar3.setVisibility(View.INVISIBLE);
        }

    }

    public boolean isAllTebakGambarComplete(Activity activity,int level){
        SessionTebakGambar sessionStage = new SessionTebakGambar(activity);
        String gambarStageComplete = sessionStage.getGambarComplete(level);
        try {
            JSONObject stageGTebakGambar = new JSONObject(gambarStageComplete);
            if(stageGTebakGambar.has(keyGambar1) && stageGTebakGambar.has(keyGambar2) &&stageGTebakGambar.has(keyGambar3)){
                return true;
            }else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isKeyGambarComplete(Activity activity, int level, String keyGambar){
        SessionTebakGambar sessionStage = new SessionTebakGambar(activity);
        String gambarStageComplete = sessionStage.getGambarComplete(level);
        try {
            JSONObject stageGTebakGambar = new JSONObject(gambarStageComplete);
            if(stageGTebakGambar.has(keyGambar)){
                return true;
            }else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }
}
