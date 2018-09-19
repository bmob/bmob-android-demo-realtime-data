package cn.bmob.rdt;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created on 18/9/19 09:29
 *
 * @author zhangchaozhou
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 数据监听是付费功能，请确保您appkey对应的应用已开通数据监听功能
         */
        Bmob.initialize(this, "f25fe6dad5bca9d0bb090072ea1e3c65");
    }
}
