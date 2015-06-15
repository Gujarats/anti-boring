package santana.tebaktebakan.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import santana.tebaktebakan.AppController;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ServerConstants;
import santana.tebaktebakan.imageManager.BitmapLoader;
import santana.tebaktebakan.imageManager.UploadCompleted;
import santana.tebaktebakan.imageManager.UploadImage;
import santana.tebaktebakan.requestNetwork.CostumRequestString;
import santana.tebaktebakan.session.SessionManager;
import santana.tebaktebakan.storageMemory.SavingFile;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class UploadTebakanActivity extends AppCompatActivity implements UploadCompleted, Response.Listener<String>, Response.ErrorListener{
    //session manager
    SessionManager sessionManager;
    private ImageView GambarTebakan;
    private Button Upload;
    private EditText TextTebakanUpload;
    private String UrlGambarTebakan;
    private Uri selectedimg=null;
    private String TextTebakan;
    //loading
    private ProgressBar progresbarUpload;
    private TextView percentageUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_upload_tebakan_activity);
        sessionManager = new SessionManager(getApplicationContext());
        initUI();
    }

    private void initUI(){
        TextTebakanUpload = (EditText) findViewById(R.id.TextTebakanUpload);
        GambarTebakan = (ImageView)findViewById(R.id.GambarTebakanUpload);
        percentageUpload = (TextView)findViewById(R.id.PercentageUpload);
        progresbarUpload = (ProgressBar)findViewById(R.id.ProgressBarUpload);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                selectedimg = data.getData();
                System.out.println("Paht image"+data.getData().getPath());
                GambarTebakan.setImageBitmap(BitmapLoader.decodePathtoBitmap(getRealPathFromURI(getApplicationContext(), selectedimg), 400, 400));
            }
        }

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void UploadCliced(View v){
        switch (v.getId()){
            case R.id.GambarTebakanUpload :
                //browse the file
                Intent fintent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                fintent.setType("image/jpeg");
                try {
//                    startActivityForResult(fintent, 101);
                    startActivityForResult(Intent.createChooser(fintent, "Choose Picture"), 101);
                } catch (ActivityNotFoundException e) {

                }
                break;
            case R.id.UploadTebakan :
                //Upload the image and the Text
                TextTebakan = TextTebakanUpload.getText().toString();
                if(!TextTebakan.equalsIgnoreCase("") && !TextTebakan.trim().toString().isEmpty()){
                    Bitmap bitmap = BitmapLoader.decodePathtoBitmap(getRealPathFromURI(getApplicationContext(), selectedimg), 400, 400);
                    String filname = "IMG_" + sessionManager.getUidUser() + "_" + String.valueOf(System.currentTimeMillis())+".jpg";
                    SavingFile savingFile = new SavingFile(getApplicationContext(),filname,bitmap);
                    String pathImage =null;
                    if(savingFile.isExternalStorageWritable()){
                        pathImage = savingFile.SaveBitmapToExternal();
                    }
                    else{
                        pathImage = savingFile.saveBitmapToInternal();
                    }

                    UploadImage uploadImage = new UploadImage(getApplicationContext(),pathImage,progresbarUpload,percentageUpload,this);
                    uploadImage.execute();
                }
                break;
        }
    }


    @Override
    public void UploadCompleted(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            UrlGambarTebakan = jsonObject.getString("response");
            UploadTebakanText();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void UploadTebakanText(){
        if(selectedimg!=null){
            Map<String,String> mParams = new HashMap<String,String>();
            mParams.put(ServerConstants.mParamsToken,sessionManager.getToken());
            mParams.put(ServerConstants.mParams_idUser,sessionManager.getUidUser());
            mParams.put(ServerConstants.mParamsTextTebakan,TextTebakan);
            mParams.put(ServerConstants.mParamsGambarTebakan,UrlGambarTebakan);
            CostumRequestString myreq = new CostumRequestString(com.android.volley.Request.Method.POST,ServerConstants.insertTebakan,mParams,UploadTebakanActivity.this,UploadTebakanActivity.this);
            myreq.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(myreq);
        }else{

        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error",error.getMessage());
        Toast.makeText(getApplicationContext(),"Wooww, Error man",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onResponse(String response) {
        Log.d("response",response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getString(ServerConstants.statusBeckend).equalsIgnoreCase(ServerConstants.statusBeckendOk)){
                Toast.makeText(getApplicationContext(),"Uploaded Complete",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Wooww, Error man",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
