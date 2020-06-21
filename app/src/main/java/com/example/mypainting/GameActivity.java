package com.example.mypainting;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
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
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zhanglei.com.paintview.DrawTypeEnum;
import zhanglei.com.paintview.PaintView;
import zhanglei.com.paintview.Util;


public class GameActivity extends BaseActivity implements IPaintColorListener,IPaintPenListner{
    private static final String TAG="MainActivity";
    //用户id
    private int nextRound=1;
    //定义布局元素
    private PaintView paintView;
    private ImageView ivUndo;
    private ImageView ivRedo;
    private ImageView logo;
    private  TextView titlemsg;
    private  TextView resultmsg,score;
    private Button back,pause,clear;
    private Button game_start,submit;

    private File file;
    private File textfile;
    private Painting paint;

    private SelectPenWindow selectPenWindow;
    private SelectColorWindow selectColorWindow;

    private MyResHandler myHandler=new MyResHandler();
    private MyScoreHandler myScoreHandler=new MyScoreHandler();
    private String errMsg;
    private String topic;
    //当前用户
    private User this_user=new User();
    //计数器 全局
    private Chronometer ch;
    String[] Paints = {"apple", "book", "bowtie", "candle", "cloud", "cup", "door", "envelope", "eyeglasses", "guitar", "hammer",
            "hat", "ice cream", "leaf", "scissors", "star", "t-shirt", "pants", "lightning", "tree"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        // supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        logo = findViewById(R.id.logo);
        score=findViewById(R.id.score);
        paintView = findViewById(R.id.paintView);
        ivRedo = findViewById(R.id.iv_redo);
        ivUndo = findViewById(R.id.iv_undo);
        //title = findViewById(R.id.title);
        game_start = findViewById(R.id.game_start);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
        pause = findViewById(R.id.pause);
        titlemsg = findViewById(R.id.title);
        resultmsg = findViewById(R.id.resultmsg);
        //实例化计时器
        ch = (Chronometer) findViewById(R.id.chronometer);
        selectPenWindow = new SelectPenWindow(this);
        selectPenWindow.setPaintPenListener(this);

        selectColorWindow = new SelectColorWindow(this);
        selectColorWindow.setIPaintColorListener(this);
        this_user.setUserid(1);
        //开始游戏
        game_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                game_start.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                //循环
                do{
                Log.i(TAG, "随机抽取题目...");
                Random random = new Random();
                int i = random.nextInt(19);
                Log.i(TAG, "生成随机题目i为" + i);
                titlemsg.setText("请画出" + Paints[i]);
                //设置初始时间
                ch.setBase(SystemClock.elapsedRealtime() + 30000);
                ch.setFormat("%s");
                //倒计时实现
                ch.start();
                ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        ch.setText(ch.getText().toString().substring(1));
                        if (SystemClock.elapsedRealtime() - ch.getBase() >= 0) ch.stop();
                    }
                });

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
                file = Util.bitmap2File(GameActivity.this, bitmap);
                paintView.clear();
                Log.i(TAG, "查看file路径：" + "\n" + file.getAbsolutePath() + "\n");
                //二值化处理
                // PaintHelper.convertToBlackWhite(bitmap);
                //测试二值化处理是否成功
                textfile = Util.bitmap2File(GameActivity.this, PaintHelper.convertToBlackWhite(bitmap));
                Log.i(TAG, "查看二值化图片路径：" + "\n" + textfile.getAbsolutePath() + "\n");

                final String save_url = "http://49.235.199.207:8080/guessServer/SaveServlet";
                final String rec_url = "http://49.235.199.207:8080/guessServer/RecognizeServlet";
                final String score_url = "http://49.235.199.207:8080/guessServer/ScoreServlet";
                //上传绘图到服务器
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            myHandler.textView=resultmsg;
                            //上传绘图到服务器
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("file", textfile.getName(),
                                            RequestBody.create(MediaType.parse("multipart/form-data"),textfile))
                                    .addFormDataPart("user", (new Gson()).toJson(this_user))
                                    .build();
                            Request request = new Request.Builder()
                                    .url(save_url)
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);
                            String res = response.body().string();
                            Log.i(TAG, "----------保存返回数据------Respnsonse---------" + res);
                            //Gson gson=new GsonBuilder().setDateFormat("MMM dd, yyyy, hh:mm:ss aa").create();
                            Ret ret = new Gson().fromJson(res, Ret.class);
                            //保存painting类
                            paint = new Gson().fromJson(ret.getData(),Painting.class);
                            Log.i(TAG,"--------- save paint url--------"+paint.getUrl());
                            nextRound = ret.getCode();
                            Log.i(TAG, "--------保存图片------nextRound------"+ret.getCode());
                        } catch (IOException e) {
                            Log.e(TAG, "失败");
                            e.printStackTrace();
                        }

                        //识别图片
                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(5, TimeUnit.SECONDS)
                                .readTimeout(5, TimeUnit.SECONDS)
                                .writeTimeout(5, TimeUnit.SECONDS)
                                .build();
                        Request request = new Request.Builder()
                                .url(rec_url)
                                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(paint)))
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String res = response.body().string();
                            Log.i(TAG, "识别返回数据" + res);
                            Ret ret = new Gson().fromJson(res, Ret.class);
                            Message message = Message.obtain();
                            message.arg1 = ret.getCode();
                            message.obj =ret.getData();
                            myHandler.sendMessage(message);
                            Log.i(TAG, "message-----------myHandler.textView----"+myHandler.textView);
                            Log.i(TAG, "message-----------识别结果-------"+message.obj);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                } }).start();
                //获取用户积分
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myScoreHandler.textView=score;
                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(5, TimeUnit.SECONDS)
                                .readTimeout(5, TimeUnit.SECONDS)
                                .writeTimeout(5, TimeUnit.SECONDS)
                                .build();
                        Request request = new Request.Builder()
                                .url(score_url)
                                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(this_user)))
                                .build();
                        try{
                            Response response = client.newCall(request).execute();
                            String res = response.body().string();
                            Log.i(TAG, "---------------积分返回-------------res"+res);
                            Ret ret = new Gson().fromJson(res, Ret.class);
                            User user=new Gson().fromJson(ret.getData(),User.class);
                            Message message1 = Message.obtain();
                            message1.arg1 = ret.getCode();
                            message1.obj = "积分 " + user.getScore();
                            Log.i(TAG, "---------------积分返回-------------" +message1.obj);
                            myScoreHandler.sendMessage(message1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
            }while(nextRound==0);
    }
});

        //启动画笔功能
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
                BackAlertDialog dialog=BackAlertDialog.getInstance();
                dialog.showConfirmDialog(GameActivity.this,"现在退出不会保存记录哦\n确定退出吗?");
                dialog.setOnButtonClickListener(new BackAlertDialog.OnButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(AlertDialog dialog) {
                        dialog.dismiss();
                        Log.i(TAG,"点击了确定按钮，下一步应该跳转至主菜单");
                        //onDestroy();
                        Intent intent=new Intent(GameActivity.this,ChooseMode.class);
                        startActivity(intent);
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
                utils.showConfirmDialog(GameActivity.this,"游戏暂停");
                utils.setOnButtonClickListener(new AlertDialogUtils.OnButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(AlertDialog dialog) {
                        dialog.dismiss();
                        Log.i(TAG,"退出游戏，下一步应该跳转至主菜单");
                        //onDestroy();
                        Intent intent=new Intent(GameActivity.this,ChooseMode.class);
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

