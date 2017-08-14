package com.huier.fw_glide;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.RequestOptions;

/**
 * 作者：张玉辉
 * 时间：2017/8/14.
 * 为了添加新的方法，修改已有的方法或者添加对其他类型格式的支持，你需要在扩展中使用加了注解的静态方法。
 * GlideOption用来添加自定义的方法，GlideType用来支持新的格式。
 */
@GlideExtension
public class CustomGlideExtension {
    //缩略图的最小尺寸，单位：px
    private static final int MINI_THUMB_SIZE = 100;

    //将构造方法设为私有，作为工具类使用
    private CustomGlideExtension(){
    }

    /**
     * 1）自己新增的方法的第一个参数必须是RequestOptions options；
     * 2）方法必须是静态的。
     * @param options
     */
    @GlideOption
    public static void miniThumb(RequestOptions options){
        options.fitCenter().override(MINI_THUMB_SIZE);
    }
}
