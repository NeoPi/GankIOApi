package com.neopi.gankio.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by neopi on 17-3-1.
 */

public class DeviceUtils {

  public static int SCREEN_WIDTH = 0;

  public static void init(Context context){
    SCREEN_WIDTH = getScreenWidth(context);
  }


  private static int getScreenWidth (Context context){
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display defaultDisplay = manager.getDefaultDisplay();
    DisplayMetrics displayMetrics = new DisplayMetrics();
    defaultDisplay.getMetrics(displayMetrics);
    return displayMetrics.widthPixels;
  }
}
