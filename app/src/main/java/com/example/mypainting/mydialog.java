package com.example.mypainting;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

public class mydialog extends Dialog {

    public mydialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.mydialog);
        findViewById(R.id.oktext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
