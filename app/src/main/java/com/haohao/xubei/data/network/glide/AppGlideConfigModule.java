package com.haohao.xubei.data.network.glide;

import android.content.Context;
import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.R;


/**
 * Glide加载配置
 * date：2017/12/26 15:11
 * author：Seraph
 *
 **/
@GlideModule
public class AppGlideConfigModule extends AppGlideModule {

    //配置
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        //磁盘缓存
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, AppConfig.CACHE_MAX_SIZE));
        //默认请求
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_RGB_565)
                        .disallowHardwareConfig()
                        .placeholder(R.mipmap.icon_placeholder)
                        .error(R.mipmap.icon_error)
        );
        //日志级别
        //builder.setLogLevel(Log.DEBUG);
    }


    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
