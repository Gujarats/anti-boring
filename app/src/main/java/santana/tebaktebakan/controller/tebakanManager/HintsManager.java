package santana.tebaktebakan.controller.tebakanManager;

import android.app.Activity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import santana.tebaktebakan.R;

/**
 * Created by Gujarat Santana on 19/12/15.
 */

public class HintsManager {
    private static final String TAG = "HintManager ";
    public static HintsManager instance;

    private HintsManager() {}

    public static HintsManager getInstance() {
        if (instance == null) instance = new HintsManager();
        return instance;
    }


    public void setDeleteAction(final AppCompatTextView hint, AppCompatButton delelte){
        delelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hintSource = hint.getText().toString();
                String replaceHint = deleteKeyboardValue(hintSource);
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
            }catch (IndexOutOfBoundsException ex){

            }

            Log.i(TAG, "deleteKeyboardValue: "+indexUnderScore);
            stringBuilder.setCharAt(indexUnderScore, replaceChar);
            return stringBuilder.toString();
        }else{
            // got -1
            indexUnderScore = source.length()-2;
            stringBuilder.setCharAt(indexUnderScore, replaceChar);
            return stringBuilder.toString();
        }

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

    public String getUniqueChar(String str){
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

    public String getNewChar(String source){
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


    public void checkAnswer (Activity activity,String userAnswer,String sourceAnswer){

        if(!isUnderScoreExist(userAnswer)){
            String userAnswerConverted = getUserAnswerToSentence(userAnswer);
            String sourceAnswerConverted = getUserAnswerToSentence(sourceAnswer);

            if(isRightAnswer(userAnswerConverted,sourceAnswerConverted)){
                activity.finish();
                Toast.makeText(activity, "Benar Sekali", Toast.LENGTH_SHORT).show();
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
