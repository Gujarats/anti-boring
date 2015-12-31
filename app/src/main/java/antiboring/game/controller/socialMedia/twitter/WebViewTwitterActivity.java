package antiboring.game.controller.socialMedia.twitter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import antiboring.game.R;

/**
 * Created by Gujarat Santana on 31/12/15.
 */
public class WebViewTwitterActivity extends AppCompatActivity {

    public static String EXTRA_URL = "extra_url";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_weview_twitter);

        setTitle("Login");

        final String url = this.getIntent().getStringExtra(EXTRA_URL);
        if (null == url) {
            Log.e("Twitter", "URL cannot be null");
            finish();
        }

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.contains(TwitterConstants.callbackTwitter)) {
                Uri uri = Uri.parse(url);

				/* Sending results back */
                String verifier = uri.getQueryParameter( "oauth_verifier");
                Intent resultIntent = new Intent();
                resultIntent.putExtra( "oauth_verifier", verifier);
                setResult(RESULT_OK, resultIntent);

				/* closing webview */
                finish();
                return true;
            }
            return false;
        }
    }

}
