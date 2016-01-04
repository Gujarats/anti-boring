package antiboring.game.view.font;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Gujarat Santana on 04/01/16.
 */
public class RobotoFont extends AppCompatTextView {

    private static Typeface typeface;
    public RobotoFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setRobotoTypeface();
    }

    public RobotoFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRobotoTypeface();
    }

    public RobotoFont(Context context) {
        super(context);
        setRobotoTypeface();
    }

    private void setRobotoTypeface()  {
        try {
            if (typeface == null) {
                typeface = Typeface.createFromAsset(this.getContext().getAssets(), "roboto_regular.ttf");
            }
            this.setTypeface(typeface);
        }
        catch (Exception e) {
            Log.d("FONT", "FONT IS NOT FOUND. USE DEFAULT FONT INSTEAD");
        }

    }


}
