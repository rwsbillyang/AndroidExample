package cn.niukid.application;


import javax.inject.Singleton;

import cn.niukid.http.HttpApiModule;
import cn.niukid.myexampleapplication.HttpBizModule;
import cn.niukid.myexampleapplication.activity.ReposListActivity;
import dagger.Component;

/**
 * Created by bill on 8/21/17.
 *
 * 需要创建的Module都需添加在此处，并且还需在AppApplication中new出来，并赋给Dagger；
 * Module中需要用@Provide注解第三方库的对象创建方法，Module中创建的各种Http请求Service接口，可在各Activity中
 * 通过@Inject注解直接使用，AppComponent中的inject方法的参数则表明了要注入的位置
 */
@Singleton
@Component(modules = { AppModule.class,HttpApiModule.class, HttpBizModule.class})
public interface AppComponent {
    /**
     * 参数表示要注入的位置
     * */
    void inject(ReposListActivity activity);
}
