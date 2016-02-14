package antiboring.game.model.object;

/**
 * Created by Gujarat Santana on 08/11/15.
 */
public class MTebakanGambar {
    private String gambarUrl;
    private String jawaban;
    private int level;

    public MTebakanGambar(){}

    public MTebakanGambar(String gambarUrl, String jawaban, int level) {
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
