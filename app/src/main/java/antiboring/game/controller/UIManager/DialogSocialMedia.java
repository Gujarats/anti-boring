package antiboring.game.controller.UIManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.ImageView;

import antiboring.game.R;
import antiboring.game.controller.socialMedia.FacebookManager;

/**
 * Created by Gujarat Santana on 30/12/15.
 */
public class DialogSocialMedia {
    public static DialogSocialMedia instance;
    private AppCompatDialog dialog;

    private DialogSocialMedia() {}

    public static DialogSocialMedia getInstance() {
        if (instance == null) instance = new DialogSocialMedia();
        return instance;
    }

    public void showShareDialogTebakGambar(final Context context,final Activity activity,final String imageUrl){
        dialog = new AppCompatDialog(activity);
        dialog.setContentView(R.layout.dialog_share_media);

        ImageView facebookShare = (ImageView)dialog.findViewById(R.id.facebook);
        facebookShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacebookManager.getInstance().loginFacebook(context,activity,imageUrl);

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }



}
