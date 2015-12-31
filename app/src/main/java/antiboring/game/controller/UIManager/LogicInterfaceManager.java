package antiboring.game.controller.UIManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.HashMap;
import java.util.Map;

import antiboring.game.R;
import antiboring.game.common.ApplicationConstants;
import antiboring.game.controller.tebakanManager.CoinsManager;
import antiboring.game.view.activity.HintsTebakGambarActivity;
import antiboring.game.view.activity.HintsTebakKataActivity;

/**
 * Created by Gujarat Santana on 09/11/15.
 */
public class LogicInterfaceManager {

    public static LogicInterfaceManager instance;

    private LogicInterfaceManager() {}

    public static LogicInterfaceManager getInstance() {
        if (instance == null) instance = new LogicInterfaceManager();
        return instance;
    }

    public int getLevel(Activity activity) {
        Intent intent = activity.getIntent();
        int lvl;
        try {
            if (intent.getExtras() != null) {
                lvl = intent.getExtras().getInt(ApplicationConstants.level);

                return lvl;
            }else{
                return 0;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setTextViewLevel(int lvl, AppCompatTextView txtLevel){
        if (lvl < 10) {
            txtLevel.setText("0" + lvl);
        } else {
            txtLevel.setText("" + lvl);
        }
    }

    public void setAnimtaionEffectonHint(final ImageView btnHelp){


        UiUpdater mUIUpdater = new UiUpdater(new Runnable() {
            @Override
            public void run() {
                // do stuff ...
                YoYo.with(Techniques.Swing).playOn(btnHelp);
            }
        },5000);

// Start updates
        mUIUpdater.startUpdates();

    }

    public void setOnClickEffect(final Activity activity, View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        LinearLayout view = (LinearLayout) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.setBackgroundResource(R.drawable.square_shape);
                        GradientDrawable drawable = (GradientDrawable) v.getBackground();
                        drawable.setColor(Color.parseColor("#ccccff"));
//                        view.setBackgroundColor(Color.parseColor("#ccccff"));
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                    }
                    case MotionEvent.ACTION_CANCEL: {
//                        ImageView view = (ImageView) v;
                        LinearLayout view = (LinearLayout) v;
                        //clear the overlay
                        view.setBackgroundColor(0);
//                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    public void setOnClickEffect(final Activity activity, LinearLayout imageView) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        LinearLayout view = (LinearLayout) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.setBackgroundResource(R.drawable.circle_shape);
                        GradientDrawable drawable = (GradientDrawable) v.getBackground();
                        drawable.setColor(Color.parseColor("#ccccff"));
//                        view.setBackgroundColor(Color.parseColor("#ccccff"));
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                    }
                    case MotionEvent.ACTION_CANCEL: {
//                        ImageView view = (ImageView) v;
                        LinearLayout view = (LinearLayout) v;
                        //clear the overlay
                        view.setBackgroundColor(0);
//                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    public void setOnClickEffect(final Activity activity, ImageView imageView) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    public void backAction(final Activity activity, LinearLayout imageView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public void startActivityAction(LinearLayout button,final Context context, final Class<?> target){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, target);
                context.startActivity(intent);
            }
        });
    }

    public void showDialogForHint(final Activity activity, final ImageView btnHelp, final String jawabanTebakan, final String imageUrl, final int level,final String idGambar){

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHintManager.getInstance().setDialogHint(activity, R.layout.dialog_hint, new DialogHintManager.CallBackDialog() {
                    @Override
                    public void yes(AppCompatDialog dialog) {
                        if (CoinsManager.getInstance().isEnoughCoinHint(activity)) {
                            CoinsManager.getInstance().setCoinsOnHintActivity(activity);
                            startActivityHintsTebakanGambar(activity, jawabanTebakan, imageUrl, level, idGambar);
                            dialog.dismiss();
                        } else {
                            CoinsManager.getInstance().showDialogZeroCoin(activity);
                            dialog.dismiss();
                        }


                    }
                });

            }
        });
    }

    public void showDialogSocialMediaTebakKata(final Context context,final Activity activity, final ImageView btnShare, final String tebakKata){

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSocialMedia.getInstance().showShareDialogTebakKata(context, activity, tebakKata);

            }
        });
    }

    public void showDialogSocialMediaTebakGambar(final Context context, final Activity activity, final ImageView btnShare, final String imageUrl){

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSocialMedia.getInstance().showShareDialogTebakGambar(context,activity,imageUrl);

            }
        });
    }

    public void showDialogForHintTebakKata(final Activity activity, final ImageView btnHelp, final String jawabanTebakan, final String tebakanKata, final int level){

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHintManager.getInstance().setDialogHint(activity, R.layout.dialog_hint, new DialogHintManager.CallBackDialog() {
                    @Override
                    public void yes(AppCompatDialog dialog) {
                        if(CoinsManager.getInstance().isEnoughCoinHint(activity)){
                            CoinsManager.getInstance().setCoinsOnHintActivity(activity);
                            startActivityHintsTebakKata(activity,jawabanTebakan,tebakanKata,level);
                            dialog.dismiss();
                        }else{
                            CoinsManager.getInstance().showDialogZeroCoin(activity);
                            dialog.dismiss();
                        }

                    }
                });

            }
        });
    }

    private void startActivityHintsTebakanGambar(final Activity activity,final String jawabanTebakan,final String imageUrl,int level,String idGambar){
        Intent intent = new Intent(activity, HintsTebakGambarActivity.class);
        intent.putExtra(ApplicationConstants.jawabanTebakan, jawabanTebakan);
        intent.putExtra(ApplicationConstants.imageUrl, imageUrl);
        intent.putExtra(ApplicationConstants.keyGambar,idGambar);
        intent.putExtra(ApplicationConstants.level,level);
        activity.startActivity(intent);

    }

    private void startActivityHintsTebakKata(final Activity activity,final String jawabanTebakan,String tebakanKata,int level){
        Intent intent = new Intent(activity, HintsTebakKataActivity.class);
        intent.putExtra(ApplicationConstants.jawabanTebakan, jawabanTebakan);
        intent.putExtra(ApplicationConstants.tebakanKata, tebakanKata);
        intent.putExtra(ApplicationConstants.level,level);
        activity.startActivity(intent);

    }


    public Map<String,String> getDataFromIntent(Activity activity){
        Intent  intent =activity.getIntent();
        Map<String,String> value = new HashMap<>();
        try{

            if(intent.getExtras()!=null){
                value.put(ApplicationConstants.imageUrl,intent.getExtras().getString(ApplicationConstants.imageUrl));
                value.put(ApplicationConstants.jawabanTebakan,intent.getExtras().getString(ApplicationConstants.jawabanTebakan));
                value.put(ApplicationConstants.level, String.valueOf(intent.getExtras().getInt(ApplicationConstants.level)));
                value.put(ApplicationConstants.keyGambar, String.valueOf(intent.getExtras().getString(ApplicationConstants.keyGambar)));


                return value;
            }else
                return value;

        }catch (NullPointerException x){
            x.printStackTrace();
        }
        return value;
    }

    public Map<String,String> getDataFromIntentTebakKata(Activity activity){
        Intent  intent =activity.getIntent();
        Map<String,String> value = new HashMap<>();
        try{

            if(intent.getExtras()!=null){
                value.put(ApplicationConstants.tebakanKata,intent.getExtras().getString(ApplicationConstants.tebakanKata));
                value.put(ApplicationConstants.jawabanTebakan,intent.getExtras().getString(ApplicationConstants.jawabanTebakan));
                value.put(ApplicationConstants.level, String.valueOf(intent.getExtras().getInt(ApplicationConstants.level)));


                return value;
            }else
                return value;

        }catch (NullPointerException x){
            x.printStackTrace();
        }
        return value;
    }

}
