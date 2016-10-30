package poly.fall16.pro2051.group8.raovat.utils;

import android.content.Context;
import android.util.Log;

/**
 * Created by Giang Long on 10/29/2016.
 */

public class MyMethod {
    public static void Log(Context c, String msg){
        Log.d(c.getClass().getSimpleName(), msg);
    }
}
