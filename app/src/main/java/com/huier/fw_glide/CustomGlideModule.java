package com.huier.fw_glide;


import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

/**
 * 作者：张玉辉
 * 时间：2017/8/14.
 * 这个类实现必须要有@GlideModule注解。
 * 如果添加的方法失效，那就检查下这里。
 * Glide会自动合理分配内存缓存，但是也可以自己手动分配。
 * 其它方法：
    //清除内存
    GlideApp.get(context).clearMemory();
    //在使用的时候，可以跳过内存缓存：
    GlideApp.with(getActivity())
        .load(url)
        .skipMemoryCache(true)
        .dontAnimate()
        .centerCrop()
        .into(imageView);
    //清除磁盘缓存
    GlideApp.get(context).clearDiskCache();
    //加载图片时设置磁盘缓存策略：
    GlideApp.with(getActivity())
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .dontAnimate()
        .centerCrop()
        .into(imageView);
 * 默认的策略是DiskCacheStrategy.AUTOMATIC
 * DiskCacheStrategy有五个常量：
 * 1）DiskCacheStrategy.ALL使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据；
 * 2）DiskCacheStrategy.NONE不使用磁盘缓存；
 * 3）DiskCacheStrategy.DATA在资源解码前就将原始数据写入磁盘缓存；
 * 4）DiskCacheStrategy.RESOURCE在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源；
 * 5）DiskCacheStrategy.AUTOMATIC根据原始图片数据和资源编码策略来自动选择磁盘缓存策略。
 */
@GlideModule
public class CustomGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        /**
         * 自定义配置内存缓存方式一：通过MemorySizeCalculator设置。
         * setMemoryCacheScreens设置MemoryCache应该能够容纳的像素值的设备屏幕数，说白了就是缓存多少屏图片，默认值是2。
         */
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));

        /** 自定义配置内存缓存方式二 */
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));

        /** 自定义配置内存缓存方式三：自己实现MemoryCache接口 */
//        builder.setMemoryCache(new CustomGlideMemoryCache());

        /************************************* 磁盘缓存 *********************************/
        //Glide使用DiskLruCacheWrapper作为默认的磁盘缓存，默认大小是250M,缓存文件放在APP的缓存文件夹下。
        //可以指定缓存在内部存储或外部存储，也可以指定缓存大小和文件夹。
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "cacheFolderName", diskCacheSizeBytes));
//        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context));

        //自定义磁盘缓存
//        builder.setDiskCache(new DiskCache.Factory() {
//            @Override
//            public DiskCache build() {
//                return new YourAppCustomDiskCache();
//            }
//        });
    }

    /**
     * 禁止解析Manifest文件,主要针对V3升级到v4的用户，可以提升初始化速度，避免一些潜在错误。
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
