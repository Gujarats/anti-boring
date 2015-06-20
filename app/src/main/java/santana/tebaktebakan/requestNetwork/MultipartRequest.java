//package santana.tebaktebakan.requestNetwork;
//
//import android.util.Log;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.ParseError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.HttpHeaderParser;
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.Map;
//
///**
// * Created by Gujarat Santana on 19/06/15.
// */
//public class MultipartRequest extends Request {
//    private static final String KEY_PICTURE = "picture";
//    private static final String TAG = "MutlipartRequest";
//
//    private HttpEntity mHttpEntity;
//    private final Class mClass;
//    private Response.Listener mListener;
//    private Map<String, String> mHeaders;
//    private final JSONObject gson;
//
//    public MultipartRequest(String url, File file,
//                            Class clazz,
//                            Map<String, String> headers,
//                            Response.Listener listener,
//                            Response.ErrorListener errorListener) {
//        super(Request.Method.POST, url, errorListener);
//        mHeaders = headers;
//        mClass = clazz;
//        mListener = listener;
//        gson = new JSONObject();
//        mHttpEntity = buildMultipartEntity(file);
//    }
//
//    public MultipartRequest(String url, String path,
//                            Class clazz,
//                            Map<String, String> headers,
//                            Response.Listener listener,
//                            Response.ErrorListener errorListener) {
//        super(Request.Method.POST, url, errorListener);
//        mHeaders = headers;
//        mClass = clazz;
//        mListener = listener;
//        gson = new JSONObject();
//        mHttpEntity = buildMultipartEntity(path);
//    }
//
//    private HttpEntity buildMultipartEntity(String path) {
//        File file = new File(path);
//        return buildMultipartEntity(file);
//    }
//
//    private HttpEntity buildMultipartEntity(File file) {
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        String fileName = file.getName();
//        builder.addBinaryBody(KEY_PICTURE, file, ContentType.create("image/jpeg"), fileName);
//        return builder.build();
//    }
//
//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        return mHeaders != null ? mHeaders : super.getHeaders();
//    }
//
//    @Override
//    public String getBodyContentType() {
//        return mHttpEntity.getContentType().getValue();
//    }
//
//    @Override
//    public byte[] getBody() throws AuthFailureError {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        try {
//            mHttpEntity.writeTo(bos);
//        } catch (IOException e) {
//            VolleyLog.e("IOException writing to ByteArrayOutputStream");
//        }
//        return bos.toByteArray();
//    }
//
//    @Override
//    protected Response parseNetworkResponse(NetworkResponse response) {
//        String json;
//        try {
//            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//        } catch (UnsupportedEncodingException e) {
//            Log.e(TAG, String.format("Encoding problem parsing API response. NetworkResponse:%s", response.toString()), e);
//            return Response.error(new ParseError(e));
//        }
//        try {
//            return Response.success(gson.fromJson(json, mClass), HttpHeaderParser.parseCacheHeaders(response));
//        } catch (JsonSyntaxException e) {
//            Log.e(TAG, String.format("Couldn't API parse JSON response. NetworkResponse:%s", response.toString()), e);
//            Log.e(TAG, String.format("Couldn't API parse JSON response. Json dump: %s", json));
//            return Response.error(new ParseError(e));
//        }
//    }
//
//    @Override
//    protected void deliverResponse(Object response) {
//
//    }
//
//
//    @Override
//    public int compareTo(Object o) {
//        return 0;
//    }
//}
