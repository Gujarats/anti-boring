package antiboring.game.controller.UIManager;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;

/**
 * Created by Gujarat Santana on 27/12/15.
 */
public class DialogCorrectAnswer {
    public static DialogCorrectAnswer instance;
    private AppCompatDialog dialog;

    private DialogCorrectAnswer() {}

    public static DialogCorrectAnswer getInstance() {
        if (instance == null) instance = new DialogCorrectAnswer();
        return instance;
    }

    public void setCorrectAnswerDialog(Activity activity, int layout){
        dialog = new AppCompatDialog(activity);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void setCompleteAllDialog(Activity activity, int layout){
        dialog = new AppCompatDialog(activity);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
