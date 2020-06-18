package com.example.mypainting.Util;
import android.util.Log;

import com.example.mypainting.gson.PaintRet;
import com.example.mypainting.gson.UsrPaint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class HttpUtil {

//    private static final String TAG = "HttpUtil";
//    public static void sendOkHttpRequest(String url){
//        OkHttpClient client = new OkHttpClient();
//        //String url = "http://10.0.2.2:8080/guessServer/RecognizeServlet";
//        PaintRet paint;
//        OkHttpClient client1=new OkHttpClient.Builder()
//                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Connection","close")
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            String jsonString = response.body().string();
//            Log.i(TAG, jsonString);
//            Gson gson = new Gson();
//            paint = gson.fromJson(jsonString, new TypeToken<PaintRet>(){}.getType());
//            System.out.println(paint.getCode());
//            System.out.println(paint.getMsg());
//            System.out.println(paint.getData());
//
//    } catch ( IOException e) {
//        Log.i(TAG, "IO异常，尝试重新请求");
//        e.printStackTrace();
//    }
//    }

    public static void savePainttoServer(UsrPaint user, File file) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url="http://10.0.2.2:8080/guessServer/SavaServlet";
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
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.protocol() + " " + response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.i(TAG, headers.name(i) + ":" + headers.value(i));
                }
                Log.i(TAG, "onResponse: " + response.body().string());
            }
        });
    }
}
