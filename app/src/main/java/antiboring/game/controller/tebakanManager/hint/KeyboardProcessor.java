package antiboring.game.controller.tebakanManager.hint;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by gujarat on 04/11/17.
 */

public class KeyboardProcessor {

    public static final String Tag = "KeyboardProcessor";
    private List<Integer> indexHintDisplays;

    public KeyboardProcessor() {}

    public static KeyboardProcessor instance;
    public static KeyboardProcessor getInstance() {
        if (instance == null) instance = new KeyboardProcessor();
        return instance;
    }

    /**
     * get Unique Char from given source and insert into list for keyboard button
     * @param source
     * @return
     */
    public List<String> getKeyboardValues(String source){
        String UniqueChar = getUniqueChar(source);
        String alphabets = getNewChar(UniqueChar);
        String [] keyboardKeys= alphabets.split("");
        List<String> keyboardKeyList = new ArrayList<String>();
        for(String keyboardKey : keyboardKeys){
            keyboardKeyList.add(keyboardKey);
        }

        return keyboardKeyList;
    }

    /**
     * @param source
     * @return new keyboard values including the source
     */
    public String getNewChar(String source){
        int limitKeyboardKeys = 16;
        Random random = new Random();
        String result=source.toUpperCase();
        String [] sourceNewChar = new String []{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P",
                "Q","R","S","T","U","V","W","X","Y","Z"};
        for (int i=0;i<limitKeyboardKeys;i++) {
            // get a random value within the loop but never get the previous value
            int randomIndex = random.nextInt(limitKeyboardKeys + 1);
            String s = sourceNewChar[randomIndex];
            if(!isCharExistInString(s,result)){
                if(result.length()<limitKeyboardKeys){
                    result += s;
                }
            }else{
                i--;
            }
        }
        return result;
    }

    /**
     * Removing duplicate character in the String and return the unique one
     * @param sentence
     * @return
     */
    public String getUniqueChar(String sentence){
        String result="";
        String[] splitSentence= sentence.toLowerCase().split(" ");
        for(String word : splitSentence) {
            String[] chars = word.split("");
            for(String c : chars) {
                if(!result.isEmpty()){
                    if(!result.contains(c)){
                        result = result + c;
                    }
                }else{
                    result = c;
                }
            }
        }
        return result;
    }


    /**
     * Delete action for the displayed hint
     * @param sourceHint
     * @return
     */
    public String deleteKeyboardNew(String sourceHint){
        String [] arraySourceHint = sourceHint.split("");
        String replacer = "_";
        String result="";

        if(isAllowDelete(sourceHint)){
            for (int i = arraySourceHint.length-1; i >=0 ; i--) {
                if(!arraySourceHint[i].equalsIgnoreCase("_") && !arraySourceHint[i].equalsIgnoreCase(" ") && i!=0){
                    if(!isHintIsExist(i)){
                        arraySourceHint[i] = replacer;
                        break;
                    }
                }
            }

            for (String anArraySourceHint : arraySourceHint) {
                result += anArraySourceHint;
            }

            return result;
        }

        return sourceHint;
    }

    private String[] removeNullValues(String[] input){
        List<String> list = new ArrayList<String>();

        for(String s : input) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }

        input = list.toArray(new String[list.size()]);
        return input;
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

    public String displayHintCharRandom(String sourceAnswer, String hintDisplay){
        String[] splitSourceAnswer = sourceAnswer.split("");
        return getRandomHintAnswerCharFromSourceAnswer(splitSourceAnswer, hintDisplay);
    }

    /**
     * Check if the source Hint is allow to do action delete
     * if sourceHint = B _ _ _  then do not delete
     * @param sourceHint
     * @return
     */
    private boolean isAllowDelete(String sourceHint){

        sourceHint = sourceHint.replaceAll("\\s+","");
        String[] sourceHintSplit  = sourceHint.split("");
        sourceHintSplit = removeNullValues(sourceHintSplit);
        if (sourceHintSplit[1].equals("_")){
            return false;
        }
        return true;
    }

    /**
     * Check if the current index is the hint displayed by the user
     * @param indextHInt
     * @return
     */
    private boolean isHintIsExist(int indextHInt){
        try{
            for (int i1 = 0; i1 < this.indexHintDisplays.size(); i1++) {
                if(indextHInt == this.indexHintDisplays.get(i1)){
                    return true;
                }
            }
        }catch (NullPointerException e){
            // avoid null execption
        }
        return false;
    }

    private List<Integer> getIndexDisplayHint(String sourceHint,String[] arrayDisplayHint){
        List<Integer> indexDisplays = new ArrayList<>();

        for (int i = 0; i < arrayDisplayHint.length; i++) {
            int index  = sourceHint.indexOf(arrayDisplayHint[i]);
            indexDisplays.add(index);
        }

        return indexDisplays;
    }

    private List<Integer> getIndexUnderScore(String[] splitHintDisplay){
        // get all the index of _ in splitHintDisplay
        List<Integer> listIndexUnderscore = new ArrayList<>();
        for (int i = 0; i <splitHintDisplay.length ; i++) {
            if(splitHintDisplay[i].equals("_")){
                listIndexUnderscore.add(i);
            }
        }

        return listIndexUnderscore;
    }

    private String getRandomHintAnswerCharFromSourceAnswer(String[] splitSourceAnswer,String hintDisplay){
        String convertedHintDisplay = getHintDisplayToWord(hintDisplay);
        String[] splitHintDisplay = convertedHintDisplay.split("");
        List<Integer> listIndexUnderscore = getIndexUnderScore(splitHintDisplay);

        Random randomGenerator = new Random();
        if(listIndexUnderscore.size()>0 && listIndexUnderscore.size()-1>0){
            int indexUnderScore = randomGenerator.nextInt(listIndexUnderscore.size()-1);
            // add index hint display to global variable
            addIndexHint(indexUnderScore);
            String result = splitSourceAnswer[listIndexUnderscore.get(indexUnderScore)];
            splitHintDisplay[listIndexUnderscore.get(indexUnderScore)] = result.toUpperCase();
            result = TextUtils.join("  ",splitHintDisplay);
            return result;
        }else{
            try{
                String result = splitSourceAnswer[listIndexUnderscore.get(0)];
                splitHintDisplay[listIndexUnderscore.get(0)] = result;
                result = TextUtils.join("  ",splitHintDisplay);
                return result;
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            return TextUtils.join("  ",splitHintDisplay);
        }
    }

    private String getHintDisplayToWord(String hintDisplay){
        String [] splitHintDisplay = hintDisplay.split("    "); // split 4 white space

        //define List Word
        List<String> listWord = new ArrayList<>();
        for (String s : splitHintDisplay) {
            String word = s.replaceAll("\\s+","");
            word = word.replaceAll(" ","");
            listWord.add(word);
        }

        // convert the listword to sentence
        String result="";
        for (int i = 0; i < listWord.size(); i++) {

            if(i==listWord.size()-1){
                result+=listWord.get(i);
            }else{
                result+=listWord.get(i)+" ";
            }
        }
        return result;
    }

    private boolean isCharExistInString(String charsWord,String source){
        if(source.contains(charsWord)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * marked index hint so that when press delete the displayed hint character is not deleted
     */
    public void addIndexHint(int index){
        if (this.indexHintDisplays != null) {
            this.indexHintDisplays.add(index);
        }else{
            this.indexHintDisplays = new ArrayList<>();
            this.indexHintDisplays.add(index);
        }
    }

    public void removeHintIndex() {
        this.indexHintDisplays.clear();
    }
}
