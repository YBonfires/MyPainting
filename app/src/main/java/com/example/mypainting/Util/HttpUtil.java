package com.example.mypainting.Util;
import android.util.Log;

import com.example.mypainting.gson.Paint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

    private static final String TAG = "HttpUtil";
    public static void sendOkHttpRequest(String url){
        OkHttpClient client = new OkHttpClient();
        //String url = "http://10.0.2.2:8080/guessServer/RecognizeServlet";
        Paint paint;
        OkHttpClient client1=new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Connection","close")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            Log.i(TAG, jsonString);
            Gson gson = new Gson();
            paint = gson.fromJson(jsonString, new TypeToken<Paint>(){}.getType());
            System.out.println(paint.getCode());
            System.out.println(paint.getMsg());
            System.out.println(paint.getData());

    } catch ( IOException e) {
        Log.i(TAG, "IO异常，尝试重新请求");
        e.printStackTrace();
    }
    }
}
