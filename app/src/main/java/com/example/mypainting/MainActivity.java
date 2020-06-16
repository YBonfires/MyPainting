package com.example.mypainting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import zhanglei.com.paintview.DrawTypeEnum;
import zhanglei.com.paintview.PaintView;

public class MainActivity extends AppCompatActivity implements IPaintColorListener,IPaintPenListner{
    private static final String TAG="MainActivity";
    //定义布局元素
    private PaintView paintView;
    private ImageView ivUndo;
    private ImageView ivRedo;
    private ImageView logo;
    private TextView title;
    private Button back,pause,clear;
   // private Button game_start;
    private TextView resultmsg;

    private SelectPenWindow selectPenWindow;
    private SelectColorWindow selectColorWindow;

    //dialog
    private MyDialog PauseDialog;

    //计数器 全局
    private Chronometer ch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //实例化
        logo = findViewById(R.id.logo);
        paintView = findViewById(R.id.paintView);
        ivRedo = findViewById(R.id.iv_redo);
        ivUndo = findViewById(R.id.iv_undo);
        title = findViewById(R.id.title);
        //  game_start = findViewById(R.id.game_start);
        back = findViewById(R.id.back);
        pause = findViewById(R.id.pause);
        resultmsg = findViewById(R.id.resultmsg);

        PauseDialog = new MyDialog(this, R.layout.layout, new int[]{R.id.textView, R.id.button});

        selectPenWindow = new SelectPenWindow(this);
        selectPenWindow.setPaintPenListener(this);

        selectColorWindow = new SelectColorWindow(this);
        selectColorWindow.setIPaintColorListener(this);

//        //开始游戏
//         game_start.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//
//             }
//         });

        //启动画笔功能
        paintView.setDrawType(DrawTypeEnum.PEN);
        //点击橡皮擦
        findViewById(R.id.btn_select_eraser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.setDrawType(DrawTypeEnum.ERASER);
            }
        });

        //点击清空面板
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.clear();
            }
        });

        //点击上一步
        ivUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.undo();
            }
        });
        //点击下一步
        ivRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.redo();
            }
        });
        //Undo,Redo状态改变监听器
        paintView.setOnReDoUnDoStatusChangedListener(new PaintView.OnReDoUnDoStatusChangedListener() {
            @Override
            public void onReDoUnDoStatusChanged(boolean canReDo, boolean canUnDo) {
                if (canReDo) {
                    ivRedo.setImageResource(R.mipmap.icon_redo);
                } else {
                    ivRedo.setImageResource(R.mipmap.icon_redo_gray);
                }
                if (canUnDo) {
                    ivUndo.setImageResource(R.mipmap.icon_undo);
                } else {
                    ivUndo.setImageResource(R.mipmap.icon_undo_gray);
                }
            }
        });
        //点击画笔
        findViewById(R.id.btn_select_paint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.setDrawType(DrawTypeEnum.PEN);
                selectPenWindow.showPopupWindow(v);
            }
        });
        //点击颜色
        findViewById(R.id.btn_select_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColorWindow.showPopupWindow(v);
            }
        });
        //点击返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackAlertDialog dialog=BackAlertDialog.getInstance();
                dialog.showConfirmDialog(MainActivity.this,"现在退出不会保存记录哦\n确定退出吗?");
                dialog.setOnButtonClickListener(new BackAlertDialog.OnButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(AlertDialog dialog) {
                        dialog.dismiss();
                        Log.i(TAG,"点击了确定按钮，下一步应该跳转至主菜单");
                    }

                    @Override
                    public void onNegativeButtonClick(AlertDialog dialog) {
                        dialog.dismiss();
                        Log.i(TAG,"取消，返回当前界面");
                    }
                });
            }
        });
        //点击暂停
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogUtils utils=AlertDialogUtils.getInstance();
                utils.showConfirmDialog(MainActivity.this,"游戏暂停");
                utils.setOnButtonClickListener(new AlertDialogUtils.OnButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(AlertDialog dialog) {
                        //
                    }
                    @Override
                    public void onNegativeButtonClick(AlertDialog dialog) {
                        dialog.dismiss();
                        Log.i(TAG,"取消，返回当前界面");
                    }
                });
            }
        });
    }

    @Override
    public void onColorChanged(int paintColor) {
        paintView.setPaintColor(paintColor);
    }

    @Override
    public void onPaintWidthChanged(DrawStrokeEnum drawStrokeEnum) {
        paintView.setPaintWidth(drawStrokeEnum.getPenStroke());
        paintView.setRushPaintWidth(drawStrokeEnum.getEraserStroke());
    }

    //回收资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != paintView) {
            paintView.destroy();
        }
    }}

