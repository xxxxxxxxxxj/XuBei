package com.haohao.xubei.ui.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.haohao.xubei.R;
import com.haohao.xubei.ui.module.main.model.NoticeBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TextSwitcher 上下滚动
 * date：2018/6/25 17:41
 * author：Seraph
 **/
public class TextSwitcherView extends TextSwitcher implements ViewSwitcher.ViewFactory {

    private List<NoticeBean> reArrayList = new ArrayList<>();
    private int resIndex = 0;
    private final int UPDATE_TEXTSWITCHER = 1;
    private int timerStartAgainCount = 0;
    private NoticeBean showBean;

    public TextSwitcherView(Context context) {
        super(context);
        init();
    }

    public TextSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setFactory(this);
        this.setInAnimation(getContext(), R.anim.vertical_in);
        this.setOutAnimation(getContext(), R.anim.vertical_out);
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 3000);
    }

    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            //不能在这里创建任何UI的更新，toast也不行
            Message msg = new Message();
            msg.what = UPDATE_TEXTSWITCHER;
            handler.sendMessage(msg);
        }
    };

    private Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case UPDATE_TEXTSWITCHER:
                updateTextSwitcher();
                break;
            default:
                break;
        }
        return false;
    });

    /**
     * 需要传递的资源
     */
    public void getResource(List<NoticeBean> reArrayList) {
        this.reArrayList = reArrayList;
    }

    public void updateTextSwitcher() {
        if (this.reArrayList != null && this.reArrayList.size() > 0) {
            showBean = this.reArrayList.get(resIndex++);
            this.setText(showBean.title);
            if (resIndex > this.reArrayList.size() - 1) {
                resIndex = 0;
            }
        }

    }

    @Override
    public View makeView() {
        TextView tView = new TextView(getContext());
        tView.setTextSize(14);
        tView.setTextColor(0xff33b5e5);
        tView.setMaxLines(1);
        tView.setEllipsize(TextUtils.TruncateAt.END);
        return tView;
    }

    //获取当前展示的bean
    public NoticeBean getShowTextBean() {
        return showBean;
    }
}
