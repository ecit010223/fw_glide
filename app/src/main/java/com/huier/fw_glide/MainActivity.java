package com.huier.fw_glide;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

/**
 * 占位符就是请求的图片没加载出来时显示的默认图片，Glide支持三种不同情况下的占位符：
 * 1）Placeholder请求图片加载中；
 * 2）Error请求图片加载错误；
 * 3）Fallback请求url/model为空。
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private ImageView imgContent;
    private RecyclerView mRecyclerView;
    private Button btnSimple,btnCancel,btnGlideApp,btnCustomGlide,btnGifGlide,btnPlaceholder,
            btnRequestOptions,btnTransitionOptions,btnRequestBuilder,btnTransformations;
    //当前被点击的View
    private View mCurrentClickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
    }

    private void initView(){
        imgContent = (ImageView) findViewById(R.id.img_content);
        btnSimple = (Button)findViewById(R.id.btn_simple);
        btnSimple.setOnClickListener(this);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        btnGlideApp = (Button)findViewById(R.id.btn_GlideApp);
        btnGlideApp.setOnClickListener(this);
        btnCustomGlide = (Button)findViewById(R.id.btn_custom_glide);
        btnCustomGlide.setOnClickListener(this);
        btnGifGlide = (Button)findViewById(R.id.btn_gif_glide);
        btnGifGlide.setOnClickListener(this);
        btnPlaceholder = (Button)findViewById(R.id.btn_placeholder);
        btnPlaceholder.setOnClickListener(this);
        btnRequestOptions = (Button)findViewById(R.id.btn_RequestOptions);
        btnRequestOptions.setOnClickListener(this);
        btnTransitionOptions = (Button)findViewById(R.id.btn_TransitionOptions);
        btnTransitionOptions.setOnClickListener(this);
        btnRequestBuilder = (Button)findViewById(R.id.btn_RequestBuilder);
        btnRequestBuilder.setOnClickListener(this);
        btnTransformations = (Button)findViewById(R.id.btn_Transformations);
        btnTransformations.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(requestPermission()){
            switch (view.getId()){
                case R.id.btn_simple:
                    simpleUse();
                    break;
                case R.id.btn_cancel:
                    cancelUse();
                    break;
                case R.id.btn_GlideApp:
                    glideAppUse();
                    break;
                case R.id.btn_custom_glide:
                    customGlide();
                    break;
                case R.id.btn_gif_glide:
                    gifGlide();
                    break;
                case R.id.btn_placeholder:
                    placeHolder();
                    break;
                case R.id.btn_RequestOptions:
                    requestOptions();
                    break;
                case R.id.btn_TransitionOptions:
                    transitionOptions();
                    break;
                case R.id.btn_RequestBuilder:
                    requestBuilder();
                    break;
                case R.id.btn_Transformations:
                    transformations();
                    break;
            }
        }else{
            mCurrentClickView = view;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Constants.INTERNET_REQEUST_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    onClick(mCurrentClickView);
                }
                break;
        }
    }

    private boolean requestPermission(){
        boolean hasPermission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.INTERNET)== PackageManager.PERMISSION_GRANTED;
        if(!hasPermission){
            ActivityCompat.requestPermissions(scanForActivity(mContext),new String[]{Manifest.permission.INTERNET},
                    Constants.INTERNET_REQEUST_CODE);
            ActivityCompat.shouldShowRequestPermissionRationale(scanForActivity(mContext),Manifest.permission.INTERNET);
            return false;
        }else{
            return true;
        }
    }

    /**
     * Glide会自动读取ImageView的缩放类型，所以一般在layout文件指定scaleType即可：
     * CenterCrop, CenterInside, CircleCrop, FitCenter, RoundedCorners。
     * Glide支持在Java代码中设置这些缩放类型：
     * 1）CenterCrop缩放宽和高都到达View的边界,有一个参数在边界上,另一个参数可能在边界上,也可能超过边界;
     * 2）CenterInside如果宽和高都在View的边界内,那就不缩放,否则缩放宽和高都进入View的边界,
     *   有一个参数在边界上,另一个参数可能在边界上,也可能在边界内;
     * 3）CircleCrop圆形且结合了CenterCrop的特性；
     * 4）FitCenter缩放宽和高都进入View的边界,有一个参数在边界上,另一个参数可能在边界上,也可能在边界内;
     * 5）RoundedCorners圆角。
     */
    private void transformations(){
        //方式一：使用RequestOptions
        RequestOptions options = new RequestOptions();
        options.centerInside();

        Glide.with(mContext)
                .load(Constants.URL_SIMPLE)
                .apply(options)
                .into(imgContent);
        //方式二：使用RequestOptions中的transform方法
        Glide.with(mContext)
                .load(Constants.URL_SIMPLE)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgContent);
        //方式三：V4特性
        GlideApp.with(mContext)
                .load(Constants.URL_SIMPLE)
                .fitCenter()
                .into(imgContent);
    }

    /**
     * 1）指定加载类型。asBitmap()、asGif()、asDrawable()、asFile()；
     * 2）指定要加载url/model；
     * 3）指定要加载到那个View；
     * 4）指定要应用的RequestOption；
     * 5）指定要应用的TransitionOption；
     * 6）指定要加载的缩略图。
     */
    private void requestBuilder(){
        //指定类型为Bitmap
        RequestBuilder<Bitmap> requestBuilder = Glide.with(mContext).asBitmap();
        //RequestBuilder也可以重复使用
        Glide.with(mContext)
                .asDrawable()
                .apply(centerCropTransform());
//        for (int i = 0; i < numViews; i++) {
//            ImageView view = viewGroup.getChildAt(i);
//            String url = urls.get(i);
//            requestBuilder.load(url).into(view);
//        }
    }

    /**
     * Recycle的加载优化：只在拖动和静止时加载，自动滑动时不加载。
     */
    private void optimizeRecycle(){
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        GlideApp.with(mContext).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        GlideApp.with(mContext).pauseRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        GlideApp.with(mContext).resumeRequests();
                        break;
                }
            }
        });
    }

    /**
     * Glide中的过渡动画是指占位符到请求图片或缩略图到完整尺寸请求图片的动画，过渡动画只能针对单一请求，不能跨请求执行。
     * 1）淡入；
     * 2）交叉淡入；
     * 3）不过渡
     * TransitionOptions是和你要加载的资源的类型绑定的，也就是说，如果你请求一张位图(Bitmap),你就需要
     * 使用BitmapTransitionOptions，而不是DrawableTransitionOptions。因此，你请求的这张位图，
     * 你需要用简单的淡入，而不能用交叉淡入(DrawableTransitionOptions.withCrossFade())。
     * 如果既不是Bitmap也不是Drawable可以使用GenericTransitionOptions。
     * 如果要使用自定义的动画，可以使用GenericTransitionOptions.with(int viewAnimationId)或者
     * BitmapTransitionOptions.withCrossFade(int animationId, int duration)或者
     * DrawableTransitionOptions.withCrossFade(int animationId, int duration)。
     */
    private void transitionOptions(){
        Glide.with(mContext)
                .load(Constants.URL_SIMPLE)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgContent);
        /**
         * 过渡动画执行时机：
         * 1）图片在磁盘缓存；
         * 2）图片在本地；
         * 3）图片在远程。
         * 如果图片在内存缓存上是不会执行过渡动画的，如果需要在内存缓存上加载动画，可以这样：
         */
        GlideApp.with(this)
                .load(R.mipmap.ic_launcher)
                .listener(new RequestListener(){
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target,
                                                boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource,
                                           boolean isFirstResource) {
                        if (dataSource == DataSource.MEMORY_CACHE) {
                            //当图片位于内存缓存时，glide默认不会加载动画
                            imgContent.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
                        }
                        return false;
                    }
                })
                .fitCenter()
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .into(imgContent);
        /**
         * 出于性能考虑，最好不要在ListView,GridView,RecycleView中使用过渡动画,使用TransitionOptions.dontTransition()
         * 可以不加载动画，也可以使用dontAnimate不加载动画。
         */
        GlideApp.with(mContext)
                .load(Constants.URL_SIMPLE)
                .placeholder(R.mipmap.ic_launcher)
                .dontAnimate()
                .into(imgContent);
    }

    /**
     * Glide中的大多请求参数都可以通过RequestOptions类和apply()方法来设置,Glide中的请求参数主要有:
     * 1）Placeholders占位符；
     * 2）Transformations变换；
     * 3）Caching Strategies缓存策略；
     * 4）组件特定参数：编码质量，解码参数等。
     * 如下，要将图片的显示方式设为CenterCrop：
     */
    private void requestOptions(){
        Glide.with(mContext)
                .load(Constants.URL_SIMPLE)
                .apply(centerCropTransform())
                .into(imgContent);
    }

    /** 点位符的使用 **/
    private void placeHolder(){
        GlideApp.with(mContext)
                .load("http://xxxx.com")
                .placeholder(R.mipmap.ic_launcher)
                .error(new ColorDrawable(Color.RED))
                .fallback(new ColorDrawable(Color.CYAN))
                .into(imgContent);
    }

    /** 调用自定义添加的类型 **/
    private void gifGlide(){
        GlideApp.with(mContext)
                .asGif()
                .load(Constants.URL_GIF)
                .into(imgContent);
    }

    /** 调用自定义的方法 **/
    private void customGlide(){
        GlideApp.with(mContext)
                .load(Constants.URL_SIMPLE)
                .miniThumb()
                .into(imgContent);
    }

    /** 简单使用 **/
    private void simpleUse(){
        Log.d(Constants.TAG,"simpleUse()");
        Glide.with(mContext)
                .load(Constants.URL_SIMPLE)
                .into(imgContent);
    }

    /**
     * 取消加载
     * 实际上并不需要取消加载，因为在with方法中传入的Activity或Fragment被销毁的时候，Glide会自动取消加载
     * 并且回收所有的加载过程中所使用的资源。
     */
    private void cancelUse(){
        Log.d(Constants.TAG,"cancelUse()");
        Glide.with(mContext)
                .clear(imgContent);
    }

    /**
     * Glidev4中的Glide.with().load()后没有之前版本的fitCenter和placeholder这样的方法，但是GlideApp有，
     * 可以直接在builder中使用。GlideApp可以代替之前版本的Glide开头.
     * 1）对于library项目来讲可以使用自定义方法继承Glide的API；
     * 2）对于应用来讲，在继承Glide的API后，可以通过添加自定义方法。
     * 要使用GlideApp，必须继承AppGlideModule
     */
    private void glideAppUse(){
        Log.d(Constants.TAG,"glideAppUse");
        GlideApp.with(mContext)
                .load(Constants.URL_SIMPLE)
                .placeholder(R.mipmap.ic_launcher)
                .fitCenter()
                .into(imgContent);
    }

    private Activity scanForActivity(Context context){
        if(context == null){
            return null;
        }else if(context instanceof Activity){
            return (Activity)context;
        }else if(context instanceof ContextWrapper){
            return scanForActivity(((ContextWrapper)context).getBaseContext());
        }
        return null;
    }
}
