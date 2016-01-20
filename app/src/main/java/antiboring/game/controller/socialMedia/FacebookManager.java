package antiboring.game.controller.socialMedia;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Arrays;
import java.util.List;

import antiboring.game.R;
import antiboring.game.controller.tebakanManager.Tebakan;

/**
 * Created by Gujarat Santana on 30/12/15.
 */
public class FacebookManager {
    private static final String TAG = "FacebookManager";
    public static FacebookManager instance;
    CallbackManager callbackManager;
    private LoginManager loginManager;

    private FacebookManager() {}

    public static FacebookManager getInstance() {
        if (instance == null) instance = new FacebookManager();
        return instance;
    }

    public void InitFacebook(Context context){
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
    }


    public void loginFacebookShareAntiBoring(final Context context,final Activity activity){
        List<String> permissionNeeds = Arrays.asList("publish_actions");
        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(activity, permissionNeeds);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Success share facebook", Toast.LENGTH_SHORT).show();
                        shareAntiBoring(context);
                    }
                });


            }

            @Override
            public void onCancel() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Cancel share facebook", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onError(FacebookException error) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Error share facebook", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void loginFacebookTebakGambar(final Context context, final Activity activity, final String imageUrl){
        List<String> permissionNeeds = Arrays.asList("publish_actions");
        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(activity, permissionNeeds);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(activity, "Success share facebook", Toast.LENGTH_SHORT).show();
                shareTebakGambar(context, activity, imageUrl);
            }

            @Override
            public void onCancel() {
                Toast.makeText(activity, "Cancel share facebook", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(activity, "Error share facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginFacebookTebakKata(final Context context, final Activity activity, final String tebakKata){
        List<String> permissionNeeds = Arrays.asList("publish_actions");
        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(activity, permissionNeeds);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(activity, "Success share facebook", Toast.LENGTH_SHORT).show();
                shareTebakKata(context, activity, tebakKata);
            }

            @Override
            public void onCancel() {
                Toast.makeText(activity, "Cancel share facebook", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(activity, "Error share facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public CallbackManager getCallbackManager(){
        return this.callbackManager;
    }

    public void shareAntiBoring(Context context){
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.header_);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
    }

    public void shareTebakGambar(Context context, Activity activity,String imageUrl){
        Log.d(TAG, "shareTebakGambar() called with: " + "context = [" + context + "], activity = [" + activity + "], imageUrl = [" + imageUrl + "]");
        Bitmap image = getBitmapForShareTebakGambar(context,activity,imageUrl);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
    }

    public void shareTebakKata(Context context, Activity activity,String tebakkata){
        Log.d(TAG, "shareTebakGambar() called with: " + "context = [" + context + "], activity = [" + activity + "], imageUrl = [" + tebakkata + "]");
        Bitmap image = getBitmapTebakKata(activity, tebakkata);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
    }

    private Bitmap getBitmapForShareTebakGambar(Context context ,Activity activity,String imageUrl) {
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);

        //Inflate the layout into a view and configure it the way you like
        RelativeLayout view = new RelativeLayout(activity);
        mInflater.inflate(R.layout.layout_tebak_gambar_share, view, true);
        ImageView TebakGambar = (ImageView) view.findViewById(R.id.TebakGambar);

        //set size image view
        Tebakan.getInstance().setSizeImageView(activity,TebakGambar);

        //set image view
        TebakGambar.setImageResource(Tebakan.getInstance().getID(imageUrl, context));

        //Provide it with a layout params. It should necessarily be wrapping the
        //content as we not really going to have a parent for it.
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        //Pre-measure the view so that height and width don't remain null.
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //Assign a size and position to the view and all of its descendants
        view.layout(view.getMeasuredWidth(), view.getMeasuredHeight(), view.getMeasuredWidth(), view.getMeasuredHeight());

        //Create the bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        //Create a canvas with the specified bitmap to draw into
        Canvas c = new Canvas(bitmap);

        //Render this view (and all of its children) to the given Canvas
        view.draw(c);
        return bitmap;
    }

    private Bitmap getBitmapTebakKata(Activity activity,String tebakKata){
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);

        //Inflate the layout into a view and configure it the way you like
        RelativeLayout view = new RelativeLayout(activity);
        mInflater.inflate(R.layout.layout_tebak_kata_share, view, true);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layoutTebakKata);
        AppCompatTextView tebakanKata = (AppCompatTextView) view.findViewById(R.id.TebakKata);

        //set size image view
        Tebakan.getInstance().setSizeLinearLayout(activity, layout);

        //set TExt tebakan
        tebakanKata.setText(tebakKata);

        //Provide it with a layout params. It should necessarily be wrapping the
        //content as we not really going to have a parent for it.
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        //Pre-measure the view so that height and width don't remain null.
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //Assign a size and position to the view and all of its descendants
        view.layout(view.getMeasuredWidth(), view.getMeasuredHeight(), view.getMeasuredWidth(), view.getMeasuredHeight());

        //Create the bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        //Create a canvas with the specified bitmap to draw into
        Canvas c = new Canvas(bitmap);

        //Render this view (and all of its children) to the given Canvas
        view.draw(c);
        return bitmap;

    }

}
