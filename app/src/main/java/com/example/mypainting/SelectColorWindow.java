package com.example.mypainting;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class SelectColorWindow extends PopupWindow implements View.OnClickListener{
    private static final String TAG="SelectColorWindow";

    private View view;

    private ImageView mPaintBlack;
    private ImageView mPaintBlue;
    private ImageView mPaintYellow;
    private ImageView mPaintRed;

    private IPaintColorListener paintColorListener;
    public void setIPaintColorListener(IPaintColorListener paintColorListener){
        this.paintColorListener=paintColorListener;
    }

    public SelectColorWindow(Context context){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popwindow_color, null);

        mPaintRed = (ImageView) view.findViewById(R.id.red);
        mPaintBlue = (ImageView) view.findViewById(R.id.blue);
        mPaintYellow = (ImageView) view.findViewById(R.id.yellow);
        mPaintBlack = (ImageView) view.findViewById(R.id.black);

        mPaintBlack.setOnClickListener(this);
        mPaintBlue.setOnClickListener(this);
        mPaintRed.setOnClickListener(this);
        mPaintYellow.setOnClickListener(this);

        setContentView(view);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0x000000));
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.black:
                if(paintColorListener!=null) paintColorListener.onColorChanged(Color.BLACK);
                Log.i(TAG,"选择黑色");
                mPaintBlack.setBackgroundResource(R.drawable.shape_toobar_select_bg);
                mPaintYellow.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
                mPaintBlue.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
                mPaintRed.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
                dismiss();
                break;
            case R.id.blue:
                if(paintColorListener!=null) paintColorListener.onColorChanged(Color.BLUE);
                Log.i(TAG,"选择蓝色");
                mPaintBlue.setBackgroundResource(R.drawable.shape_toobar_select_bg);
                mPaintYellow.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
                mPaintBlack.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
                mPaintRed.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
                dismiss();
                break;
            case R.id.red:
                if(paintColorListener!=null) paintColorListener.onColorChanged(Color.RED);
                Log.i(TAG,"选择红色");
                mPaintRed.setBackgroundResource(R.drawable.shape_toobar_select_bg);
                mPaintYellow.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
                mPaintBlack.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
                mPaintBlue.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
                dismiss();
                break;
            case R.id.yellow:
                if(paintColorListener!=null) paintColorListener.onColorChanged(Color.YELLOW);
                Log.i(TAG,"选择黄色");
                mPaintYellow.setBackgroundResource(R.drawable.shape_toobar_select_bg);
                mPaintBlue.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
                mPaintBlack.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
                mPaintRed.setBackgroundResource(R.drawable.shape_toobar_unselect_bg);
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
