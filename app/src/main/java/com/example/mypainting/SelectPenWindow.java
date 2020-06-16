package com.example.mypainting;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class SelectPenWindow extends PopupWindow implements View.OnClickListener {

    private View view;
    private ImageView mPaintWidthSmall;
    private ImageView mPaintWidthMiddle;
    private ImageView mPaintWidthLarge;

    private IPaintPenListner paintPenListener;

    public void  setPaintPenListener (IPaintPenListner paintPenListener){
        this.paintPenListener=paintPenListener;
    }

    public SelectPenWindow(Context context){
        super(context);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.popwindow_pen,null);

        mPaintWidthMiddle = (ImageView) view.findViewById(R.id.paint_width_middle);
        mPaintWidthSmall = (ImageView) view.findViewById(R.id.paint_width_small);
        mPaintWidthLarge = (ImageView) view.findViewById(R.id.paint_width_big);

        mPaintWidthLarge.setOnClickListener(this);
        mPaintWidthSmall.setOnClickListener(this);
        mPaintWidthMiddle.setOnClickListener(this);

        setContentView(view);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable((0x000000)));
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
       int id=v.getId();

       switch (id){
           case R.id.paint_width_small:
           if (null!=paintPenListener){
               paintPenListener.onPaintWidthChanged(DrawStrokeEnum.LEVEL1);
           }
               mPaintWidthSmall.setBackgroundResource(R.drawable.shape_toobar_select_bg);
               mPaintWidthMiddle.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
               mPaintWidthLarge.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
               dismiss();
               break;
           case R.id.paint_width_middle:
               if (null!=paintPenListener){
                   paintPenListener.onPaintWidthChanged(DrawStrokeEnum.LEVEL2);
               }
               mPaintWidthSmall.setBackgroundResource(R.drawable.shape_toobar_select_bg);
               mPaintWidthMiddle.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
               mPaintWidthLarge.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
               dismiss();
               break;
           case R.id.paint_width_big:
               if (null!=paintPenListener){
                   paintPenListener.onPaintWidthChanged(DrawStrokeEnum.LEVEL3);
               }
               mPaintWidthSmall.setBackgroundResource(R.drawable.shape_toobar_select_bg);
               mPaintWidthMiddle.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
               mPaintWidthLarge.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
               dismiss();
               break;
       }
    }

    public void showPopupWindow(View parent) {
        if (!isShowing()) {
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            showAtLocation(parent, Gravity.NO_GRAVITY, location[0] + parent.getWidth(), location[1] - 10);
        } else {
            dismiss();
        }
    }

}
