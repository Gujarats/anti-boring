package santana.tebaktebakan.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Map;

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

    @Bind(R.id.layoutKeyBoard)
    LinearLayout layoutKeyBoard;
    @Bind(R.id.TebakGambar)
    ImageView TebakGambar;
    @Bind(R.id.hint)
    AppCompatTextView hint;

    private String jawabanTebakan,imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hint_activity);

        initUi();
    }

    private void initUi(){
        ButterKnife.bind(this);
        setLayoutKeyBoard();

        Map<String,String> getDataIntent = LogicInterfaceManager.getInstance().getDataFromIntent(this);
        imageUrl = getDataIntent.get(ApplicationConstants.imageUrl);
        jawabanTebakan = getDataIntent.get(ApplicationConstants.jawabanTebakan);
        Tebakan.getInstance().loadImageToImageView2(TebakGambar, imageUrl, getApplicationContext());
        hint.setText(getUniqueChar(jawabanTebakan));

    }

    private void setLayoutKeyBoard(){
        int width =  (int) getResources().getDimension(R.dimen.widthKeyboard);
        int height = (int) getResources().getDimension(R.dimen.heightKeyboard);
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(height,height);
        linearLayout.setLayoutParams(layoutParams);

        AppCompatButton button  = new AppCompatButton(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);

        button.setLayoutParams(params);
//        button.setId(i);
        linearLayout.addView(button);
        ArrayList<View> list =new ArrayList<View>();
        list.add(linearLayout);
        layoutKeyBoard.addChildrenForAccessibility(list);

//        for(int i=0;i<16;i++){
//            for(int o=0;o<2;o++){
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
//
//                button.setLayoutParams(params);
//                button.setId(i);
//                linearLayout.addView(button);
//            }
//            if(layoutKeyBoard.getParent()!=null)
//                ((ViewGroup)layoutKeyBoard.getParent()).removeView(layoutKeyBoard);
//            layoutKeyBoard.addView(button);
//        }
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

    private boolean isCharExistInString(String charsWord,String source){
        if(source.indexOf(charsWord)>-1){
            return true;
        }else{
            return false;
        }
    }

}
