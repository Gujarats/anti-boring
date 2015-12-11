package santana.tebaktebakan.controller.UIManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import santana.tebaktebakan.R;

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
                lvl = intent.getExtras().getInt("LEVEL_STAGE");

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
}
