package com.bwie.download2;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/*
http://gdown.baidu.com/data/wisegame/df65a597122796a4/weixin_821.apk
*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Boolean isDowloding = false;
    HttpHandler handler;
    private ProgressBar pb;
    private TextView tv_error;
    private TextView tv_progress;
    private Button btn_down, btn_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化
        pb = (ProgressBar) findViewById(R.id.pb);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_error = (TextView) findViewById(R.id.tv_failure);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        // 开始点击事件
        btn_down.setOnClickListener(this);
        // 暂停点击事件
        btn_stop.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String fileName = "weixin_821.apk";
        switch (view.getId()) {
            case R.id.btn_down:

                btn_stop.setEnabled(true);
                btn_down.setEnabled(false);

                String path = "http://gdown.baidu.com/data/wisegame/df65a597122796a4/" + fileName;
                HttpUtils http = new HttpUtils();
                handler = http.download(path, Environment.getExternalStorageDirectory() + "/"
                        + fileName, true, true, new RequestCallBack<File>() {

                    @Override
                    public void onSuccess(ResponseInfo<File> arg0) {
                        isDowloding = false;
                        // 下载成功
                        Toast.makeText(MainActivity.this, arg0.result.getPath(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // 下载失败
                        tv_error.setText(arg1);
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                        if (current < total) {
                            isDowloding = true;
                        } else {
                            isDowloding = false;
                        }
                        // 下载任务
                        pb.setMax((int) total);
                        pb.setProgress((int) current);
                        tv_progress.setText(current * 100 / total + "%");
                    }

                });

                break;
            case R.id.btn_stop:
                btn_stop.setEnabled(false);
                btn_down.setEnabled(true);
                if (isDowloding) {
                    if (handler != null) {
                        handler.cancel();
                    }
                }
                break;
        }
    }
}
