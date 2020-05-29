package com.xubei.shop.ui.views;

import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import javax.inject.Inject;

/**
 * 通用popweindow在6.0以上显示位置问题
 * date：2017/6/28 14:41
 * author：Seraph
 *
 **/
public class FixNPopWindow extends PopupWindow {

    @Inject
    FixNPopWindow() {
        this(null,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public FixNPopWindow(View contentView, int width, int height) {
        super(contentView, width, height);
        setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        setOutsideTouchable(true);
        setTouchable(true);
        setFocusable(true);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }
}
