package antiboring.game.controller.tebakanManager;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import antiboring.game.R;
import antiboring.game.controller.Session.SessionHintDisplay;
import antiboring.game.controller.UIManager.DialogCorrectAnswer;
import antiboring.game.controller.UIManager.DialogDisplayCharManager;

/**
 * Created by Gujarat Santana on 19/12/15.
 */

public class HintsManager {
    private static final String TAG = "HintManager";
    public static HintsManager instance;
    private List<Integer> indexHintDisplays = new ArrayList<>();

    private HintsManager() {}

    public static HintsManager getInstance() {
        if (instance == null) instance = new HintsManager();
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
//        String result = displayHintChar(activity, jawabanTebakan, hintDisplayText);
        String result = displayHintCharRandom(jawabanTebakan, hintDisplayText);
        checkAnswerHintTebakKata(activity, result, jawabanTebakan, level);
        hint.setText(result);
    }

    public void displayHintTebakGambar(final Activity activity, final AppCompatTextView hint, final String jawabanTebakan, int level, String idGambar){
        String hintDisplayText = hint.getText().toString();
//        String result = displayHintChar(activity, jawabanTebakan, hintDisplayText);
        String result = displayHintCharRandom(jawabanTebakan, hintDisplayText);
        checkAnswerHintTebakGambar(activity, result, jawabanTebakan, level, idGambar);
        hint.setText(result);
    }

    public void setDeleteAction(final AppCompatTextView hint, LinearLayout delelte){
        delelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hintSource = hint.getText().toString();
//                String replaceHint = deleteKeyboardValue(hintSource);
                String replaceHint = deleteKeyboardNew(hintSource, indexHintDisplays);
                Log.i(TAG, "onClick: delete " + replaceHint);
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

    private String deleteKeyboardNew(String sourceHint,List<Integer> indexDisplays){
        String [] arraySourceHint = sourceHint.split("");
        System.out.println(Arrays.toString(arraySourceHint));
        String replacer = "_";

        for (int i = arraySourceHint.length-1; i >=0 ; i--) {
            if(!arraySourceHint[i].equalsIgnoreCase("_") && !arraySourceHint[i].equalsIgnoreCase(" ") && i!=0){
                if(!isHintIsExist(indexDisplays,i)){
                    arraySourceHint[i] = replacer;
                    break;
                }

            }
        }

        String result="";
        for (int i = 0; i < arraySourceHint.length; i++) {
            result+=arraySourceHint[i];
        }

        System.out.println(Arrays.toString(arraySourceHint));
        System.out.println(result);
        return result;

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


    public  String setKeyboardValue(String keyboard,String source){
        StringBuilder stringBuilder = new StringBuilder(source);
        char charKeyboard = keyboard.charAt(0);
        if(stringBuilder.indexOf("_")>0){
            stringBuilder.setCharAt(stringBuilder.indexOf("_"), charKeyboard);
            return stringBuilder.toString();
        }else{
            // no underscore anymore
            return stringBuilder.toString();
        }

    }

    public String displayHintChar(Activity activity,String sourceAnswer, String hintDisplay){

        int indexHint = getIndexHint(activity);
        sourceAnswer = sourceAnswer.replaceAll("\\s+", ""); // replacing white space
        String[] splitSourceAnswer = sourceAnswer.split("");
        String hintAnswer = splitSourceAnswer[indexHint];//get the char/variable hint
        String result = setKeyboardValue(hintAnswer, hintDisplay);//set it to hint for dusplay
        // set next indext hint for display next hint char
        setIndexHint(activity,indexHint+1);

        return result;
    }

    public String displayHintCharRandom(String sourceAnswer, String hintDisplay){
        String[] splitSourceAnswer = sourceAnswer.split("");
        String hintAnswer =getRandomHintAnswerCharFromSourceAnswer(splitSourceAnswer, hintDisplay);
        return hintAnswer;
    }

    public String getRandomHintAnswerCharFromSourceAnswer(String[] splitSourceAnswer,String hintDisplay){
        Log.d(TAG, "getRandomHintAnswerChar() called with: " + "splitSourceAnswer = [" + Arrays.toString(splitSourceAnswer) + "], hintDisplay = [" + hintDisplay + "]");
        String convertedHintDisplay = getHintDisplayToWord(hintDisplay);
        String[] splitHintDisplay = convertedHintDisplay.split("");
        Log.i(TAG, "spliHintDisplay: "+ Arrays.toString(splitHintDisplay));
        // get all the index of _ in splitHintDisplay
        List<Integer> listIndexUnderscore = new ArrayList<>();
        for (int i = 0; i <splitHintDisplay.length ; i++) {
            if(splitHintDisplay[i].equals("_")){
                listIndexUnderscore.add(i);
            }
        }
        Log.i(TAG, "getRandomHintAnswerCharFromSourceAnswer underscore: "+splitHintDisplay.length +" source"+splitSourceAnswer.length);
        Log.i(TAG, "getRandomHintAnswerCharFromSourceAnswer underscore: "+Arrays.toString(splitHintDisplay) +" source "+Arrays.toString(splitSourceAnswer));
        Log.i(TAG, "getRandomHintAnswerCharFromSourceAnswer underscore: "+listIndexUnderscore.toString());

        Random randomGenerator = new Random();
        if(listIndexUnderscore.size()>0 && listIndexUnderscore.size()-1>0){
            int indexUnderScore = randomGenerator.nextInt(listIndexUnderscore.size()-1);
            // add index hint display to global variable
            indexHintDisplays.add(indexUnderScore);

            Log.i(TAG, "getRandomHintAnswerCharFromSourceAnswer: index "+indexUnderScore);
            String result = splitSourceAnswer[listIndexUnderscore.get(indexUnderScore)];

            splitHintDisplay[listIndexUnderscore.get(indexUnderScore)] = result.toUpperCase();

            Log.i(TAG, "getRandomHintAnswerCharFromSourceAnswer: result" +Arrays.toString(splitHintDisplay));

            result = TextUtils.join("  ",splitHintDisplay);
            return result;

        }else{
            try{
                String result = splitSourceAnswer[listIndexUnderscore.get(0)];

                splitHintDisplay[listIndexUnderscore.get(0)] = result;

                Log.i(TAG, "getRandomHintAnswerCharFromSourceAnswer: result" +Arrays.toString(splitHintDisplay));

                result = TextUtils.join("  ",splitHintDisplay);
                return result;
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            return TextUtils.join("  ",splitHintDisplay);

        }


    }

    public String getHintDisplayToWord(String hintDisplay){
        //split hint display
        String [] splitHintDisplay = hintDisplay.split("    "); // split 4 white space

        //define List Word
        List<String> listWord = new ArrayList<>();
        for (int i = 0; i < splitHintDisplay.length; i++) {
            Log.i(TAG, "getHintDisplayToWord: "+i+" "+splitHintDisplay[i]);
            String word = splitHintDisplay[i].replaceAll("\\s+","");
            word = word.replaceAll(" ","");
            listWord.add(word);
        }

        Log.i(TAG, "getHintDisplayToWord: listWord "+listWord.toString());


        // convert the listword to sentence
        String result="";
        for (int i = 0; i < listWord.size(); i++) {

            if(i==listWord.size()-1){
                result+=listWord.get(i);
            }else{
                result+=listWord.get(i)+" ";
            }
        }

        Log.i(TAG, "getHintDisplayToWord: result"+result);

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


    public void setKeyboard(List<AppCompatButton> keyboardKeys, String SourceString){
        /*1. get Unique Char from jawabanTebakan and insert into list*/
        String UniqueChar = HintsManager.getInstance().getUniqueChar(SourceString);
        String newChar = HintsManager.getInstance().getNewChar(UniqueChar);
        String keyboardKey = newChar;
        String [] array = keyboardKey.split("");
        List<String> keyboardKeyList = new ArrayList<String>();
        for(int o=0;o<array.length;o++){
            keyboardKeyList.add(array[o]);
        }

        /*2. insert list into keyboard button*/
        for(int i=0;i<keyboardKeys.size();i++){
            Random rand = new Random();
            int randomNumber = rand.nextInt(((keyboardKeyList.size() - 1)) + 1);
            if(keyboardKeyList.get(randomNumber).isEmpty()){
                keyboardKeyList.remove(randomNumber);
                randomNumber = rand.nextInt(((0+keyboardKeyList.size()-1)) + 1);
                keyboardKeys.get(i).setText(keyboardKeyList.get(randomNumber));
            }

            keyboardKeys.get(i).setText(keyboardKeyList.get(randomNumber));
            keyboardKeyList.remove(randomNumber);

        }
    }

    private String getUniqueChar(String str){
        String result="";
        String[] wordsSource = str.toLowerCase().split(" ");
        for(int i=0;i< wordsSource.length;i++){
            String[] charsWord = wordsSource[i].split("");
            for(int o=0;o<charsWord.length;o++){
                if(!result.isEmpty()){
                    if(!isCharExistInString(charsWord[o],result)){
                        result+=charsWord[o];
                    }
                }else{
                    result=charsWord[o];
                }
            }
        }
        return result;
    }

    private String getNewChar(String source){
        int limitKeyboardKeys = 16;
        String result=source.toUpperCase();
        String [] sourceNewChar = new String []{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P",
                "Q","R","S","T","U","V","W","X","Y","Z"};
        for(int o=0;o<sourceNewChar.length;o++){
            if(!isCharExistInString(sourceNewChar[o],result)){
                if(result.length()<limitKeyboardKeys){
                    result += sourceNewChar[o];

                }
            }
        }

        return result;
    }

    private boolean isCharExistInString(String charsWord,String source){
        if(source.indexOf(charsWord)>-1){
            return true;
        }else{
            return false;
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
