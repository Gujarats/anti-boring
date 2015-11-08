package santana.tebaktebakan.object;

/**
 * Created by Gujarat Santana on 08/11/15.
 */
public class TebakanGambarObject {
    private String gambarUrl;
    private String jawaban;
    private String level;

    public TebakanGambarObject(){}

    public TebakanGambarObject(String gambarUrl, String jawaban, String level) {
        this.gambarUrl = gambarUrl;
        this.jawaban = jawaban;
        this.level = level;
    }

    public String getGambarUrl() {
        return gambarUrl;
    }

    public void setGambarUrl(String gambarUrl) {
        this.gambarUrl = gambarUrl;
    }

    public String getJawaban() {
        return jawaban;
    }

    public void setJawaban(String jawaban) {
        this.jawaban = jawaban;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
