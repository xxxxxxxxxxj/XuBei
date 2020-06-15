package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.QualifierType;
import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.common.photopreview.PhotoPreviewActivity;
import com.haohao.xubei.ui.module.common.photopreview.PhotoPreviewBean;

import java.util.ArrayList;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 图片查看器
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class PhotoPreviewModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(PhotoPreviewActivity activity);

    @QualifierType("p")
    @ActivityScoped
    @Provides
    static Integer provideIntPosition(Activity activity) {
        return activity.getIntent().getIntExtra(PhotoPreviewActivity.CURRENT_POSITION, 0);
    }

    @ActivityScoped
    @Provides
    static ArrayList<PhotoPreviewBean> provideArrayList(Activity activity) {
        return (ArrayList<PhotoPreviewBean>) activity.getIntent().getSerializableExtra(PhotoPreviewActivity.PHOTO_LIST);
    }
}
