package cxm.example.kurento.util;

import android.content.Context;

/**
 * Created by xiemchen on 5/2/17.
 */

public class Pixel {
    public static int dp2px(float dp, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }
}
