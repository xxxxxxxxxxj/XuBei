package com.xubei.shop.utlis;

import android.content.Context;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * com.haohao.zuhaohao.utlis
 * date：2018/9/5 12:34
 * author：Seraph
 **/
public class LinearLayoutManager2 extends LinearLayoutManager {

    @Inject
    public LinearLayoutManager2(Context context) {
        super(context);
    }


    /**
     * 重写该方法，去捕捉该异常
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

}
