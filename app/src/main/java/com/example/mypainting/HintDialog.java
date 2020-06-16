package com.example.mypainting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class HintDialog extends Dialog {

    public HintDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.activity_hint_dialog);
        findViewById(R.id.oktext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}