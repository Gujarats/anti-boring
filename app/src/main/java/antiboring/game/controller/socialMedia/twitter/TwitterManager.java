package antiboring.game.controller.socialMedia.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import antiboring.game.R;
import antiboring.game.controller.tebakanManager.Tebakan;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Gujarat Santana on 30/12/15.
 */
public class TwitterManager {
    private static final String TAG = "TwitterManager";
    public static TwitterManager instance;
    private static RequestToken requestToken;
    private Activity activity;
    private Context context;
    private SessionTwitter sessionTwitter;
    private String imageUrl;
    private String tebakanKata;
    private Twitter twitter;
    private TwitterManager() {}

    public static TwitterManager getInstance() {
        if (instance == null) instance = new TwitterManager();
        return instance;
    }

    public void initTwitterTebakKata(Activity activity, Context context, String tebakanKata){
        /**
         *  Enabling strict mode for twitter
         *  */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.activity =activity;
        this.context = context;
        this.sessionTwitter = new SessionTwitter(activity);
        this.tebakanKata = tebakanKata;

        if (!isLogin(activity)) {

            Uri uri = activity.getIntent().getData();
            if (uri != null && uri.toString().startsWith(TwitterConstants.callbackTwitter)) {
                // oAuth verifier
                Log.d("masukkk", "gan");
                String verifier = uri
                        .getQueryParameter(TwitterConstants.oAuthVerifier);

                try {
                    // Get the access token
                    AccessToken accessToken = twitter.getOAuthAccessToken(
                            requestToken, verifier);

                    // Getting user details from twitter
                    // For now i am getting his name only
                    long userID = accessToken.getUserId();
                    User user = twitter.showUser(userID);
                    String username = user.getName();

                    sessionTwitter.setLogin(true);
                    sessionTwitter.setPrefKeyOauthSecret(accessToken.getTokenSecret());
                    sessionTwitter.setPrefKeyOauthToken(accessToken.getToken());


                    // Displaying in xml ui
                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                }
            }
        }
    }

    public void initTwitterTebakGambar(Activity activity, Context context, String imageUrl){
        /**
         *  Enabling strict mode for twitter
         *  */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.activity =activity;
        this.context = context;
        this.sessionTwitter = new SessionTwitter(activity);
        this.imageUrl = imageUrl;

        if (!isLogin(activity)) {

            Uri uri = activity.getIntent().getData();
            if (uri != null && uri.toString().startsWith(TwitterConstants.callbackTwitter)) {
                // oAuth verifier
                Log.d("masukkk", "gan");
                String verifier = uri
                        .getQueryParameter(TwitterConstants.oAuthVerifier);

                try {
                    // Get the access token
                    AccessToken accessToken = twitter.getOAuthAccessToken(
                            requestToken, verifier);

                    // Getting user details from twitter
                    // For now i am getting his name only
                    long userID = accessToken.getUserId();
                    User user = twitter.showUser(userID);
                    String username = user.getName();

                    sessionTwitter.setLogin(true);
                    sessionTwitter.setPrefKeyOauthSecret(accessToken.getTokenSecret());
                    sessionTwitter.setPrefKeyOauthToken(accessToken.getToken());


                    // Displaying in xml ui
                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                }
            }
        }
    }

    public void shareTwitterTebakGambar(final Activity activity){

        if(isLogin(activity)){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                        Thread backgroundTask = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sendTebakanGambarToTwitter("#AntiBoring Gambar Apa itu?");
                            }
                        });
                        backgroundTask.start();
                        Toast.makeText(activity, "Yeayy Share Twitter Success", Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            loginToTwitter();
        }


    }

    public void shareTwitterTebakKata(final Activity activity){

        if(isLogin(activity)){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Thread backgroundTask = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendTebakanKataToTwitter("#AntiBoring Kira - kira apa ya?");
                        }
                    });
                    backgroundTask.start();
                    Toast.makeText(activity, "Yeayy Share Twitter Success", Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            loginToTwitter();
        }


    }

    private void loginToTwitter() {

        final ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(TwitterConstants.consumerKeyTwitter);
        builder.setOAuthConsumerSecret(TwitterConstants.consumerSecretTwitter);

        final Configuration configuration = builder.build();
        final TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();
        try {
            requestToken = twitter.getOAuthRequestToken(TwitterConstants.callbackTwitter);

            /**
             *  Loading twitter login page on webview for authorization
             *  Once authorized, results are received at onActivityResult
             *  */
            final Intent intent = new Intent(context, WebViewTwitterActivity.class);
            intent.putExtra(WebViewTwitterActivity.EXTRA_URL, requestToken.getAuthenticationURL());
            activity.startActivityForResult(intent, TwitterConstants.WEBVIEW_REQUEST_CODE_Twitter);

        } catch (TwitterException e) {
            e.printStackTrace();
        }

    }

    public void onActivityResult(Activity activity,int requestCode, int resultCode,Intent data){
        if (requestCode == TwitterConstants.WEBVIEW_REQUEST_CODE_Twitter) {
            if (resultCode == Activity.RESULT_OK) {
                String verifier = data.getExtras().getString(TwitterConstants.oAuthVerifier);
                try {
                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

                    long userID = accessToken.getUserId();
                    final User user = twitter.showUser(userID);
                    String username = user.getName();

                    sessionTwitter.setLogin(true);
                    sessionTwitter.setPrefKeyOauthSecret(accessToken.getTokenSecret());
                    sessionTwitter.setPrefKeyOauthToken(accessToken.getToken());

                    shareTwitterTebakGambar(activity);


                } catch (Exception e) {
                    //Log.e("Twitter Login Failed", e.getMessage());
                }
            }
        }
    }

    public boolean isLogin(Activity activity){
        SessionTwitter sessionTwitter = new SessionTwitter(activity);
        return sessionTwitter.isLogin();
    }

    public void sendTebakanKataToTwitter(String status){
        String statusku;
        try {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TwitterConstants.consumerKeyTwitter);
            builder.setOAuthConsumerSecret(TwitterConstants.consumerSecretTwitter);

            // Access Token
            String access_token = sessionTwitter.getPrefKeyOauthToken();

            //Access Token Secret
            String access_token_secret = sessionTwitter.getPrefKeyOauthSecret();

            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            twitter4j.Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

            // Update status
            if (status.length() >= 100) {

                statusku = status.substring(0, 70);
            } else {
                statusku = status;
            }
            StatusUpdate statusUpdate = new StatusUpdate(statusku + "Game AntiBoring");

            //get Image and save it ot file
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bmp = getBitmapTebakKata(tebakanKata);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            InputStream is = new ByteArrayInputStream(stream.toByteArray());
            statusUpdate.setMedia("antiBoring.jpg", is);

            twitter.updateStatus(statusUpdate);


        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public void sendTebakanGambarToTwitter(String status){
        String statusku;
        try {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TwitterConstants.consumerKeyTwitter);
            builder.setOAuthConsumerSecret(TwitterConstants.consumerSecretTwitter);

            // Access Token
            String access_token = sessionTwitter.getPrefKeyOauthToken();

            //Access Token Secret
            String access_token_secret = sessionTwitter.getPrefKeyOauthSecret();

            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            twitter4j.Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

            // Update status
            if (status.length() >= 100) {

                statusku = status.substring(0, 70);
            } else {
                statusku = status;
            }
            StatusUpdate statusUpdate = new StatusUpdate(statusku + "Game AntiBoring");

            //get Image and save it ot file
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bmp = getBitmapForShareTebakGambar(context, activity, imageUrl);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            InputStream is = new ByteArrayInputStream(stream.toByteArray());
            statusUpdate.setMedia("antiBoring.jpg", is);

            twitter.updateStatus(statusUpdate);


        } catch (TwitterException e) {
            e.printStackTrace();
        }
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

    private Bitmap getBitmapTebakKata(String tebakKata){
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
