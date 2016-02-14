package antiboring.game.model.object;

/**
 * Created by Gujarat Santana on 01/01/16.
 */
public class MBuyCoins {
    private String title;
    private String description;
    private String price;
    private int icon;

    public MBuyCoins(String title, String price, int icon, String desc) {
        this.title = title;
        this.price = price;
        this.icon = icon;
        this.description = desc;
    }

    public MBuyCoins(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
