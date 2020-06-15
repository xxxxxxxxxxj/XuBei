package com.haohao.xubei.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.haohao.xubei.R;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;

/**
 * 透明的等待dialog
 */
public class CustomLoadingDialog extends Dialog {

    @Inject
    CustomLoadingDialog(Context context) {
        super(context, R.style.progress_dialog);
        setContentView(R.layout.default_dialog_loading);
        setCanceledOnTouchOutside(true);
        try {
            Objects.requireNonNull(getWindow()).getAttributes().gravity = Gravity.CENTER;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setCancelable(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                dismiss();
                break;
        }
        return true;
    }


    public void setDialogMessage(String message) {
        show();
        if (message != null) {
            ((TextView) findViewById(R.id.tv_loading_msg)).setText(message);
        }
    }
}