package cn.niukid.myexampleapplication;

import android.util.Log;

import cn.niukid.myexampleapplication.repo.RepoHttpService;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * 所有创建http service的可添加至此模块中，用于依赖注入
 *
 * Created by bill on 8/21/17.
 */
//@Provides: 在modules中，我们定义的方法是用这个注解，以此来告诉Dagger我们想要构造对象并提供这些依赖。
@Module
public class HttpBizModule {


    @Provides
    protected RepoHttpService provideHttpBizService(Retrofit retrofit) {
        Log.d("HttpApiModule","provideHttpBizService...retrofit="+retrofit);
        return retrofit.create(RepoHttpService.class);
    }


}
