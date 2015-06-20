package santana.tebaktebakan.imageManager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

import santana.tebaktebakan.common.ServerConstants;
import santana.tebaktebakan.session.SessionManager;

/**
* Created by root on 24/04/15.
*/
public class UploadImage extends AsyncTask<Void, Integer, String> {

    private final static String TAG = "server";
    private long totalSize = 0;
    private File filePath;
    private ProgressBar progressBar;
    private TextView txtPercentage;
    private Context context;
    private SessionManager sessionManager;
    private UploadCompleted uploadCompleted;

    public UploadImage(Context context,String filePath,ProgressBar progressBar, TextView percentage, UploadCompleted uploadCompleted){
//        this.filePath=filePath;
        this.filePath= new File(filePath);
        this.progressBar=progressBar;
        this.txtPercentage=percentage;
        this.context=context;
        this.sessionManager = new SessionManager(context);
        this.uploadCompleted = uploadCompleted;
    }

    @Override
    protected void onPreExecute() {
        // setting progress bar to zero
        progressBar.setProgress(0);
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // Making progress bar visible
        progressBar.setVisibility(View.VISIBLE);

        // updating progress bar value
        progressBar.setProgress(progress[0]);

        // updating percentage value
        txtPercentage.setText(String.valueOf(progress[0]) + "%");
    }

    @Override
    protected String doInBackground(Void... params) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(ServerConstants.UploadGambarTebakan);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });


            entity.addPart("image", new FileBody(filePath.getAbsoluteFile()));

            totalSize = entity.getContentLength();
            httppost.setEntity(entity);
            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e(TAG, "respond: " + result);
        uploadCompleted.UploadCompleted(result);

        // showing the server response in an alert dialog
//        showAlert(result);

        super.onPostExecute(result);
    }

}
