package santana.tebaktebakan.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import santana.tebaktebakan.AppController;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ServerConstants;
import santana.tebaktebakan.object.TebakanObject;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class TebakanListAdapter extends BaseAdapter implements Response.Listener<String>,Response.ErrorListener {

    private final int layout;
    private final LayoutInflater inflater;
    private List<TebakanObject> tebakanObjects;
    private Context context;
    private Activity activity;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public TebakanListAdapter(Context context, Activity activity, int layout){
        this.context  = context;
        this.activity = activity;
        inflater = activity.getLayoutInflater();
        this.layout=layout;
        tebakanObjects = new ArrayList<TebakanObject>();
    }

    @Override
    public int getCount() {
        return tebakanObjects.size();
    }

    @Override
    public Object getItem(int i) {
        return tebakanObjects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View converView, ViewGroup viewGroup){
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        View view = converView;
        final viewHoler holder;
        if (view == null) {
            view = inflater.inflate(layout, viewGroup, false);
            holder = new viewHoler();
            holder._idTebakan = (TextView) view.findViewById(R.id._idTebakan);
            holder.TextTebakan = (AppCompatTextView) view.findViewById(R.id.TextTebakan);
            holder.GambarTebakan = (NetworkImageView) view.findViewById(R.id.GambarTebakan);
            view.setTag(holder);
        } else {
            holder = (viewHoler) view.getTag();
            notifyDataSetChanged();
        }

        TebakanObject tebakanObject = tebakanObjects.get(position);
        populateView(holder,tebakanObject);

        return view;
    }

    private void populateView(viewHoler Holder, TebakanObject tebakanObject){
        Holder._idTebakan.setText(tebakanObject.get_idTebakan());
        Holder.TextTebakan.setText(tebakanObject.getTextTebakan());
        Holder.GambarTebakan.setImageUrl(tebakanObject.getUrlGambarTebakan(),imageLoader);
    }



    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error",error.getMessage());
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("response",response);
            if(jsonObject.getString(ServerConstants.statusBeckend).equalsIgnoreCase(ServerConstants.statusBeckendOk)){

                switch (jsonObject.getInt(ServerConstants.resultType)){
                    case ServerConstants.addTebakanView :
                        JSONArray jsonArray = jsonObject.getJSONArray(ServerConstants.dataListTebakan);
                        for(int i=0;i<jsonArray.length();i++){
                            TebakanObject tebakanObject = new TebakanObject();
                            tebakanObject.set_idTebakan(jsonArray.getJSONObject(i).getString(ServerConstants._idTebakan));
                            tebakanObject.setTextTebakan(jsonArray.getJSONObject(i).getString(ServerConstants.textTebakan));
                            tebakanObject.setUrlGambarTebakan(jsonArray.getJSONObject(i).getString(ServerConstants.gambarTebakan));
                            tebakanObjects.add(tebakanObject);
                            notifyDataSetChanged();
                        }
                        break;
                    case ServerConstants.addTebakanAtas :
                        JSONArray jsonArrayAtas = jsonObject.getJSONArray(ServerConstants.dataListTebakan);
                        List<TebakanObject> tempList = new ArrayList<TebakanObject>();
                        for(int i=0;i<jsonArrayAtas.length();i++){
                            TebakanObject tebakanObject = new TebakanObject();
                            tebakanObject.set_idTebakan(jsonArrayAtas.getJSONObject(i).getString(ServerConstants._idTebakan));
                            tebakanObject.setTextTebakan(jsonArrayAtas.getJSONObject(i).getString(ServerConstants.textTebakan));
                            tebakanObject.setUrlGambarTebakan(jsonArrayAtas.getJSONObject(i).getString(ServerConstants.gambarTebakan));
                            tempList.add(tebakanObject);
//                            tebakanObjects.add(tebakanObject);
                            notifyDataSetChanged();
                        }

                        tempList.addAll(tebakanObjects);
                        tebakanObjects.clear();
                        tebakanObjects.addAll(tempList);
                        notifyDataSetChanged();
                        break;
                    case ServerConstants.addITebakanDown :
                        JSONArray jsonArrayBawah = jsonObject.getJSONArray(ServerConstants.dataListTebakan);
                        for(int i=0;i<jsonArrayBawah.length();i++){
                            TebakanObject tebakanObject = new TebakanObject();
                            tebakanObject.set_idTebakan(jsonArrayBawah.getJSONObject(i).getString(ServerConstants._idTebakan));
                            tebakanObject.setTextTebakan(jsonArrayBawah.getJSONObject(i).getString(ServerConstants.textTebakan));
                            tebakanObject.setUrlGambarTebakan(jsonArrayBawah.getJSONObject(i).getString(ServerConstants.gambarTebakan));

                            tebakanObjects.add(tebakanObject);
                            notifyDataSetChanged();
                        }
                        break;
                    default:
                        break;
                }

            }else{
                Log.d("Error", "Load Data Error");
//                TebakanObject tebakanObject = new TebakanObject();
//                tebakanObjects.lastIndexOf(tebakanObject);
//                tebakanObjects.size();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static class viewHoler{
        NetworkImageView GambarTebakan;
        AppCompatTextView TextTebakan;
        TextView _idTebakan;
    }
}
