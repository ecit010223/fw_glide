package com.huier.fw_glide;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mActivity;
    private ImageView imgContent;
    private Button btnSimple;
    private Button btnCancel;
    private Button btnGlideApp;
    //当前被点击的View
    private View mCurrentClickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
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
        boolean hasPermission = ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.INTERNET)== PackageManager.PERMISSION_GRANTED;
        if(!hasPermission){
            ActivityCompat.requestPermissions(scanForActivity(mActivity),new String[]{Manifest.permission.INTERNET},
                    Constants.INTERNET_REQEUST_CODE);
            ActivityCompat.shouldShowRequestPermissionRationale(scanForActivity(mActivity),Manifest.permission.INTERNET);
            return false;
        }else{
            return true;
        }
    }

    /** 简单使用 **/
    private void simpleUse(){
        Log.d(Constants.TAG,"simpleUse()");
        Glide.with(mActivity).load(Constants.URL_SIMPLE).into(imgContent);
    }

    /**
     * 取消加载
     * 实际上并不需要取消加载，因为在with方法中传入的Activity或Fragment被销毁的时候，Glide会自动取消加载
     * 并且回收所有的加载过程中所使用的资源。
     */
    private void cancelUse(){
        Log.d(Constants.TAG,"cancelUse()");
        Glide.with(mActivity).clear(imgContent);
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
        GlideApp.with(mActivity)
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
