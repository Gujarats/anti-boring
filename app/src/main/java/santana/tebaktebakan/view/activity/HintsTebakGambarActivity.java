package santana.tebaktebakan.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;
import santana.tebaktebakan.controller.tebakanManager.Tebakan;

/**
 * Created by Gujarat Santana on 16/12/15.
 */
public class HintsTebakGambarActivity extends AppCompatActivity {

    static final ButterKnife.Setter<View, Boolean> ENABLED = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
        }
    };
    @Bind(R.id.layoutKeyBoard)
    LinearLayout layoutKeyBoard;
    @Bind(R.id.TebakGambar)
    ImageView TebakGambar;
    @Bind(R.id.hint)
    AppCompatTextView hint;
    /*
    * List keyboard button
    * */
    @Bind({ R.id.key1, R.id.key2, R.id.key3,R.id.key4,R.id.key5,R.id.key6,R.id.key7,R.id.key8,R.id.key9,
            R.id.key10,R.id.key11,R.id.key12,R.id.key13,R.id.key14,R.id.key15,R.id.key16})
    List<AppCompatButton> keyboardKeys;
    private String jawabanTebakan,imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hint_activity);

        initUi();
    }

    private void initUi(){
        ButterKnife.bind(this);
        ButterKnife.apply(keyboardKeys, ENABLED, false);

        Map<String,String> getDataIntent = LogicInterfaceManager.getInstance().getDataFromIntent(this);
        imageUrl = getDataIntent.get(ApplicationConstants.imageUrl);
        jawabanTebakan = getDataIntent.get(ApplicationConstants.jawabanTebakan);
        Tebakan.getInstance().loadImageToImageView2(TebakGambar, imageUrl, getApplicationContext());
        hint.setText(getUniqueChar(jawabanTebakan));

        String keyboardKey = getNewChar(getUniqueChar(jawabanTebakan));
        String [] array = keyboardKey.split("");
        List<String> keyboardKeyList = new ArrayList<String>();
        for(int o=0;o<array.length;o++){
            keyboardKeyList.add(array[o]);
        }



        for(int i=0;i<keyboardKeys.size();i++){
            Random rand = new Random();
            int randomNumber = rand.nextInt(((keyboardKeyList.size()-1)) + 1);
            if(keyboardKeyList.get(randomNumber).isEmpty()){
                keyboardKeyList.remove(randomNumber);
                randomNumber = rand.nextInt(((0+keyboardKeyList.size()-1)) + 1);
                keyboardKeys.get(i).setText(keyboardKeyList.get(randomNumber));
            }
            keyboardKeys.get(i).setText(keyboardKeyList.get(randomNumber));
            keyboardKeyList.remove(randomNumber);
        }





    }

    private void setLayoutKeyBoard(List<String> keyboardKeyList){
        for(int i=0;i<16;i++){
            Random rand = new Random();
            int randomNumber = rand.nextInt(((keyboardKeyList.size()-1)) + 1);
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
                if(result.length()<16){
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
