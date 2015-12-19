package santana.tebaktebakan.controller.tebakanManager;

import android.support.v7.widget.AppCompatButton;
import android.view.View;

/**
 * Created by Gujarat Santana on 19/12/15.
 */
public class HintsManager {
    public static HintsManager instance;

    private HintsManager() {}

    public static HintsManager getInstance() {
        if (instance == null) instance = new HintsManager();
        return instance;
    }


    public void setOnClickEvent(final AppCompatButton appCompatButton, final onKeyboardListener listener){

        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyPress = appCompatButton.getText().toString();
                listener.onPressKey(keyPress);
            }
        });
    }

    public interface onKeyboardListener{
        void onPressKey(String key);
    }


}
