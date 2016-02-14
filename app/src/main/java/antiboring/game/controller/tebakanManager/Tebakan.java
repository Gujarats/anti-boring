package antiboring.game.controller.tebakanManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import antiboring.game.R;
import antiboring.game.common.ApplicationConstants;
import antiboring.game.common.JsonConstantKey;
import antiboring.game.controller.Session.SessionStars;
import antiboring.game.controller.UIManager.DialogCorrectAnswer;
import antiboring.game.controller.UIManager.UIAnimationManager;
import antiboring.game.model.object.MTebakanGambar;
import antiboring.game.model.object.MTebakanKata;
import antiboring.game.view.activity.AnswerTebakGambarActivity;
import antiboring.game.view.activity.HintsTebakKataActivity;

/**
 * Created by Gujarat Santana on 08/11/15.
 */
public class Tebakan {

    private static final String TAG
            = "Tebakan";
    public static Tebakan instance;

    private Tebakan() {}

    public static Tebakan getInstance() {
        if (instance == null) instance = new Tebakan();
        return instance;
    }

    /**
     * we put Json to save progress user in setKeyLevelJson
     * @param activity
     */

    public void saveProgressLevel(Activity activity,int currentLevel){
        try {
            SessionStars sessionStars = new SessionStars(activity);
            setStarsSession(activity);

            String KeyLevelJson = sessionStars.getKeyLevelJson();

            int currentStarsAtLevel = getStarsAtLevel(KeyLevelJson,currentLevel);
            Log.i(TAG, "saveProgressLevel: "+currentStarsAtLevel);
            currentStarsAtLevel = calculateStars(currentStarsAtLevel);
            Log.i(TAG, "saveProgressLevel after: "+currentStarsAtLevel);




            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JsonConstantKey.key_level,currentLevel);
            jsonObject.put(JsonConstantKey.key_stars,currentStarsAtLevel);

            if(!KeyLevelJson.isEmpty()){

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                JSONObject KeyLevelJsonObject = new JSONObject(KeyLevelJson);

                // set progress current level
                KeyLevelJsonObject.put(String.valueOf(currentLevel),jsonArray);

                // open/unlocked next level
                if(!KeyLevelJsonObject.has(String.valueOf(currentLevel+1))){
                    jsonObject = new JSONObject();
                    jsonObject.put(JsonConstantKey.key_level,currentLevel+1);
                    jsonObject.put(JsonConstantKey.key_stars,0);

                    jsonArray = new JSONArray();
                    jsonArray.put(jsonObject);
                    KeyLevelJsonObject.put(String.valueOf(currentLevel+1),jsonArray);
                }


                // saved it into SharedPreference
                sessionStars.setKeyLevelJson(KeyLevelJsonObject.toString());
            }else{

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                JSONObject KeyLevelJsonObject = new JSONObject();
                KeyLevelJsonObject.put(String.valueOf(currentLevel),jsonArray);

                // saved it into SharedPreference
                sessionStars.setKeyLevelJson(KeyLevelJsonObject.toString());


            }


            /*
            * this is KeyLevelJson looks like
            * {
            *   "1" : {[
            *       "key_level" : 1,
            *       "key_stars" : 2
            *   ]},
            *
            *   "2" : {[
            *       "key_level" : 2,
            *       "key_stars" : 1
            *   ]},
            *
            *   "3" : {[
            *       "key_level" : 3,
            *       "key_stars" : 0
            *   ]}
            *
            * }
            * */


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private int calculateStars(int currentStarsAtLevel){
        if(currentStarsAtLevel<2){
            currentStarsAtLevel = currentStarsAtLevel+1;
            Log.i(TAG, "calculateStars: "+currentStarsAtLevel);
            return currentStarsAtLevel;
        }else{
            return currentStarsAtLevel;
        }
    }

    private int getStarsAtLevel(String keyLevelJson,int currentLevel){
        Log.d(TAG, "getStarsAtLevel() called with: " + "keyLevelJson = [" + keyLevelJson + "], currentLevel = [" + currentLevel + "]");

        int currentStarsAtLevel=0;
        try {
            JSONObject jsonObject = new JSONObject(keyLevelJson);
            if(jsonObject.has(String.valueOf(currentLevel))){
                JSONArray jsonArray = jsonObject.getJSONArray(String.valueOf(currentLevel));
                currentStarsAtLevel = jsonArray.getJSONObject(0).getInt(JsonConstantKey.key_stars);
                return currentStarsAtLevel;
            }else
                return currentStarsAtLevel;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return currentStarsAtLevel;
    }


    public void setStarsSession(Activity activity){
        SessionStars sessionStars = new SessionStars(activity);
        int currentStars = sessionStars.getKeyStars();
        // set stars
        if(currentStars==0){
            currentStars = 1;
            sessionStars.setKeyStars(currentStars);
        }else if(currentStars==1){
            currentStars = 2;
            sessionStars.setKeyStars(currentStars);
        }else if(currentStars==2){
            currentStars = 0;
            sessionStars.setKeyStars(currentStars);
        }
    }

    public void setOnClickHintTebakKata(final Activity activity, final ImageView imageView,final MTebakanKata tebakanKataObject ){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, HintsTebakKataActivity.class);
                        intent.putExtra(ApplicationConstants.tebakanKata, tebakanKataObject.getTebakKata());
                        intent.putExtra(ApplicationConstants.jawabanTebakan, tebakanKataObject.getJawabanTebakKata());
                        intent.putExtra(ApplicationConstants.level, tebakanKataObject.getLevel());

                        activity.startActivity(intent);
                    }
                });
            }
        });
    }

    public List<MTebakanGambar> getImageLevel(int level, Activity activity){
        try {
            List<MTebakanGambar> tebakanGambarObjects = new ArrayList<MTebakanGambar>();
            String json = loadTebakGambarJSONFromAsset(activity);
            if(!json.isEmpty()){
                //get json from asset
                JSONObject obj = new JSONObject(json);

                //get specific level from json
                JSONArray m_jArry = obj.getJSONArray(String.valueOf(level));


                //get content from jsonArray to array list TebakanGambarObject
                for (int i = 0; i < m_jArry.length(); i++) {
                    JSONObject tebakanObjectLevel = m_jArry.getJSONObject(i);
                    MTebakanGambar tebakanGambarObject = new MTebakanGambar();
                    tebakanGambarObject.setGambarUrl(tebakanObjectLevel.getString("gambar_url"));
                    tebakanGambarObject.setJawaban(tebakanObjectLevel.getString("jawaban"));
                    tebakanGambarObject.setLevel(level);
                    tebakanGambarObjects.add(tebakanGambarObject);
                }

                return tebakanGambarObjects;
            }else{
                MTebakanGambar tebakanGambarObject = new MTebakanGambar();
                tebakanGambarObject.setGambarUrl("");
                tebakanGambarObject.setGambarUrl("");
                tebakanGambarObjects.add(tebakanGambarObject);
                return tebakanGambarObjects;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public MTebakanKata getTebakKata(int level,Activity activity){
        try {
            MTebakanKata tebakanKataObject = new MTebakanKata();
            String jsonTebakKata = loadJsonTebakKataFromAsset(activity);
            if(!jsonTebakKata.isEmpty()){
                JSONObject jsonObject = new JSONObject(jsonTebakKata);
                JSONArray m_jArry = jsonObject.getJSONArray(String.valueOf(level));

                JSONObject objectTebakKata= m_jArry.getJSONObject(0);
                tebakanKataObject.setJawabanTebakKata(objectTebakKata.getString("jawaban"));
                tebakanKataObject.setTebakKata(objectTebakKata.getString("tebak_kata"));
                return tebakanKataObject;

            }else{
                tebakanKataObject.setJawabanTebakKata("");
                tebakanKataObject.setTebakKata("");
                return tebakanKataObject;
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return null;

    }

    public String loadTebakGambarJSONFromAsset(Activity activity) {
        String json = "";
        try {
            InputStream is = activity.getAssets().open("gambar_database.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return json;
    }

    private String loadJsonTebakKataFromAsset(Activity activity){
        String json = "";
        try {
            InputStream is = activity.getAssets().open("tebak_kata_database.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return json;
    }


    public void setSizeImageView(Activity activity,ImageView imageView){
         /*get widht of the phone*/
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int WidthPhone = size.x;
            int HeightPhone = size.y;

        imageView.getLayoutParams().height = WidthPhone;
        imageView.getLayoutParams().width = WidthPhone;
        imageView.requestLayout();
    }

    public void setSizeLinearLayout(Activity activity,LinearLayout linearLayout){
         /*get width of the phone
         * and make square layout that depends on the width of the phone
         * */
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int WidthPhone = size.x;
        int HeightPhone = size.y;

        linearLayout.getLayoutParams().height = WidthPhone;
        linearLayout.getLayoutParams().width = WidthPhone;

        linearLayout.requestLayout();
    }

    public void loadImageToImageView(ImageView imageView,String res, Context context){
        int resource = getID(res,context);
        Picasso.with(context)
                .load(resource)
                .into(imageView);

    }

    public void loadImageToImageView2(ImageView imageView,String res, Context context){
        int resource = getID(res,context);
        Picasso.with(context)
                .load(resource)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    public void setOnClickTebakGambar(final Activity activity,ImageView imageView, final MTebakanGambar tebakanObject, final String idGambar){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AnswerTebakGambarActivity.class);
                intent.putExtra(ApplicationConstants.imageUrl, tebakanObject.getGambarUrl());
                intent.putExtra(ApplicationConstants.jawabanTebakan, tebakanObject.getJawaban());
                intent.putExtra(ApplicationConstants.level,tebakanObject.getLevel());
                intent.putExtra(ApplicationConstants.keyGambar,idGambar);

                activity.startActivity(intent);
            }
        });
    }


    public int getID(String resourceName,Context context){
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(resourceName, "drawable",
                context.getPackageName());
        return resourceId;
    }

    private boolean isEditTextEmpty(EditText editText){
        String word = editText.getText().toString();
        if(word.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public void checkAnswer(final Context context,final Activity activity, final EditText jawaban, final String kunciJawaban,final int level,final ImageView btnCek,final String idGambar){
        btnCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set User Played to true for showing ads
                UserPlayManager.getInstance().setUserPlayed(activity,true);


                if(!isEditTextEmpty(jawaban)){
                    String jawabanUser = jawaban.getText().toString();
                    if(isRightAnswer(jawabanUser,kunciJawaban)){
                        // jawaban benar sekali
                        Toast.makeText(context, "Benar Sekali", Toast.LENGTH_SHORT).show();

                        //set coin on right answer
                        CoinsManager.getInstance().setCoinOnRightAnswer(activity);
                        //save progress gambar
                        GambarCompleteManager.getInstance().setTebakGambarComplete(activity, level, idGambar);

                        //check progress and save it to StageManger giving stars
                        if(GambarCompleteManager.getInstance().isAllTebakGambarComplete(activity,level)){
                            StageManger.getInstance().setTebakGambarStageComplete(activity,level);
                        }

                        //check stage complete to get bonus coins
                        if(StageManger.getInstance().isAllStageClear(activity,level)){
                            CoinsManager.getInstance().setOnCompleteAllStars(activity);

                            //show dialog complate all stage
                            DialogCorrectAnswer.getInstance().setCompleteAllDialog(activity,R.layout.dialog_complete_all_stars);
                            SoundEffectManager.getInstance().playCorrectAnswer(activity);
                        }else{
                            //show dialog for correct answer
                            DialogCorrectAnswer.getInstance().setCorrectAnswerDialog(activity,R.layout.dialog_correct_answer);
                            SoundEffectManager.getInstance().playCorrectAnswer(activity);
                        }

                        // finish activity after 2 seconds
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.finish();
                                    }
                                }, 2000);
                            }
                        });

                    }else{
                        String [] kunciJawabanSplit = kunciJawaban.split(" ");
                        if(kunciJawabanSplit.length==3){
                            if(isOneWordMore(jawabanUser,kunciJawaban)){
                                // satu kata lagi
                                Toast.makeText(context, "Satu Kata Lagi", Toast.LENGTH_SHORT).show();
                            }else{
                                //salah total
                                YoYo.with(Techniques.Shake).playOn(activity.findViewById(R.id.layoutAnim));
                                UIAnimationManager.getInstance().setRotateAnimation(context, btnCek, 45);
                            }
                        }else{
                            if(isAlmostRight(jawabanUser,kunciJawaban)){
                                // jawaban hampir benar
                                Toast.makeText(context, "Sedikit Lagi", Toast.LENGTH_SHORT).show();

                            }else{
                                // jawaban salah total
                                jawaban.setText("");
                                YoYo.with(Techniques.Shake).playOn(activity.findViewById(R.id.layoutAnim));
                                UIAnimationManager.getInstance().setRotateAnimation(context, btnCek, 45);
                            }
                        }

                    }
                }else {
                    YoYo.with(Techniques.Shake).playOn(activity.findViewById(R.id.layoutAnim));
                    UIAnimationManager.getInstance().setRotateAnimation(context, btnCek, 45);
                }
            }
        });
    }

    public void checkAnswerTebakKata(final Context context,final Activity activity, final int level,final EditText jawaban, final String kunciJawaban,final int currentLevel,final ImageView btnCek){
        btnCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set User Played to true for showing ads
                UserPlayManager.getInstance().setUserPlayed(activity,true);


                if(!isEditTextEmpty(jawaban)){
                    String jawabanUser = jawaban.getText().toString();
                    if(isRightAnswer(jawabanUser,kunciJawaban)){
                        // jawaban benar sekali
                        Toast.makeText(context, "Benar Sekali", Toast.LENGTH_SHORT).show();

                        //set coin on right answer
                        CoinsManager.getInstance().setCoinOnRightAnswer(activity);

                        //save progress stars at level
                        saveProgressLevel(activity,currentLevel);

                        //save stage complete at tebakKata
                        StageManger.getInstance().setTebakKataStageComplete(activity, level);

                        //check stage complete to get bonus coins
                        if(StageManger.getInstance().isAllStageClear(activity,level)){
                            CoinsManager.getInstance().setOnCompleteAllStars(activity);

                            //show dialog complate all stage
                            DialogCorrectAnswer.getInstance().setCompleteAllDialog(activity,R.layout.dialog_complete_all_stars);
                            SoundEffectManager.getInstance().playCorrectAnswer(activity);
                        }else{
                            //show dialog for correct answer
                            DialogCorrectAnswer.getInstance().setCorrectAnswerDialog(activity,R.layout.dialog_correct_answer);
                            SoundEffectManager.getInstance().playCorrectAnswer(activity);
                        }

                        // finish activity after 2 seconds
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.finish();
                                    }
                                }, 2000);
                            }
                        });

                    }else{
                        String [] kunciJawabanSplit = kunciJawaban.split(" ");
                        if(kunciJawabanSplit.length==3){
                            if(isOneWordMore(jawabanUser,kunciJawaban)){
                                // satu kata lagi
                                Toast.makeText(context, "Satu Kata Lagi", Toast.LENGTH_SHORT).show();
                            }else{
                                //salah total
                                YoYo.with(Techniques.Shake).playOn(activity.findViewById(R.id.layoutAnim));
                                UIAnimationManager.getInstance().setRotateAnimation(context, btnCek, 45);
                            }
                        }else{
                            if(isAlmostRight(jawabanUser,kunciJawaban)){
                                // jawaban hampir benar
                                Toast.makeText(context, "Sedikit Lagi", Toast.LENGTH_SHORT).show();

                            }else{
                                // jawaban salah total
                                jawaban.setText("");
                                YoYo.with(Techniques.Shake).playOn(activity.findViewById(R.id.layoutAnim));
                                UIAnimationManager.getInstance().setRotateAnimation(context, btnCek, 45);
                            }
                        }

                    }
                }else{
                    YoYo.with(Techniques.Shake).playOn(activity.findViewById(R.id.layoutAnim));
                    UIAnimationManager.getInstance().setRotateAnimation(context, btnCek, 45);
                }

            }
        });
    }

    public boolean isRightAnswer(String jawaban,String kunciJawaban){
        if(jawaban.equalsIgnoreCase(kunciJawaban)){
            return true;
        }else{
            return false;
        }
    }

    public boolean isAlmostRight(String jawaban, String kunciJawaban){
        String [] jawabanSplit = jawaban.split(" ");
        String [] kunciJawabanSplit = kunciJawaban.split(" ");
        if(jawabanSplit[0].equalsIgnoreCase(kunciJawabanSplit[0])){
            return true;
        }else{
            return false;
        }
    }

    private boolean isOneWordMore(String jawaban, String kunciJawaban){
        String [] jawabanSplit = jawaban.split(" ");
        String [] kunciJawabanSplit = kunciJawaban.split(" ");
        if(jawabanSplit.length == 2 && kunciJawabanSplit.length==3){
            if(jawabanSplit[0].equalsIgnoreCase(kunciJawabanSplit[0]) && jawabanSplit[1].equalsIgnoreCase(kunciJawabanSplit[1])){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
}
