package com.example.mypainting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mypainting.gson.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.internal.Util;

public class Album<list> extends AppCompatActivity {
    private List<Bitmap> paintingList=new ArrayList<>();
    private String TAG="path";
    private  String path;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        back=findViewById(R.id.back);
        initPaintings();
        Log.i("Taag",path);
        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        PaintingAdapter adapter=new PaintingAdapter(paintingList);
        recyclerView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Album.this,ChooseMode.class);
                startActivity(intent);
            }
        });
    }


    private void initPaintings(){
      List<String> pathList=getImagePathFromSD();
      for(int i=0;i<pathList.size();i++){
          path = pathList.get(i);
          Bitmap bitmap = openImage(path);
          paintingList.add(bitmap);
      }
    }

    public static Bitmap openImage(String path){
        Bitmap bitmap = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }    return bitmap;
    }

    public String getFilesPath( Context context ){
        String filePath ;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            filePath = context.getExternalFilesDir(null).getPath();
        }else {
            //外部存储不可用
            filePath = context.getFilesDir().getPath() ;
        }
        return filePath ;
    }

    private List<String> getImagePathFromSD() {
        // 图片列表
       List<String> imagePathList = new ArrayList<String>();
      // 得到sd卡内image文件夹的路径   File.separator(/)
      String filePath ="/storage/emulated/0/Android/data/com.example.mypainting/files/paintviewdemo/file" ;
       // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
      // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
           File file = files[i];
       if (checkIsImageFile(file.getPath())) {
                      imagePathList.add(file.getPath());
                }
             }
        // 返回得到的图片列表
       return imagePathList;
  }

       @SuppressLint("DefaultLocale")
   private boolean checkIsImageFile(String fName) {
          boolean isImageFile = false;
             // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
         if ( FileEnd.equals("png")  ) {
                   isImageFile = true;
               } else {
             isImageFile = false;
                 }
          return isImageFile;
        }

   }




