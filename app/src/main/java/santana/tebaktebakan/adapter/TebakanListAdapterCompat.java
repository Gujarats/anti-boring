package santana.tebaktebakan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import santana.tebaktebakan.AppController;
import santana.tebaktebakan.R;
import santana.tebaktebakan.activity.AnswerTebakanActivity;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.common.ServerConstants;
import santana.tebaktebakan.object.TebakanObject;
import santana.tebaktebakan.requestNetwork.CostumRequestString;
import santana.tebaktebakan.session.SessionManager;

/**
 * Created by Gujarat Santana on 01/07/15.
 */
public class TebakanListAdapterCompat extends RecyclerView.Adapter<TebakanListAdapterCompat.ViewHolder> implements Response.Listener<String>,Response.ErrorListener{
    private List<TebakanObject> tebakanObjects;
    private Context context;
    private Activity activity;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private SessionManager sessionManager;

    public TebakanListAdapterCompat(Context context, Activity activity){
        sessionManager = new SessionManager(context);
        this.context  = context;
        this.activity = activity;
        tebakanObjects = new ArrayList<TebakanObject>();
        Map<String,String> mParams = new HashMap<String,String>();
        mParams.put(ServerConstants.mParams_idUser,sessionManager.getUidUser());
        mParams.put(ServerConstants.mParamsToken,sessionManager.getToken());

        CostumRequestString myReq = new CostumRequestString(Request.Method.POST,ServerConstants.ShowAllTebakan,mParams,TebakanListAdapterCompat.this,TebakanListAdapterCompat.this);
        myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myReq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error", error.getMessage());
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("response",response);
            if(jsonObject.getString(ServerConstants.statusBeckend).equalsIgnoreCase(ServerConstants.statusBeckendOk)){
                switch (jsonObject.getInt(ServerConstants.resultType)){
                    case ServerConstants.showTebakanResult:
                        JSONArray jsonResult = jsonObject.getJSONArray(ServerConstants.dataListTebakan);
                        for(int i=0;i<jsonResult.length();i++){
                            TebakanObject tebakanObject = new TebakanObject();
                            tebakanObject.set_idTebakan(jsonResult.getJSONObject(i).getString(ServerConstants._idTebakan));
                            tebakanObject.setKunciTebakan(jsonResult.getJSONObject(i).getString(ServerConstants.mParamsKunciTebakan));
                            tebakanObject.setTextTebakan(jsonResult.getJSONObject(i).getString(ServerConstants.textTebakan));
                            tebakanObject.setUrlGambarTebakan(jsonResult.getJSONObject(i).getString(ServerConstants.gambarTebakan));
                            tebakanObject.set_idUser(jsonResult.getJSONObject(i).getString(ServerConstants._idUser));
                            tebakanObject.setGcmID(jsonResult.getJSONObject(i).getString(ServerConstants.gcmID));
                            tebakanObjects.add(tebakanObject);
                            notifyDataSetChanged();
                        }
                        break;
                    case ServerConstants.addTebakanView :
                        JSONArray jsonArray = jsonObject.getJSONArray(ServerConstants.dataListTebakan);
                        for(int i=0;i<jsonArray.length();i++){
                            TebakanObject tebakanObject = new TebakanObject();
                            tebakanObject.set_idUser(jsonArray.getJSONObject(i).getString(ServerConstants._idUser));
                            tebakanObject.setGcmID(jsonArray.getJSONObject(i).getString(ServerConstants.gcmID));
                            tebakanObject.set_idTebakan(jsonArray.getJSONObject(i).getString(ServerConstants._idTebakan));
                            tebakanObject.setKunciTebakan(jsonArray.getJSONObject(i).getString(ServerConstants.mParamsKunciTebakan));
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
                            tebakanObject.set_idUser(jsonArrayAtas.getJSONObject(i).getString(ServerConstants._idUser));
                            tebakanObject.setGcmID(jsonArrayAtas.getJSONObject(i).getString(ServerConstants.gcmID));
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
                            tebakanObject.set_idUser(jsonArrayBawah.getJSONObject(i).getString(ServerConstants._idUser));
                            tebakanObject.setGcmID(jsonArrayBawah.getJSONObject(i).getString(ServerConstants.gcmID));
                            tebakanObjects.add(tebakanObject);
                            notifyDataSetChanged();
                        }
                        break;
                    default:
                        break;
                }

            }else{
                Log.d("Error", "Load Data Error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_tebakan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder._idTebakan.setText(tebakanObjects.get(position).get_idTebakan());
        holder.TextTebakan.setText(tebakanObjects.get(position).getTextTebakan());
        holder.kunciTebakan.setText(tebakanObjects.get(position).getKunciTebakan());
        holder.gambarUrl.setText(tebakanObjects.get(position).getUrlGambarTebakan());
        holder._idUser.setText(tebakanObjects.get(position).get_idUser());
        holder.gcmID.setText(tebakanObjects.get(position).getGcmID());
        //load image
        if(tebakanObjects.get(position).getUrlGambarTebakan().equalsIgnoreCase(ApplicationConstants.ImageVisibiliy)){
            holder.GambarTebakan.setVisibility(View.GONE);
        }else{
            holder.GambarTebakan.setImageUrl(tebakanObjects.get(position).getUrlGambarTebakan(), imageLoader);
            holder.GambarTebakan.setVisibility(View.VISIBLE);

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click", "di cclike");
                Context context = view.getContext();
                TextView _idUser = (TextView) view.findViewById(R.id._idUser);
                TextView _idTebakan = (TextView) view.findViewById(R.id._idTebakan);
                TextView gcmID = (TextView) view.findViewById(R.id.gcmID);
                TextView kunciTebakan = (TextView) view.findViewById(R.id.kunciTebakan);
                AppCompatTextView textTebakan = (AppCompatTextView) view.findViewById(R.id.TextTebakan);
                TextView gambarUrl = (TextView) view.findViewById(R.id.gambarUrl);

                String _idUserString = _idUser.getText().toString();
                String _idTebakanString = _idTebakan.getText().toString();
                String gcmIDString = gcmID.getText().toString();
                String kunciTebakanString = kunciTebakan.getText().toString();
                String textTebakanString = textTebakan.getText().toString();
                String gambarUrlString = gambarUrl.getText().toString();

                if (
                        !_idUserString.trim().isEmpty() &&
                                !_idTebakanString.trim().isEmpty() &&
                                !gcmIDString.trim().isEmpty() &&
                                !kunciTebakanString.trim().isEmpty() &&
                                !gambarUrlString.trim().isEmpty() &&
                                !textTebakanString.trim().isEmpty()
                        ) {
                    Log.d("Click", "masuk");
                    Intent intent = new Intent(context, AnswerTebakanActivity.class);
                    intent.putExtra(ApplicationConstants.FromActivity, ApplicationConstants.TebakanListActivity);
                    intent.putExtra(ApplicationConstants._idUserTebakan, _idUserString);
                    intent.putExtra(ApplicationConstants._idTebakan, _idTebakanString);
                    intent.putExtra(ApplicationConstants.gcmID, gcmIDString);
                    intent.putExtra(ApplicationConstants.kunciTebakan, kunciTebakanString);
                    intent.putExtra(ApplicationConstants.imageUrl, gambarUrlString);
                    intent.putExtra(ApplicationConstants.textTebakan, textTebakanString);
                    context.startActivity(intent);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return tebakanObjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final NetworkImageView GambarTebakan;
        public final AppCompatTextView TextTebakan;
        public final TextView _idTebakan;
        public final TextView gambarUrl;
        public final TextView _idUser;
        public final TextView gcmID;
        public final TextView kunciTebakan;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            _idTebakan = (TextView) view.findViewById(R.id._idTebakan);
            TextTebakan = (AppCompatTextView) view.findViewById(R.id.TextTebakan);
            GambarTebakan = (NetworkImageView) view.findViewById(R.id.GambarTebakan);
            kunciTebakan = (TextView) view.findViewById(R.id.kunciTebakan);
            _idUser = (TextView) view.findViewById(R.id._idUser);
            gcmID = (TextView) view.findViewById(R.id.gcmID);
            gambarUrl = (TextView) view.findViewById(R.id.gambarUrl);
        }

    }
}
