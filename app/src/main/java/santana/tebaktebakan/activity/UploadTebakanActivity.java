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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import santana.tebaktebakan.AppController;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ServerConstants;
import santana.tebaktebakan.imageManager.BitmapLoader;
import santana.tebaktebakan.imageManager.UploadCompleted;
import santana.tebaktebakan.requestNetwork.CostumRequest2;
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
    private EditText TextTebakanUpload,TextKunciTebakan;
    private String UrlGambarTebakan="no_data";
    private Uri selectedimg=null;
    private String TextTebakan,KunciTebakan;
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
        TextKunciTebakan = (EditText) findViewById(R.id.TextKunciTebakan);
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
                KunciTebakan = TextKunciTebakan.getText().toString();
                if(!KunciTebakan.equalsIgnoreCase("") && !KunciTebakan.trim().toString().isEmpty()){
                    TextTebakan = TextTebakanUpload.getText().toString();
                    if(!TextTebakan.equalsIgnoreCase("") && !TextTebakan.trim().toString().isEmpty()){

                        /**
                         * upload wheter image only or text only or both
                         */

                        if(selectedimg!=null && !selectedimg.equals("")){
                            Log.d("UPload","imageUpload");
                            Bitmap bitmap = BitmapLoader.decodePathtoBitmap(getRealPathFromURI(getApplicationContext(), selectedimg), 400, 400);

                            String filname = "IMG_" + sessionManager.getUidUser() + "_" + String.valueOf(System.currentTimeMillis())+".jpg";
                            SavingFile savingFile = new SavingFile(getApplicationContext(),filname,bitmap);
                            File pathImage =null;
                            if(savingFile.isExternalStorageWritable()){
                                pathImage = savingFile.SaveBitmapToExternalFile();
                            }
                            else{
                                pathImage = savingFile.saveBitmapToInternalFile();
                            }

                            Map<String,String> mParams = new HashMap<String,String>();
                            mParams.put(ServerConstants.mParamsToken,sessionManager.getToken());
                            mParams.put(ServerConstants.mParams_idUser,sessionManager.getUidUser());
                            mParams.put(ServerConstants.mParamsTextTebakan,TextTebakan);
                            mParams.put(ServerConstants.mParamsGambarTebakan,UrlGambarTebakan);
                            mParams.put(ServerConstants.mParamsKunciTebakan,KunciTebakan);
                            mParams.put(ServerConstants.mParamsGcmID, sessionManager.getGcmID());
                            CostumRequest2 myreq = new CostumRequest2(Request.Method.POST,ServerConstants.insertTebakanTextGambar,mParams, pathImage,filname, UploadTebakanActivity.this,UploadTebakanActivity.this);
                            myreq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            AppController.getInstance().addToRequestQueue(myreq);

//                            String filname = "IMG_" + sessionManager.getUidUser() + "_" + String.valueOf(System.currentTimeMillis())+".jpg";
//
//
//
//                            SavingFile savingFile = new SavingFile(getApplicationContext(),filname,bitmap);
//                            String pathImage =null;
//                            if(savingFile.isExternalStorageWritable()){
//                                pathImage = savingFile.SaveBitmapToExternal();
//                            }
//                            else{
//                                pathImage = savingFile.saveBitmapToInternal();
//                            }
//
//                            UploadImage uploadImage = new UploadImage(getApplicationContext(),pathImage,progresbarUpload,percentageUpload,this);
//                            uploadImage.execute();
                        }else{
                            Log.d("UPload","TextUPload");
                            UploadTebakanText();
                        }

                    }else{
                        if(selectedimg!=null && !selectedimg.equals("")){
                            Log.d("UPload","TextUPload");
                            Bitmap bitmap = BitmapLoader.decodePathtoBitmap(getRealPathFromURI(getApplicationContext(), selectedimg), 500, 500);
                            String filname = "IMG_" + sessionManager.getUidUser() + "_" + String.valueOf(System.currentTimeMillis())+".jpg";
                            SavingFile savingFile = new SavingFile(getApplicationContext(),filname,bitmap);
                            File pathImage =null;
                            if(savingFile.isExternalStorageWritable()){
                                pathImage = savingFile.SaveBitmapToExternalFile();
                            }
                            else{
                                pathImage = savingFile.saveBitmapToInternalFile();
                            }

                            Map<String,String> mParams = new HashMap<String,String>();
                            mParams.put(ServerConstants.mParamsToken,sessionManager.getToken());
                            mParams.put(ServerConstants.mParams_idUser,sessionManager.getUidUser());
                            mParams.put(ServerConstants.mParamsTextTebakan,TextTebakan);
                            mParams.put(ServerConstants.mParamsGambarTebakanUrl,UrlGambarTebakan);
                            mParams.put(ServerConstants.mParamsKunciTebakan,KunciTebakan);
                            mParams.put(ServerConstants.mParamsGcmID, sessionManager.getGcmID());
                            CostumRequest2 myreq = new CostumRequest2(Request.Method.POST,ServerConstants.insertTebakanTextGambar,mParams, pathImage, filname,UploadTebakanActivity.this,UploadTebakanActivity.this);
                            myreq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            AppController.getInstance().addToRequestQueue(myreq);
//
//                            UploadImage uploadImage = new UploadImage(getApplicationContext(),pathImage,progresbarUpload,percentageUpload,this);
//                            uploadImage.execute();
                        }
                    }
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
        if(UrlGambarTebakan.isEmpty()){
            UrlGambarTebakan = "no data image";
        }
            Map<String,String> mParams = new HashMap<String,String>();
            mParams.put(ServerConstants.mParamsToken,sessionManager.getToken());
            mParams.put(ServerConstants.mParams_idUser,sessionManager.getUidUser());
            mParams.put(ServerConstants.mParamsTextTebakan,TextTebakan);
            mParams.put(ServerConstants.mParamsGambarTebakan,UrlGambarTebakan);
            mParams.put(ServerConstants.mParamsKunciTebakan,KunciTebakan);
            mParams.put(ServerConstants.mParamsGcmID,sessionManager.getGcmID());
            CostumRequestString myreq = new CostumRequestString(com.android.volley.Request.Method.POST,ServerConstants.insertTebakanTextGambar,mParams,UploadTebakanActivity.this,UploadTebakanActivity.this);
            myreq.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(myreq);
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
