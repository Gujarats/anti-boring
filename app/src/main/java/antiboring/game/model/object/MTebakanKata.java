package antiboring.game.model.object;

/**
 * Created by Gujarat Santana on 13/12/15.
 */
public class MTebakanKata {
    private String tebakKata;
    private String jawabanTebakKata;
    private int level;

    public MTebakanKata(String tebakKata, String jawabanTebakKata, int level) {
        this.tebakKata = tebakKata;
        this.jawabanTebakKata = jawabanTebakKata;
        this.level= level;
    }

    public MTebakanKata(){}


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTebakKata() {
        return tebakKata;
    }

    public void setTebakKata(String tebakKata) {
        this.tebakKata = tebakKata;
    }

    public String getJawabanTebakKata() {
        return jawabanTebakKata;
    }

    public void setJawabanTebakKata(String jawabanTebakKata) {
        this.jawabanTebakKata = jawabanTebakKata;
    }
}
