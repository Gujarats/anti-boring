//package santana.tebaktebakan.imageManager;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//
//import org.json.JSONObject;
//
//import java.io.File;
//
///**
// * Created by Gujarat Santana on 18/06/15.
// */
//public class JSONRequestResponse {
//    public JSONRequestResponse(Context cntx) {
//        mContext = cntx;
//    }
//
//    private final Context mContext;
//    private int reqCode;
//    private IParseListener listner;
//
//    private boolean isFile = false;
//    private String file_path = "", key = "";
//
//    public void getResponse(String url, final int requestCode,
//                            IParseListener mParseListener) {
//        getResponse(url, requestCode, mParseListener, null);
//    }
//
//    public void getResponse(String url, final int requestCode,
//                            IParseListener mParseListener, Bundle params) {
//        this.listner = mParseListener;
//        this.reqCode = requestCode;
//
//        Response.Listener<JSONObject> sListener = new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                if (listner != null) {
//                    listner.SuccessResponse(response, reqCode);
//                }
//            }
//        };
//
//        Response.ErrorListener eListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (listner != null) {
//                    listner.ErrorResponse(error, reqCode);
//                }
//            }
//        };
//
//        if (!isFile) {
//            JsonObjectRequest jsObjRequest = new JsonObjectRequest(
//                    Request.Method.GET, url, null, sListener,
//
//                    eListener);
//            MyVolley.getRequestQueue().add(jsObjRequest);
//        } else {
//            if (file_path != null) {
//                File mFile = new File(file_path);
//                MultipartRequest multipartRequest =
//                        new MultipartRequest(url,eListener, sListener, key, mFile, params);
//                MyVolley.getRequestQueue().add(multipartRequest);
//            }
//        }
//    }
//
//    public boolean isFile() {
//        return isFile;
//    }
//
//
//    public void setFile(String param, String path) {
//        if (path != null && param != null) {
//            key = param;
//            file_path = path;
//            this.isFile = true;
//        }
//    }
//}
