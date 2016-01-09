package antiboring.game.controller.appBilling;

/**
 * Created by Gujarat Santana on 09/01/16.
 */
public class ItemForSaleObject {
    private String name;
    private String price;
    private boolean isAvailable;

    public ItemForSaleObject(){}

    public ItemForSaleObject(String name, String price, boolean isAvailable) {
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
