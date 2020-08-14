package com.example.instagram;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.jetbrains.annotations.NotNull;

public class Utils {

    public static int getScreenHeight(Activity context) {
        DisplayMetrics outMetrics = getDisplayMetrics(context);
        return outMetrics.heightPixels;
    }

    public static int getScreenWidth(Activity context) {
        DisplayMetrics outMetrics = getDisplayMetrics(context);
        return outMetrics.widthPixels;
    }

    @NotNull
    public static DisplayMetrics getDisplayMetrics(Activity context) {
        WindowManager manager = context.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public static int dp2px(Context ctx, float dp) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
