//package santana.tebaktebakan.imageManager;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.HttpHeaderParser;
//
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by Gujarat Santana on 18/06/15.
// */
//public class PhotoMultipartRequest extends Request<T> {
//
//
//private static final String FILE_PART_NAME = "file";
//
//private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
//private final Response.Listener<T> mListener;
//private final File mImageFile;
//protected Map<String, String> headers;
//
//public PhotoMultipartRequest(String url, Response.ErrorListener errorListener, Listener<T> listener, File imageFile){
//        super(Request.Method.POST, url, errorListener);
//        mListener = listener;
//        mImageFile = imageFile;
//        buildMultipartEntity();
//        }
//
//@Override
//public Map<String, String> getHeaders() throws AuthFailureError {
//        Map<String, String> headers = super.getHeaders();
//
//        if (headers == null
//        || headers.equals(Collections.emptyMap())) {
//        headers = new HashMap<String, String>();
//        }
//
//        headers.put("Accept", "application/json");
//
//        return headers;
//        }
//
//private void buildMultipartEntity(){
//        mBuilder.addBinaryBody(FILE_PART_NAME, mImageFile, ContentType.create("image/jpeg"), mImageFile.getName());
//        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
//        }
//
//@Override
//public String getBodyContentType(){
//        String contentTypeHeader = mBuilder.build().getContentType().getValue();
//        return contentTypeHeader;
//        }
//
//@Override
//public byte[] getBody() throws AuthFailureError {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        try {
//        mBuilder.build().writeTo(bos);
//        } catch (IOException e) {
//        VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
//        }
//
//        return bos.toByteArray();
//        }
//
//    @Override
//    protected Response<T> parseNetworkResponse(NetworkResponse response) {
//        return null;
//    }
//
//    @Override
//protected Response<T> parseNetworkResponse(NetworkResponse response) {
//        T result = null;
//        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
//        }
//
//@Override
//protected void deliverResponse(T response) {
//        mListener.onResponse(response);
//        }
//        }
