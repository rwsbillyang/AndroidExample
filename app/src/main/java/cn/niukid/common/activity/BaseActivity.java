package cn.niukid.common.activity;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;

import butterknife.ButterKnife;
import cn.niukid.common.application.AppApplication;
import cn.niukid.common.application.AppComponent;

/**
 * 1.ButterKnife绑定，子类不再需绑定，直接使用ButterKnife；
 * 2. ARouter的依赖注入
 * 3.子类实现getLayoutId，不再需setContentView；
 * 4.返回AppComponent，不再需要对AppApplication进行调用，方便使用。
 *
 * Created by bill on 8/21/17.
 * */
public abstract class BaseActivity extends AppCompatActivity implements LifecycleOwner {

    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);


    /**
     * 凡以此类作为父类的Activity不再需在onCreate中进行如下的ButterKnife绑定，直接可在子类中进行绑定各种view
     * 和click事件
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        //http://jakewharton.github.io/butterknife/

        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
    }


    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    /**
     * 指定layoutId资源标识符后，不再需在onCreate中调用setContentView
     * */
    protected abstract int getLayoutId();

    /**
     * 子类的onCreate可直接调用component().inject(this);进行注入，从而实现依赖注入
     * */
    public AppComponent component() {
        return AppApplication.get().getAppComponent();
    }
}