package santana.tebaktebakan.storageMemory;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by root on 25/04/15.
 */
public class SavingFile {
    private String fileName;
    private Bitmap bitmap;
    private Context context;

    public SavingFile(){}

    public SavingFile(Context context, String fileName, Bitmap bitmap) {
        this.fileName = fileName;
        this.bitmap = bitmap;
        this.context = context;
    }

    public String saveBitmapToInternal(){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("persona", Context.MODE_PRIVATE);
        // Create imageDir
        File imageFile=new File(directory,fileName);
        if (imageFile.exists ()) imageFile.delete ();

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(imageFile);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile.getAbsolutePath();
    }

    public String SaveBitmapToExternal() {

//        String root = Environment.getExternalStorageDirectory().toString();
        File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/persona");
        directory.mkdirs();


        File fileImage = new File(directory, fileName);
        if (fileImage.exists ()) fileImage.delete ();
        try {
            FileOutputStream out = new FileOutputStream(fileImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileImage.getAbsolutePath();
    }


    public File SaveBitmapToExternalFile() {

        String root = Environment.getExternalStorageDirectory().toString();
        File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/persona");
        directory.mkdirs();


        File fileImage = new File(directory, fileName);
        if (fileImage.exists ()) fileImage.delete ();
        try {
            FileOutputStream out = new FileOutputStream(fileImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileImage;
    }

    public File saveBitmapToInternalTesting(byte[] buffer){
        try {
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            fos.write(buffer);
            fos.close();
            //returning file path
            File file = new File(context.getFilesDir()+"/"+fileName);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public File saveBitmapToInternalFile(){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("persona", Context.MODE_PRIVATE);
        // Create imageDir
        File imageFile=new File(directory,fileName);
        if (imageFile.exists ()) imageFile.delete ();

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(imageFile);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }
    /*
    use this method to get Wirte Acces status to external storage
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void genreteConfigFielChat(){
        try
        {
//            File root = new File(context.getFilesDir(), "chatConfig.txt");
//
//            File mydir = context.getDir("PersonaDir", Context.MODE_PRIVATE); //Creating an internal dir;
//            File fileWithinMyDir = new File(mydir, "chatConfig.txt"); //Getting a file within the dir.
//            FileOutputStream out = new FileOutputStream(fileWithinMyDir);

            ContextWrapper contextWrapper = new ContextWrapper(context);
            File directory = contextWrapper.getDir("PersonaDir", Context.MODE_PRIVATE);
            File myInternalFile = new File(directory , "chatConfig.txt");


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("check",true);
            String string = jsonObject.toString();
            FileOutputStream outputStream;

            try {
                outputStream = new FileOutputStream(myInternalFile);
                outputStream.write(string.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String data) {

        try {
            ContextWrapper contextWrapper = new ContextWrapper(context);
            File directory = contextWrapper.getDir("PersonaDir", Context.MODE_PRIVATE);
            File myInternalFile = new File(directory , "chatConfig.txt");

            FileOutputStream fos = new FileOutputStream(myInternalFile);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String readFromFile() {

        String ret = "";

        try {
            ContextWrapper contextWrapper = new ContextWrapper(context);
            File directory = contextWrapper.getDir("PersonaDir", Context.MODE_PRIVATE);
            File myInternalFile = new File(directory , "chatConfig.txt");

            FileInputStream fis = new FileInputStream(myInternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                ret = ret + strLine;
            }
            in.close();
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
