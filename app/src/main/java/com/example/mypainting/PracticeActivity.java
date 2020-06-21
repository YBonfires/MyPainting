package com.example.mypainting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.mypainting.gson.Painting;
import com.example.mypainting.gson.Ret;
import com.example.mypainting.gson.User;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
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
    private  TextView resultmsg;
    private Button back,pause,clear;
    private Button game_start,submit;
    private Button hint;
    private User this_user=new User();
    private SelectPenWindow selectPenWindow;
    private SelectColorWindow selectColorWindow;
    private File file,textfile;
    private Painting paint;
    private MyHandler myHandler=new MyHandler();
    private MyResHandler myresHandler=new MyResHandler();
    private MyScoreHandler myScoreHandler=new MyScoreHandler();

    private static boolean[] isHandled = {false, false, false, false, false, false, false, false, false, false,false,
            false, false, false, false, false, false, false, false, false};
    private static int level = 0;
    private static int timer = 0;
    private static boolean over = false;

    private int i=0;
    private Chronometer ch;
    private ArrayList list;
    private int type;
    private String[] Paints={"apple", "book", "bowtie", "candle", "cloud", "cup", "door", "envelope", "eyeglasses", "guitar", "hammer",
            "hat", "ice cream", "leaf", "scissors", "star", "t-shirt", "pants", "lightning", "tree"};
    private int[] hintimage={R.drawable.apple, R.drawable.book,R.drawable.bowtie,R.drawable.candle,
            R.drawable.cloud,R.drawable.cup,R.drawable.door,R.drawable.envelope,
            R.drawable.eyeglasses,R.drawable.guitar,R.drawable.hammer,R.drawable.hat,
            R.drawable.icecream,R.drawable.leaf,R.drawable.scissors,R.drawable.star,
            R.drawable.tshirt,R.drawable.pants,R.drawable.lightning,R.drawable.tree};

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
                ch.setBase(SystemClock.elapsedRealtime() + 30000);
                ch.setFormat("%s");
                ch.start();
                Message message = Message.obtain();
                message.what = 0;
                message.arg1 = 1;
                myHandler.sendMessageDelayed(message, 30000);
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
                    ch.stop();
                    Toast toast = Toast.makeText(getApplicationContext(), "正在提交", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Bitmap bitmap = paintView.getPaintViewScreen(Bitmap.Config.ARGB_8888);
                    file = Util.bitmap2File(PracticeActivity.this, bitmap);
                    paintView.clear();
                    //测试二值化处理是否成功
                    textfile = Util.bitmap2File(PracticeActivity.this, PaintHelper.convertToBlackWhite(bitmap));
                    Log.i(TAG, "提前提交");
                    Message message = Message.obtain();
                    message.what = 0;
                    message.arg1 = 0;
                    myHandler.sendMessage(message);
                }
            });

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
                                over=true;
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
                                over=true;
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
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if(over)
                        break;
                    if ((msg.arg1==0&&!isHandled[level]) || (msg.arg1==1&&!isHandled[timer])) {
                        if(level==20) over=true;
                        isHandled[level] = true;
                        if(msg.arg1==1) {
                            Log.i(TAG, "第 "+timer+" 张画倒计时结束，开始处理");
                            Bitmap bitmap = paintView.getPaintViewScreen(Bitmap.Config.ARGB_8888);
                            file = Util.bitmap2File(PracticeActivity.this, bitmap);
                            paintView.clear();
                            //测试二值化处理是否成功
                            textfile = Util.bitmap2File(PracticeActivity.this, PaintHelper.convertToBlackWhite(bitmap));
                            timer++;
                        }
                        final String url = "http://49.235.199.207:8080/guessServer/SaveServlet";
                        final String url1 = "http://49.235.199.207:8080/guessServer/RecognizeServlet";

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    myresHandler.textView=resultmsg;
                                    ResponseBody responseBody =upload(url, textfile, this_user);
                                    String res = responseBody.string();
                                    Log.i(TAG, res);
                                    Ret ret = new Gson().fromJson(res, Ret.class);
                                    paint = new Gson().fromJson(ret.getData(), Painting.class);
                                    Log.i(TAG,"--------- save paint url--------"+paint.getUrl());

                                    OkHttpClient client = new OkHttpClient.Builder()
                                            .connectTimeout(5, TimeUnit.SECONDS)
                                            .readTimeout(5, TimeUnit.SECONDS)
                                            .writeTimeout(5, TimeUnit.SECONDS)
                                            .build();
                                    Request request = new Request.Builder()
                                            .url(url1)
                                            .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(paint)))
                                            .build();
                                    Response response = client.newCall(request).execute();
                                    String res1 = response.body().string();
                                    Log.i(TAG, res1);
                                    Ret ret1 = new Gson().fromJson(res1, Ret.class);
                                    Message message = Message.obtain();
                                    message.arg1 = ret1.getCode();
                                    message.obj ="你画的是"+ret1.getData();
                                    myresHandler.sendMessage(message);
                                    Log.i(TAG, "message-----------识别结果-------"+ret1.getData());
                                    Log.i(TAG, "Paints"+Paints[i]);
                                    if(ret1.getData().equals(Paints[i])) {
                                        Log.i(TAG, "通过第" + level + "关");
                                        level++;
                                        if(level == 20) {
                                            Log.i(TAG, "游戏结束");
                                            over = true;
                                        }
                                        else
                                            myHandler.sendEmptyMessage(1);
                                    }
                                    else {
                                        Log.i(TAG, "画错了，进入下一关");
                                        myHandler.sendEmptyMessage(1);
                                        over = false;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    else {
                        Log.i(TAG, "第 "+timer+" 张画倒计时结束，检测已处理，跳过处理");
                        timer++;
                    }
                    break;
                case 1:
                    Log.i(TAG, "--------下一关-----------");
                    //resultmsg.setText("你画的是...");
                    Log.i(TAG, "开始倒计时30s");
                    ch.setBase(SystemClock.elapsedRealtime() + 30000);
                    ch.setFormat("%s");
                    ch.start();
                    Random random = new Random();
                    i = random.nextInt(19);
                    titlemsg.setText("请画出" + Paints[i]);
                    Message message = Message.obtain();
                    message.what = 0;
                    message.arg1 = 1;
                    myHandler.sendMessageDelayed(message, 30000);
                    break;
                default:
                    break;
            }
        }
    }
    public ResponseBody upload(String url, File file, User user) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"),file))
                .addFormDataPart("user", (new Gson()).toJson(user))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        return response.body();
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