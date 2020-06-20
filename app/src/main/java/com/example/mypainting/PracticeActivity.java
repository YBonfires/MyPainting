package com.example.mypainting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mypainting.Util.PaintHelper;
import com.example.mypainting.gson.User;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import zhanglei.com.paintview.DrawTypeEnum;
import zhanglei.com.paintview.PaintView;
import zhanglei.com.paintview.Util;

public class PracticeActivity extends AppCompatActivity implements IPaintColorListener,IPaintPenListner {
    private static final String TAG="PracticeActivity";
    //定义布局元素
    private PaintView paintView;
    private ImageView ivUndo;
    private ImageView ivRedo;
    private ImageView logo;
    private TextView titlemsg;
    private  TextView resultmsg,score;
    private Button back,pause,clear;
    private Button game_start,submit;
    private Button hint;
    private User this_user=new User();
    private SelectPenWindow selectPenWindow;
    private SelectColorWindow selectColorWindow;
    private File file,textfile;

    private Chronometer ch;
    private ArrayList list;
    private int type;
    private String[] Paints={"apple", "book", "bowtie", "candle", "cloud", "cup", "door", "envelope", "eyeglasses", "guitar", "hammer",
            "hat", "ice cream", "leaf", "scissors", "star", "t-shirt", "pants", "lightning", "tree"};
    private int[] hintimage={R.drawable.background, R.drawable.background,R.drawable.background,R.drawable.background,
            R.drawable.background,R.drawable.background,R.drawable.background,R.drawable.background,
            R.drawable.background,R.drawable.background,R.drawable.background,R.drawable.background,
            R.drawable.background,R.drawable.background,R.drawable.background,R.drawable.background,
            R.drawable.background,R.drawable.background,R.drawable.background,R.drawable.background};

    public void initArrayList(){
        list=new ArrayList();
        for(int i=0;i<19;i++){
            HashMap map=new HashMap();
            map.put("Paints",Paints[i]);
            map.put("hintimage",hintimage[i]);
            list.add(map);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        logo = findViewById(R.id.logo);
        paintView = findViewById(R.id.paintView);
        ivRedo = findViewById(R.id.iv_redo);
        ivUndo = findViewById(R.id.iv_undo);
        titlemsg = findViewById(R.id.title);
        game_start=findViewById(R.id.game_start);
        back = findViewById(R.id.back);
        pause = findViewById(R.id.pause);
        titlemsg=findViewById(R.id.title);
        hint=findViewById(R.id.hint);
        resultmsg = findViewById(R.id.resultmsg);
        submit=findViewById(R.id.submit);
        score=findViewById(R.id.score);

        //实例化计时器
        ch=(Chronometer)findViewById(R.id.chronometer);

        selectPenWindow = new SelectPenWindow(this);
        selectPenWindow.setPaintPenListener(this);

        selectColorWindow = new SelectColorWindow(this);
        selectColorWindow.setIPaintColorListener(this);


        game_start.setOnClickListener(new View.OnClickListener() {
//    final OkHttpClient client = new OkHttpClient();
//    @Override

            //关卡计数器

            public void onClick(View v) {

                game_start.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                Log.i(TAG, "点击开始游戏，随机抽取题目...");
                Random random = new Random();
                int i = random.nextInt(19);
                Log.i(TAG, "生成随机题目i为" + i);
                type=i;
                titlemsg.setText("练习模式 请画出" + Paints[i]);
                //设置初始时间
                ch.setBase(SystemClock.elapsedRealtime() + 30000);
                ch.setFormat("%s");
                //倒计时实现
                ch.start();
                ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        ch.setText(ch.getText().toString().substring(1));
                        if (SystemClock.elapsedRealtime()-ch.getBase()>=0)ch.stop();
                    }
                });

                //点击提示按钮
                hint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog=new Dialog(PracticeActivity.this);
                        View view = LayoutInflater.from(PracticeActivity.this).inflate(R.layout.activity_hint_dialog, null);
                        ImageView image=view.findViewById(R.id.hint);
                        image.setImageResource(hintimage[type]);
                        Button ok=view.findViewById(R.id.bt_ok);
                        dialog.setContentView(view);
                        dialog.show();
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });


            }});

        //点击提交按钮
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "提交------------------");
                ch.stop();
                Toast toast = Toast.makeText(getApplicationContext(), "正在提交", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                ch.stop();
                Bitmap bitmap = paintView.getPaintViewScreen(Bitmap.Config.ARGB_8888);
                //File file=new File("/sdcard/akai/");
                file = Util.bitmap2File(PracticeActivity.this, bitmap);
                paintView.clear();
                Log.i(TAG, "查看file路径：" + "\n"+file.getAbsolutePath() + "\n");
                //二值化处理
                // PaintHelper.convertToBlackWhite(bitmap);

                //测试二值化处理是否成功
                textfile=Util.bitmap2File(PracticeActivity.this, PaintHelper.convertToBlackWhite(bitmap));
                Log.i(TAG, "查看二值化图片路径：" + "\n"+textfile.getAbsolutePath() + "\n");

                //上传绘图到服务器
//                HttpUtil.UploadPaint(this_user,textfile);
            }});

                paintView.setDrawType(DrawTypeEnum.PEN);
                //点击橡皮擦
                findViewById(R.id.btn_select_eraser).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG,"橡皮擦");
                        paintView.setDrawType(DrawTypeEnum.ERASER);
                    }
                });

                //点击清空面板
                findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG,"清空面板");
                        paintView.clear();
                    }
                });

                //点击上一步
                ivUndo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG,"上一笔");
                        paintView.undo();
                    }
                });
                //点击下一步
                ivRedo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG,"下一笔");
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
                               // onDestroy();
                                Intent intent=new Intent(PracticeActivity.this,ChooseMode.class);
                                startActivity(intent);
                    }
                });
                //点击暂停
                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialogUtils utils=AlertDialogUtils.getInstance();
                        utils.showConfirmDialog(PracticeActivity.this,"游戏暂停");
                        utils.setOnButtonClickListener(new AlertDialogUtils.OnButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog dialog) {
                                dialog.dismiss();
                                Log.i(TAG,"退出游戏，下一步应该跳转至主菜单");
                               // onDestroy();
                                Intent intent=new Intent(PracticeActivity.this,ChooseMode.class);
                                startActivity(intent);
                            }
                            @Override
                            public void onNegativeButtonClick(AlertDialog dialog) {
                                dialog.dismiss();
                                Log.i(TAG,"继续游戏，返回当前界面");
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
            }

    }