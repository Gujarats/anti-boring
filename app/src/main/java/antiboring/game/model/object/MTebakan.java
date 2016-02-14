package antiboring.game.model.object;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class MTebakan {
    private String _idTebakan;
    private String _idUser;
    private String UrlGambarTebakan;
    private String TextTebakan;
    private String kunciTebakan;
    private String gcmID;

    public MTebakan(){}

    public MTebakan(String _idTebakan, String _idUser, String urlGambarTebakan, String textTebakan, String kunciTebakan, String gcmID) {
        this._idTebakan = _idTebakan;
        this._idUser = _idUser;
        UrlGambarTebakan = urlGambarTebakan;
        TextTebakan = textTebakan;
        this.kunciTebakan = kunciTebakan;
        this.gcmID = gcmID;
    }

    public String get_idUser() {
        return _idUser;
    }

    public void set_idUser(String _idUser) {
        this._idUser = _idUser;
    }

    public String getGcmID() {
        return gcmID;
    }

    public void setGcmID(String gcmID) {
        this.gcmID = gcmID;
    }

    public String getKunciTebakan() {
        return kunciTebakan;
    }

    public void setKunciTebakan(String kunciTebakan) {
        this.kunciTebakan = kunciTebakan;
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
