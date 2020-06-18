package com.example.mypainting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class AlertDialogUtils {
    public static AlertDialogUtils getInstance() {
        return new AlertDialogUtils();
    }
    public static void showConfirmDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        View view = View.inflate(context, R.layout.alert_dialog_layout, null);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_message_dialog);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel_dialog);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm_dialog);

        tvMsg.setText(message);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClickListener.onNegativeButtonClick(alertDialog);
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClickListener.onPositiveButtonClick(alertDialog);
            }
       });

        alertDialog.getWindow().setContentView(view);
    }


    private static OnButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public interface OnButtonClickListener {
        void onPositiveButtonClick(AlertDialog dialog);
        void onNegativeButtonClick(AlertDialog dialog);
    }
}
