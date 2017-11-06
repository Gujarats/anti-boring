package antiboring.game.controller.tebakanManager;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import antiboring.game.R;
import antiboring.game.controller.Session.SessionHintDisplay;
import antiboring.game.controller.UIManager.DialogCorrectAnswer;
import antiboring.game.controller.UIManager.DialogDisplayCharManager;
import antiboring.game.controller.tebakanManager.hint.KeyboardProcessor;

/**
 * Created by Gujarat Santana on 19/12/15.
 */

public class HintsUiManager {
    private static final String TAG = "HintManager";
    public static HintsUiManager instance;
    private KeyboardProcessor keyboardProcessor;

    private HintsUiManager() {
        this.keyboardProcessor = new KeyboardProcessor();
    }

    public static HintsUiManager getInstance() {
        if (instance == null) instance = new HintsUiManager();
        return instance;
    }

    public void setOnDisplayDialogCharHintTebakGambar(final Activity activity, final LinearLayout linearLayout, final AppCompatTextView hint, final String jawabanTebakan,final int level, final String idGambar){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDisplayCharManager.getInstance().setDialogHint(activity, R.layout.dialog_display_char, new DialogDisplayCharManager.CallBackDialog() {
                    @Override
                    public void yes(AppCompatDialog dialog) {

                        if(CoinsManager.getInstance().isEnoughCoinDislplayChat(activity)){
                            dialog.dismiss();
                            CoinsManager.getInstance().setCoinsOnDisplayChar(activity);
                            displayHintTebakGambar(activity, hint, jawabanTebakan, level, idGambar);
                        }else {
                            CoinsManager.getInstance().showDialogZeroCoin(activity);
                            dialog.dismiss();
                        }

                    }
                });

            }
        });
    }

    public void setOnDisplayDialogCharHintTebakKata(final Activity activity, final LinearLayout linearLayout, final AppCompatTextView hint, final String jawabanTebakan,final int level){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDisplayCharManager.getInstance().setDialogHint(activity, R.layout.dialog_display_char, new DialogDisplayCharManager.CallBackDialog() {
                    @Override
                    public void yes(AppCompatDialog dialog) {
                        if (CoinsManager.getInstance().isEnoughCoinDislplayChat(activity)) {
                            dialog.dismiss();
                            CoinsManager.getInstance().setCoinsOnDisplayChar(activity);
                            displayHintTebakKata(activity, hint, jawabanTebakan, level);
                        } else {
                            CoinsManager.getInstance().showDialogZeroCoin(activity);
                            dialog.dismiss();
                        }


                    }
                });

            }
        });
    }

    public void displayHintTebakKata(final Activity activity, final AppCompatTextView hint, final String jawabanTebakan, int level){
        String hintDisplayText = hint.getText().toString();
        String result = this.keyboardProcessor.displayHintCharRandom(jawabanTebakan, hintDisplayText);
        checkAnswerHintTebakKata(activity, result, jawabanTebakan, level);
        hint.setText(result);
    }

    public void displayHintTebakGambar(final Activity activity, final AppCompatTextView hint, final String jawabanTebakan, int level, String idGambar){
        String hintDisplayText = hint.getText().toString();
        String result = this.keyboardProcessor.displayHintCharRandom(jawabanTebakan, hintDisplayText);
        checkAnswerHintTebakGambar(activity, result, jawabanTebakan, level, idGambar);
        hint.setText(result);
    }

    public void setDeleteAction(final AppCompatTextView hint, LinearLayout delelte){
        delelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hintSource = hint.getText().toString();
                String replaceHint = keyboardProcessor.deleteKeyboardNew(hintSource);
                hint.setText(replaceHint);
            }
        });
    }

    public void setHintFirstTime(AppCompatTextView hint,String source){
        String[] sourceArray = source.split("");
        String pattern = " _ ";
        //set the first letter
        String result = sourceArray[1];
        //begin loop with the second array
        for (int i = 2; i < sourceArray.length; i++) {
            boolean isWhitespace = sourceArray[i].matches("^\\s*$");
            if(!isWhitespace){
                result += pattern;
            }else{
                result += "  ";// add two white space
            }
        }

        hint.setText(result.toUpperCase());
    }

    private String deleteKeyboardValue(String source){
        String[] splitSource = source.split("");
        StringBuilder stringBuilder = new StringBuilder(source);
        char replaceChar = '_';

        int indexUnderScore = stringBuilder.indexOf("_")-3;

        int indexUnderscoreForArray = stringBuilder.indexOf("_")-2;

        if(indexUnderScore>0){
            try{
                Log.i(TAG, "index : "+indexUnderscoreForArray +" "+splitSource[indexUnderscoreForArray] + " then "+ indexUnderscoreForArray+" "+ splitSource[indexUnderscoreForArray-1]);
                if(splitSource[indexUnderscoreForArray].equalsIgnoreCase(" ")){

                    indexUnderScore = indexUnderScore-2;
                }

                Log.i(TAG, "deleteKeyboardValue: "+indexUnderScore);
                stringBuilder.setCharAt(indexUnderScore, replaceChar);
                return stringBuilder.toString();

            }catch (IndexOutOfBoundsException ex){
                return stringBuilder.toString();
            }

        }else{
            Log.i(TAG, "deleteKeyboardValue: -1");
            // got -1
            indexUnderScore = source.length()-2;
            stringBuilder.setCharAt(indexUnderScore, replaceChar);
            return stringBuilder.toString();
        }

    }

    private boolean isHintIsExist(List<Integer> indexDisplays,int indextHInt){
        boolean isExist = false;
        for (int i1 = 0; i1 < indexDisplays.size(); i1++) {
            if(indextHInt == indexDisplays.get(i1)){
                return true;
            }
        }

        return isExist;
    }

    private List<Integer> getIndexDisplayHint(String sourceHint,String[] arrayDisplayHint){
        List<Integer> indexDisplays = new ArrayList<>();

        for (int i = 0; i < arrayDisplayHint.length; i++) {
            int index  = sourceHint.indexOf(arrayDisplayHint[i]);
            indexDisplays.add(index);
        }

        return indexDisplays;
    }


    public String displayHintChar(Activity activity,String sourceAnswer, String hintDisplay){

        int indexHint = getIndexHint(activity);
        sourceAnswer = sourceAnswer.replaceAll("\\s+", ""); // replacing white space
        String[] splitSourceAnswer = sourceAnswer.split("");
        String hintAnswer = splitSourceAnswer[indexHint];//get the char/variable hint
        String result = this.keyboardProcessor.setKeyboardValue(hintAnswer, hintDisplay);//set it to hint for dusplay
        // set next indext hint for display next hint char
        setIndexHint(activity,indexHint+1);

        return result;
    }


    public void setIndexHint(Activity activity,int indexHint){
        SessionHintDisplay sessionHintDisplay =new SessionHintDisplay(activity);
        sessionHintDisplay.setKeyDisplayHint(indexHint);
    }

    public int getIndexHint(Activity activity){
        SessionHintDisplay sessionHintDisplay =new SessionHintDisplay(activity);
        return sessionHintDisplay.getsetKeyDisplayHint();
    }


    /**
     * insert values into keyboard button
     * @param keyboardButtons
     * @param sourceString
     */
    public void setKeyboard(List<AppCompatButton> keyboardButtons, String sourceString){
        Random rand = new Random();
        List<String> keyboardValues = this.keyboardProcessor.getKeyboardValues(sourceString);
        Log.i(TAG, "keyboardValues : " + keyboardValues.size());
        Log.i(TAG, "keyboardButtons : " + keyboardButtons.size());
        for(AppCompatButton keyboardButton : keyboardButtons){
            int randomNumber = rand.nextInt(keyboardValues.size());
//            if(keyboardKeyList.get(randomNumber).isEmpty()){
//                keyboardKeyList.remove(randomNumber);
//                keyboardKeys.get(i).setText(keyboardKeyList.get(randomNumber));
//            }

            keyboardButton.setText(keyboardValues.get(randomNumber));
            keyboardValues.remove(randomNumber);
        }
    }



    public void checkAnswerHintTebakGambar(final Activity activity, String userAnswer, String sourceAnswer, int level, String idGambar){

        if(!isUnderScoreExist(userAnswer)){
            String userAnswerConverted = getUserAnswerToSentence(userAnswer);
            String sourceAnswerConverted = getUserAnswerToSentence(sourceAnswer);

            if(isRightAnswer(userAnswerConverted,sourceAnswerConverted)){
                Toast.makeText(activity, "Benar Sekali", Toast.LENGTH_SHORT).show();

                //save progress gambar
                GambarCompleteManager.getInstance().setTebakGambarComplete(activity, level, idGambar);

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
                if(userAnswerConverted.length()==sourceAnswerConverted.length()){
                    YoYo.with(Techniques.Shake).playOn(activity.findViewById(R.id.hint));
                }
            }
        }

    }

    public void checkAnswerHintTebakKata (final Activity activity,String userAnswer,String sourceAnswer,int level){

        if(!isUnderScoreExist(userAnswer)){
            String userAnswerConverted = getUserAnswerToSentence(userAnswer);
            String sourceAnswerConverted = getUserAnswerToSentence(sourceAnswer);

            if(isRightAnswer(userAnswerConverted,sourceAnswerConverted)){
                Toast.makeText(activity, "Benar Sekali", Toast.LENGTH_SHORT).show();

                //save progress stars at level
                Tebakan.getInstance().saveProgressLevel(activity, level);

                //save progress stage TebakKata
                StageManger.getInstance().setTebakKataStageComplete(activity,level);

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
                if(userAnswerConverted.length()==sourceAnswerConverted.length()){
                    YoYo.with(Techniques.Shake).playOn(activity.findViewById(R.id.hint));
                }
            }
        }

    }

    public boolean isRightAnswer(String resultAnswer, String sourceAnswer){
        if(resultAnswer.equalsIgnoreCase(sourceAnswer)){
            return true;
        }else {
            return false;
        }
    }

    private String getUserAnswerToSentence(String userAnswer){
        String result;
        result = userAnswer.replaceAll("\\s+","");
        Log.i(TAG, "getUserAnswerToSentence: "+result);
        return result;
    }

    private boolean isUnderScoreExist(String userAnswer){
        StringBuilder stringBuilder = new StringBuilder(userAnswer);

        if(stringBuilder.indexOf("_")==-1){
            return false;
        }else{
            return true;
        }
    }

}
