package antiboring.game.model.object;

/**
 * Created by Gujarat Santana on 26/12/15.
 */
public class MSettings {
    private int sourceImage;
    private String textSettings;

    public MSettings(int sourceImage, String textSettings) {
        this.sourceImage = sourceImage;
        this.textSettings = textSettings;
    }

    public MSettings(){}

    public int getSourceImage() {
        return sourceImage;
    }

    public void setSourceImage(int sourceImage) {
        this.sourceImage = sourceImage;
    }

    public String getTextSettings() {
        return textSettings;
    }

    public void setTextSettings(String textSettings) {
        this.textSettings = textSettings;
    }
}
