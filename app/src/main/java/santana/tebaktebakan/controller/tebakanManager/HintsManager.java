package santana.tebaktebakan.controller.tebakanManager;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        Log.i(TAG, "setHintFirstTime: "+result);

        hint.setText(result);
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


}
