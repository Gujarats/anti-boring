package antiboring.game.model.object;

/**
 * Created by Gujarat Santana on 26/12/15.
 */
public class SettingsObject {
    private int sourceImage;
    private String textSettings;

    public SettingsObject(int sourceImage, String textSettings) {
        this.sourceImage = sourceImage;
        this.textSettings = textSettings;
    }

    public SettingsObject(){}

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
