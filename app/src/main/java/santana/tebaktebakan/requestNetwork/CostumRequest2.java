package santana.tebaktebakan.requestNetwork;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import santana.tebaktebakan.common.ServerConstants;

/**
 * Created by Gujarat Santana on 19/06/15.
 */
public class CostumRequest2 extends Request {


    private Response.Listener listener;
    private Response.ErrorListener errorListener;
    private Map<String, String> params;

    //encoded image
    private MultipartEntityBuilder mBuilder;
    private File mImageFile;
    private String fileName;

    //encode image with byte




    public CostumRequest2(int method, String url, Map<String, String> mParams, File mImageFile,String fileName,Response.Listener<String> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.errorListener = errorListener;
        this.params = mParams;
        this.mImageFile = mImageFile;
        this.fileName = fileName;
        buildMultipartEntity();
    }

    private void buildMultipartEntity(){
//        mBuilder.addBinaryBody(FILE_PART_NAME, mImageFile, ContentType.create("image/jpeg"), mImageFile.getName());
        mBuilder = MultipartEntityBuilder.create();
//        mBuilder.addBinaryBody(ServerConstants.mParamsBinaryGambarTebakan, imageByte, ContentType.APPLICATION_FORM_URLENCODED, "imageTest");
        mBuilder.addBinaryBody(ServerConstants.mParamsGambarTebakan, mImageFile, ContentType.APPLICATION_FORM_URLENCODED,fileName);

        mBuilder.addTextBody(ServerConstants.mParamsGambarTebakanUrl, ServerConstants.getGambarTebakan+fileName , ContentType.APPLICATION_FORM_URLENCODED);
        mBuilder.addTextBody(ServerConstants.mParamsToken, params.get(ServerConstants.mParamsToken),ContentType.APPLICATION_FORM_URLENCODED);
        mBuilder.addTextBody(ServerConstants.mParams_idUser, params.get(ServerConstants.mParams_idUser),ContentType.APPLICATION_FORM_URLENCODED);
        mBuilder.addTextBody(ServerConstants.mParamsKunciTebakan, params.get(ServerConstants.mParamsKunciTebakan),ContentType.APPLICATION_FORM_URLENCODED);
        mBuilder.addTextBody(ServerConstants.mParamsTextTebakan, params.get(ServerConstants.mParamsTextTebakan),ContentType.APPLICATION_FORM_URLENCODED);
        mBuilder.addTextBody(ServerConstants.mParamsGcmID, params.get(ServerConstants.mParamsGcmID),ContentType.APPLICATION_FORM_URLENCODED);
//        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));

    }

    @Override
    public String getBodyContentType(){
        String contentTypeHeader = mBuilder.build().getContentType().getValue();
        return contentTypeHeader;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mBuilder.build().writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
        }
        return bos.toByteArray();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String,String>();
        }

        headers.put("Accept", "application/json");

        return headers;
    }

    @Override
    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new String(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override
    protected void deliverResponse(Object response) {
        listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        errorListener.onErrorResponse(error);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
