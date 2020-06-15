package com.haohao.xubei.utlis;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.haohao.xubei.R;

import javax.inject.Inject;

/**
 * 横向分割线
 * date：2018/10/16 10:32
 * author：xiongj
 **/
public class myDividerItemDecoration extends DividerItemDecoration {

    @Inject
    public myDividerItemDecoration(Context context) {
        super(context, DividerItemDecoration.HORIZONTAL);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.act_acc_list_divider_h);
        if (drawable != null) {
            setDrawable(drawable);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int viewPosition = parent.getChildAdapterPosition(view);
        if (viewPosition == 0) {
            outRect.left = SizeUtils.dp2px(13);
        }
    }


}
