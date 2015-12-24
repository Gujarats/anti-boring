package santana.tebaktebakan.model.object;

/**
 * Created by Gujarat Santana on 24/12/15.
 */
public class LevelTebakanObject {
    private int stars;
    private int level;
    private  boolean isLocked;


    public LevelTebakanObject(int stars, int level, boolean isLocked) {
        this.stars = stars;
        this.level = level;
        this.isLocked = isLocked;
    }
    public LevelTebakanObject(){}


    public boolean isLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
