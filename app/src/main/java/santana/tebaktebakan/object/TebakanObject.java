package santana.tebaktebakan.object;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class TebakanObject {
    private String _idTebakan;
    private String UrlGambarTebakan;
    private String TextTebakan;

    public TebakanObject(){}

    public TebakanObject(String _idTebakan, String urlGambarTebakan, String textTebakan) {
        this._idTebakan = _idTebakan;
        UrlGambarTebakan = urlGambarTebakan;
        TextTebakan = textTebakan;
    }

    public String get_idTebakan() {
        return _idTebakan;
    }

    public void set_idTebakan(String _idTebakan) {
        this._idTebakan = _idTebakan;
    }

    public String getUrlGambarTebakan() {
        return UrlGambarTebakan;
    }

    public void setUrlGambarTebakan(String urlGambarTebakan) {
        UrlGambarTebakan = urlGambarTebakan;
    }

    public String getTextTebakan() {
        return TextTebakan;
    }

    public void setTextTebakan(String textTebakan) {
        TextTebakan = textTebakan;
    }
}
