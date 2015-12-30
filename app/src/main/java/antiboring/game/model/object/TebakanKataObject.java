package antiboring.game.model.object;

/**
 * Created by Gujarat Santana on 13/12/15.
 */
public class TebakanKataObject {
    private String tebakKata;
    private String jawabanTebakKata;
    private int level;

    public TebakanKataObject(String tebakKata, String jawabanTebakKata, int level) {
        this.tebakKata = tebakKata;
        this.jawabanTebakKata = jawabanTebakKata;
        this.level= level;
    }

    public TebakanKataObject(){}


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
