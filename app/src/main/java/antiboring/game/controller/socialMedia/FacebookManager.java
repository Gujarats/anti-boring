package antiboring.game.controller.socialMedia;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
    public static FacebookManager instance;
    CallbackManager callbackManager;
    private LoginManager loginManager;

    private FacebookManager() {}

    public static FacebookManager getInstance() {
        if (instance == null) instance = new FacebookManager();
        return instance;
    }

    public void InitFacebook(Context context, final Activity activity){
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();


    }


    public void loginFacebook(final Activity activity,final String imageUrl){
        List<String> permissionNeeds = Arrays.asList("publish_actions");
        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(activity, permissionNeeds);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(activity, "Success share facebook", Toast.LENGTH_SHORT).show();
                shareTebakGambar(activity,imageUrl);
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

    public void shareTebakGambar(Activity activity,String imageUrl){
        Bitmap image = getBitmapForShareTebakGambar(activity,imageUrl);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
    }

    private Bitmap getBitmapForShareTebakGambar(Activity activity,String imageUrl) {
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);

        //Inflate the layout into a view and configure it the way you like
        RelativeLayout view = new RelativeLayout(activity);
        mInflater.inflate(R.layout.layout_tebak_gambar_share, view, true);
        ImageView TebakGambar = (ImageView) view.findViewById(R.id.TebakGambar);

        //set height image
        Tebakan.getInstance().setSizeImageView(activity,TebakGambar);
        //set image to imageView
        Tebakan.getInstance().loadImageToImageView2(TebakGambar,imageUrl,activity);

        //Provide it with a layout params. It should necessarily be wrapping the
        //content as we not really going to have a parent for it.
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        //Pre-measure the view so that height and width don't remain null.
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //Assign a size and position to the view and all of its descendants
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

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
