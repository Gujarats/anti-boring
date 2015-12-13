package santana.tebaktebakan.model.object;

/**
 * Created by Gujarat Santana on 13/12/15.
 */
public class TebakanKataObject {
    private String tebakKata;
    private String jawabanTebakKata;

    public TebakanKataObject(String tebakKata, String jawabanTebakKata) {
        this.tebakKata = tebakKata;
        this.jawabanTebakKata = jawabanTebakKata;
    }

    public TebakanKataObject(){}

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
