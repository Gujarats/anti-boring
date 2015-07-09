//package santana.tebaktebakan.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.media.MediaScannerConnection;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.naver.android.helloyako.imagecrop.view.ImageCropView;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//import santana.tebaktebakan.R;
//import santana.tebaktebakan.session.SessionManager;
//
///**
// * Created by Gujarat Santana on 03/07/15.
// */
//public class CropActivity extends Activity {
//    public static final String TAG = "CropActivity";
//
//    private ImageCropView imageCropView;
//    private SessionManager sessionManager;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_crop);
//        imageCropView = (ImageCropView) findViewById(R.id.image);
//
//        Intent i = getIntent();
//        Uri uri = i.getData();
//
//        imageCropView.setImageFilePath(uri.toString());
//
//        imageCropView.setAspectRatio(1,1);
//
//        findViewById(R.id.ratio11btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "click 1 : 1");
//                if(isPossibleCrop(1,1)){
//                    imageCropView.setAspectRatio(1, 1);
//                } else {
//                    Toast.makeText(CropActivity.this, R.string.can_not_crop, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
////        findViewById(R.id.ratio34btn).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Log.d(TAG, "click 3 : 4");
////                if(isPossibleCrop(3,4)){
////                    imageCropView.setAspectRatio(3, 4);
////                } else {
////                    Toast.makeText(CropActivity.this,R.string.can_not_crop,Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
////
////        findViewById(R.id.ratio43btn).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Log.d(TAG, "click 4 : 3");
////                if(isPossibleCrop(4,3)){
////                    imageCropView.setAspectRatio(4, 3);
////                } else {
////                    Toast.makeText(CropActivity.this,R.string.can_not_crop,Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
////
////        findViewById(R.id.ratio169btn).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Log.d(TAG, "click 16 : 9");
////                if(isPossibleCrop(16,9)){
////                    imageCropView.setAspectRatio(16, 9);
////                } else {
////                    Toast.makeText(CropActivity.this,R.string.can_not_crop,Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
////
////        findViewById(R.id.ratio916btn).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Log.d(TAG, "click 9 : 16");
////                if(isPossibleCrop(9,16)){
////                    imageCropView.setAspectRatio(9, 16);
////                } else {
////                    Toast.makeText(CropActivity.this,R.string.can_not_crop,Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
//
//        findViewById(R.id.crop_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!imageCropView.isChangingScale()) {
//                    Bitmap b = imageCropView.getCroppedImage();
//                    File f = bitmapConvertToFile(b);
//                }
//            }
//        });
//    }
//
//    private boolean isPossibleCrop(int widthRatio, int heightRatio){
//        int bitmapWidth = imageCropView.getViewBitmap().getWidth();
//        int bitmapHeight = imageCropView.getViewBitmap().getHeight();
//        if(bitmapWidth < widthRatio && bitmapHeight < heightRatio){
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public File bitmapConvertToFile(Bitmap bitmap) {
//        FileOutputStream fileOutputStream = null;
//        File bitmapFile = null;
//        try {
//            File file = new File(Environment.getExternalStoragePublicDirectory("tebakan"),"");
//            if (!file.exists()) {
//                file.mkdir();
//            }
//
//            bitmapFile = new File(file, "IMG_" + (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime()) + ".jpg");
//            fileOutputStream = new FileOutputStream(bitmapFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
//            MediaScannerConnection.scanFile(this, new String[]{bitmapFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
//                @Override
//                public void onMediaScannerConnected() {
//
//                }
//
//                @Override
//                public void onScanCompleted(String path, Uri uri) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(CropActivity.this, "file saved", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (fileOutputStream != null) {
//                try {
//                    fileOutputStream.flush();
//                    fileOutputStream.close();
//                } catch (Exception e) {
//                }
//            }
//        }
//
//        return bitmapFile;
//    }
//
//}
//
