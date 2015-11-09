package santana.tebaktebakan.controller.UIManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.graphics.Palette;
import android.transition.Slide;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import santana.tebaktebakan.R;

/**
 * Created by Gujarat Santana on 09/11/15.
 */
public class UIAnimationManager {
    public static UIAnimationManager instance;

    private UIAnimationManager() {}

    public static UIAnimationManager getInstance() {
        if (instance == null) instance = new UIAnimationManager();
        return instance;
    }

    public void setAnimationHeader(final Context context,final Activity activity, final CollapsingToolbarLayout header, final ImageView headerIcon, int headerIconRes){
        initActivityTransitions(activity);
        header.setExpandedTitleColor(context.getResources().getColor(android.R.color.transparent));
        Picasso.with(activity).load(headerIconRes).into(headerIcon, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) headerIcon.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(context,activity,palette,header);
                    }
                });
            }

            @Override
            public void onError() {

            }
        });
    }

    private void applyPalette(Context context,Activity activity,Palette palette,CollapsingToolbarLayout collapsingToolbarLayout) {
        int primaryDark = context.getResources().getColor(R.color.primary);
        int primary = context.getResources().getColor(R.color.primary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        FragmentActivity fragmentActivity = (FragmentActivity) activity;
        fragmentActivity.supportStartPostponedEnterTransition();
    }

    private void initActivityTransitions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            activity.getWindow().setEnterTransition(transition);
            activity.getWindow().setReturnTransition(transition);
        }
    }
}
