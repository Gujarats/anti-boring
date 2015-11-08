package santana.tebaktebakan.loadTebakan;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import santana.tebaktebakan.object.TebakanGambarObject;

/**
 * Created by Gujarat Santana on 08/11/15.
 */
public class Tebakan {

    public static Tebakan instance;

    private Tebakan() {}

    public static Tebakan getInstance() {
        if (instance == null) instance = new Tebakan();
        return instance;
    }

    public List<TebakanGambarObject> getImageLevel(int level, Activity activity){
        try {
            List<TebakanGambarObject> tebakanGambarObjects = new ArrayList<TebakanGambarObject>();
            String json = loadJSONFromAsset(activity);
            if(!json.isEmpty()){
                //get json from asset
                JSONObject obj = new JSONObject(json);

                //get specific level from json
                JSONArray m_jArry = obj.getJSONArray(String.valueOf(level));


                //get content from jsonArray to array list TebakanGambarObject
                for (int i = 0; i < m_jArry.length(); i++) {
                    JSONObject tebakanObjectLevel = m_jArry.getJSONObject(i);
                    TebakanGambarObject tebakanGambarObject = new TebakanGambarObject();
                    tebakanGambarObject.setGambarUrl(tebakanObjectLevel.getString("gambar_url"));
                    tebakanGambarObject.setJawaban(tebakanObjectLevel.getString("jawaban"));
                    tebakanGambarObjects.add(tebakanGambarObject);
                }

                return tebakanGambarObjects;
            }else{
                TebakanGambarObject tebakanGambarObject = new TebakanGambarObject();
                tebakanGambarObject.setGambarUrl("");
                tebakanGambarObject.setGambarUrl("");
                tebakanGambarObjects.add(tebakanGambarObject);
                return tebakanGambarObjects;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String loadJSONFromAsset(Activity activity) {
        String json = "";
        try {
            InputStream is = activity.getAssets().open("gambar_database.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return json;
    }

    public void loadImageToImageView(ImageView imageView,String resName, Context context){
        int resource = getID(resName,context);
        Picasso.with(context).load(resource).into(imageView);
    }

    public int getID(String resourceName,Context context){
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(resourceName, "drawable",
                context.getPackageName());
        return resourceId;
    }
}
