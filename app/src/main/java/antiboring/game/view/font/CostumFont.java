package antiboring.game.view.font;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

public class CostumFont extends AppCompatTextView{

    private static Typeface typeface;
    public CostumFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setRobotoTypeface();
    }

    public CostumFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRobotoTypeface();
    }

    public CostumFont(Context context) {
        super(context);
        setRobotoTypeface();
    }

    private void setRobotoTypeface()  {
        try {
            if (typeface == null) {
                typeface = Typeface.createFromAsset(this.getContext().getAssets(), "costum_font.ttf");
            }
            this.setTypeface(typeface);
        }
        catch (Exception e) {
            Log.d("FONT", "FONT IS NOT FOUND. USE DEFAULT FONT INSTEAD");
        }

    }


}
