package santana.tebaktebakan.requestNetwork;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Gujarat Santana on 17/05/15.
 */
public class CostumRequestString extends Request<String> {


    private Response.Listener<String> listener;
    private Response.ErrorListener errorListener;
    private Map<String, String> params;


    public CostumRequestString(int method, String url, Map<String, String> mParams, Response.Listener<String> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.errorListener = errorListener;
        this.params = mParams;
    }

    public CostumRequestString(int method, String url, Response.Listener<String> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.errorListener = errorListener;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
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
    protected void deliverResponse(String response) {
        listener.onResponse(response);

    }

    @Override
    public void deliverError(VolleyError error) {
        errorListener.onErrorResponse(error);
    }
}
