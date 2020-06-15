package com.haohao.xubei.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.haohao.xubei.R;

import javax.inject.Inject;

import androidx.annotation.NonNull;

/**
 * 领取成功悬浮框
 */
public class SuccessfulReceiptDialog extends Dialog implements View.OnClickListener {

    private View.OnClickListener onClickListener;

    @Inject
    public SuccessfulReceiptDialog(Activity activity) {
        super(activity, R.style.progress_dialog);
        setContentView(R.layout.dialog_receipt_successful);
        //设置属性
        setCanceledOnTouchOutside(true);
        setCancelable(false);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.dialog_receipt_successful);
        initLayout();
    }

    private void initLayout() {
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);
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

    /**
     * 设置宽高
     */
    public SuccessfulReceiptDialog setDialogWidthHeight(int width, int height) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = width;
        layoutParams.height = height;
        getWindow().setAttributes(layoutParams);
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok://打开福利中心
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
                break;
        }
        dismiss();
    }

    //设置监听
    public SuccessfulReceiptDialog setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }
}