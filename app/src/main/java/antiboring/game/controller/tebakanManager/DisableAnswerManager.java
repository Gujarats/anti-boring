package antiboring.game.controller.tebakanManager;

import android.app.Activity;
import android.widget.ImageView;

import org.json.JSONObject;

/**
 * Created by Gujarat Santana on 27/12/15.
 */
public class DisableAnswerManager {
    private static final String TAG = "DisableAnswerManager";
    public static DisableAnswerManager instance;

    private DisableAnswerManager() {}

    public static DisableAnswerManager getInstance() {
        if (instance == null) instance = new DisableAnswerManager();
        return instance;
    }

    public void disableAnswerforTebakGambar(Activity activity, int level,ImageView btnCek,ImageView btnHelp,String keyGambar){
        try{

            if(GambarCompleteManager.getInstance().isKeyGambarComplete(activity, level, keyGambar)){
                btnCek.setOnClickListener(null);
                btnHelp.setOnClickListener(null);
            }
        }

        catch (NullPointerException e){

        }
    }

    public void disableAnswerforTebakKata(Activity activity, int level,ImageView btnCek,ImageView btnHelp){
        try{
            JSONObject stageJson = StageManger.getInstance().getStageJson(activity,level);
            if(stageJson.has(StageManger.tebakKata)){

                btnCek.setOnClickListener(null);
                btnHelp.setOnClickListener(null);

            }


        }


        catch (NullPointerException e){

        }
    }

}
