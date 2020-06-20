package com.example.mypainting.Util;
import android.Manifest;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mypainting.gson.Painting;
import com.example.mypainting.gson.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtil {
    private static final String TAG = "UploadPaint";
    private static final String TAG1 = "RecognizeTest";

    public static void UploadPaint(User user, final File file) {
        final String url = "http://10.0.2.2:8080/guessServer/SavaServlet";
//        String imagePath= file.getAbsolutePath();
//        final File file1=new File(imagePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    User user = new User();
                    user.setUserid(user.getUserid());
                    ResponseBody responseBody = upload(url, file, user);
                    Log.e(TAG, "成功");
                    Log.i(TAG, responseBody.string());
                } catch (IOException e) {
                    Log.e(TAG, "失败");
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static ResponseBody upload(String url, File file, User user) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("image/png"),file))
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
    public static void RecognizePaint(Painting painting){
        final String url = "http://10.0.2.2:8080/guessServer/RecognizeServlet";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Painting painting = new Painting();
                painting.setUrl(painting.getUrl());
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .writeTimeout(5, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create( MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(painting)))
                        .build();
                try{
                    Response response = client.newCall(request).execute();
                    String res = response.body().string();
                    Log.i(TAG1, res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
