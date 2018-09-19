package com.example.zxingtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_zx;
    private Button btn_exit;
    private PopupWindow popupWindow;
    private WindowManager.LayoutParams params;
    private boolean mIsShowing = false;
    public static final int REQUEST_CODE_SCAN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        params = getWindow().getAttributes();

    }

    private void initView() {
        btn_zx = (Button) findViewById(R.id.btn_zx);
        btn_exit = (Button) findViewById(R.id.btn_exit);

        btn_zx.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_zx:
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                /*ZxingConfig是配置类
                 *可以设置是否显示底部布局，闪光灯，相册，
                 * 是否播放提示音  震动
                 * 设置扫描框颜色等
                 * 也可以不传这个参数
                 * */
                ZxingConfig config = new ZxingConfig();
                config.setPlayBeep(true);//是否播放扫描声音 默认为true
                config.setShake(true);//是否震动  默认为true
                config.setDecodeBarCode(false);//是否扫描条形码 默认为true
                config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为淡蓝色
                config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
                config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN);

                break;
            case R.id.btn_exit:
                if(popupWindow == null){
                    initPopup();
                }
                if(!mIsShowing){
                    params.alpha= 0.3f;
                    getWindow().setAttributes(params);
                    popupWindow.showAtLocation(findViewById(R.id.btn_exit), Gravity.BOTTOM,0,0);
                    mIsShowing = true;
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                btn_zx.setText("扫描结果为：" + content);
            }
        }
    }


    private void initPopup() {
        View pop = View.inflate(this, R.layout.item_pop, null);
        popupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        mIsShowing = false;
    }

}
