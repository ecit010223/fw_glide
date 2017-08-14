package com.huier.fw_glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.LibraryGlideModule;

import java.io.InputStream;

/**
 * 作者：张玉辉
 * 时间：2017/8/14.
 * 如果是library(即编写library项目)就实现LibraryGlideModule
 */

@GlideModule
public class OkHttpLibraryGlideModule extends LibraryGlideModule {
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        /**
         * OkHttpUrlLoader是Glide的OKHttp扩展库中的类，如果需要使用Glide的实现，可以在依赖中添加：
         * compile 'com.github.bumptech.glide:okhttp3-integration:4.0.0'
         * 添加完依赖不需要自己实现OkHttpLibraryGlideModule类，库中已经自带了，会自动使用OKHttp的。
         */
        registry.replace(GlideUrl.class, InputStream.class,new OkHttpUrlLoader.Factory());
    }
}
