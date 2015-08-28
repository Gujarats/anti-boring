package santana.tebaktebakan.socialMedia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import santana.tebaktebakan.activity.WebViewActivity;
import santana.tebaktebakan.session.SessionManager;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Gujarat Santana on 16/08/15.
 */
public class TwitterObject extends AsyncTask<String, String, Void> {
    private static RequestToken requestToken;
    private static Twitter twitter;
    private Context context;
    private SessionManager sessionManager;
    private String callbackUrl = "http://www.personatalk.com";
    private AppCompatActivity activity;
    private FinishShare finishShare;

    private Bitmap ImageTebakan;

    public TwitterObject(AppCompatActivity activity,Context context, FinishShare finishShare){
        this.context = context;
        this.activity = activity;
        this.finishShare=finishShare;
        sessionManager = new SessionManager(context);
    }

    public void initSessionTwitter(){

    }

    public void loginToTwitter(RequestToken requestToken,Twitter twitter) {
        //boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

        boolean isLoggedIn = sessionManager.getLoginTwitter();
        if (!isLoggedIn) {
            final ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(SocialMediaConstant.consumerKeyTwitter);
            builder.setOAuthConsumerSecret(SocialMediaConstant.consumerSecretTwitter);

            final Configuration configuration = builder.build();
            final TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();
            try {
                requestToken = twitter.getOAuthRequestToken(callbackUrl);

                /**
                 *  Loading twitter login page on webview for authorization
                 *  Once authorized, results are received at onActivityResult
                 *  */
                final Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
                activity.startActivityForResult(intent, SocialMediaConstant.WEBVIEW_REQUEST_CODE_Twitter);

            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            /**
             * twitter sudah login
             */
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    public void setBitmap(Bitmap bitmap){
        ImageTebakan = bitmap;
    }

//    Bitmap createBitmapTwitter(String status) {
//        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//
//        //Inflate the layout into a view and configure it the way you like
//        RelativeLayout view = new RelativeLayout(context);
//        mInflater.inflate(R.layout.layout_shared, view, true);
//
//        AutoResizeTextView tv = (AutoResizeTextView) view.findViewById(R.id.view);
//        tv.setText(status);
//        ImageView emoticonShre = (ImageView) view.findViewById(R.id.ShareImageEmoticon);
//        emoticonShre.setImageDrawable(icon_emot.getDrawable());
//
//
//        //Provide it with a layout params. It should necessarily be wrapping the
//        //content as we not really going to have a parent for it.
//        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//        //Pre-measure the view so that height and width don't remain null.
//        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//
//        //Assign a size and position to the view and all of its descendants
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//
//        //Create the bitmap
//        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
//                view.getMeasuredHeight(),
//                Bitmap.Config.ARGB_8888);
//        //Create a canvas with the specified bitmap to draw into
//        Canvas c = new Canvas(bitmap);
//
//        //Render this view (and all of its children) to the given Canvas
//        view.draw(c);
//        return bitmap;
//    }

    protected Void doInBackground(String... args) {

        String status = args[0];
        String statusku;
        try {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(SocialMediaConstant.consumerKeyTwitter);
            builder.setOAuthConsumerSecret(SocialMediaConstant.consumerSecretTwitter);

            // Access Token
            //String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
            String access_token = sessionManager.getPrefKeyOauthToken();

            //Access Token Secret
            // String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
            String access_token_secret = sessionManager.getPrefKeyOauthSecret();

            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            twitter4j.Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

            // Update status
            if (status.length() >= 100) {

                statusku = status.substring(0, 70);
            } else {
                statusku = status;
            }
            StatusUpdate statusUpdate = new StatusUpdate(statusku + "Game AntiBoring");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Bitmap bmp = Bitmap.createBitmap(bmImage.getDrawingCache());
//            Bitmap bmp = createBitmapTwitter(status);
            //TODO : disini dilakukan pengambilan gambar
//            bmp = ImageTebakan;
            ImageTebakan.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            InputStream is = new ByteArrayInputStream(stream.toByteArray());
            //InputStream is = getResources().openRawResource(R.drawable.ic_launcher);
            statusUpdate.setMedia("bray.jpg", is);

            twitter.updateStatus(statusUpdate);

//                Log.d("Status", response.getText());

        } catch (TwitterException e) {
//                Log.d("Failed to post!", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        finishShare.finishShare();
    }
}
