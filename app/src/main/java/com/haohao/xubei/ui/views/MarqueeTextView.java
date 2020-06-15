package com.haohao.xubei.ui.views;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * 实现跑马灯效果的TextView
 * date：2017/11/3 17:19
 * author：Seraph
 **/
public class MarqueeTextView extends androidx.appcompat.widget.AppCompatTextView {
    public MarqueeTextView(Context context) {
        super(context);
        init();
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setSingleLine(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }


    //返回textView是否处在选中的状态
    //而只有选中的textView才能够实现跑马灯效果
    @Override
    public boolean isFocused() {
        return true;
    }
}
