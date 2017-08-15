package com.huier.fw_glide;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

/**
 * 作者：张玉辉
 * 时间：2017/8/15.
 * Glide对ImageView的width和height属性是这样解析的：
 * 1）如果width和height都大于0，则使用layout中的尺寸；
 * 2）如果width和height都是WRAP_CONTENT，则使用屏幕尺寸；
 * 3）如果width和height中至少有一个值<=0并且不是WRAP_CONTENT，那么就会在布局的时候添加一个OnPreDrawListener监听ImageView的尺寸
 * Glide对WRAP_CONTENT的支持并不好，所以尽量不要用。
 *
 * 在运行修改ImageView尺寸方式一：继承ImageViewTarget
 * 这里指定的View的类型是ImageView,资源类型是Bitmap，可根据需要修
 * 使用：
    GlideApp.with(context)
            .asBitmap()
            .load(url)
            .dontAnimate()
            .placeholder(R.drawable.img_default)
            .into(new CustomImageViewTarget(imageview, 300, 300));
 * 在运行修改ImageView尺寸方法二：使用override()
    GlideApp.with(mContext)
            .load(url)
            .override(width,height)
            .into(view);
 */

public class CustomImageViewTarget extends ImageViewTarget<Bitmap> {
    private int width, height;

    public CustomImageViewTarget(ImageView view) {
        super(view);
    }

    public CustomImageViewTarget(ImageView view, int width, int height) {
        super(view);
        this.width = width;
        this.height = height;
    }

    /**
     * 可以通过resource获取图片的尺寸
     * @param resource
     * @param transition
     */
    @Override
    public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
    }

    @Override
    protected void setResource(@Nullable Bitmap resource) {
        view.setImageBitmap(resource);
    }

    @Override
    public void getSize(SizeReadyCallback cb) {
        if (width > 0 && height > 0) {
            cb.onSizeReady(width, height);
            return;
        }
        super.getSize(cb);
    }
}
