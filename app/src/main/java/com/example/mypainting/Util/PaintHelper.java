package com.example.mypainting.Util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class PaintHelper {
    private static final String TAG ="PaintHelper";
    public static Bitmap convertToBlackWhite(Bitmap bm) {
        int width=1280;
        int height=960;//2倍
//        int widtho = bm.getWidth();//原图像宽度
//        Log.i(TAG,"原图像宽度"+widtho);
//        int heighto = bm.getHeight();//原图高度
//        Log.i(TAG,"原图像高度"+heighto);
        int color;//用来存储某个像素点的颜色值h
        int r, g, b, a;
        int digit = 100;//阈值
        //创建空白图像，宽度等于原图宽度，高度等于原图高度，用ARGB_8888渲染
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];
        bm.getPixels(oldPx, 0, width, 0, 0, width, height);
        for (int j = 0; j < width * height; j++) {
            color = oldPx[j];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);
            //此公式将r,g,b运算获得灰度值
            int gray = (int) ((float) r * 0.3 + (float) g * 0.59 + (float) b * 0.11);

            if (gray < digit) {
                gray = 255;
            } else {
                gray = 0;
            }
            newPx[j] = Color.argb(a, gray, gray, gray);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }
}
