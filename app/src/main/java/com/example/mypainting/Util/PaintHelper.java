package com.example.mypainting.Util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

public class PaintHelper {
    public static Bitmap convertToBlackWhite(Bitmap bm) {

        int width= 640;
        int height=480;
       // int width = bm.getWidth();//原图像宽度
        //int height = bm.getHeight();//原图高度
        int color;//用来存储某个像素点的颜色值
        int r, g, b, a;//红，绿，蓝，透明度
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

            if (gray < digit) {//如果某点像素灰度小于给定阈值
                gray = 255;//将该点像素的灰度值置为0（黑色）
            } else {//如果某点像素灰度大于或等于给定阈值
                gray = 0;//将该点像素的灰度值置为1（白色）
            }
            newPx[j] = Color.argb(a, gray, gray, gray);//将处理后的透明度（没变），r,g,b分量重新合成颜色值并将其存储在数组中
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);//将处理后的像素信息赋给新图
        return bmp;
    }
}
