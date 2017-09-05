package cn.niukid.myexampleapplication;

import android.util.Log;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by bill on 8/21/17.
 */
//@Provides: 在modules中，我们定义的方法是用这个注解，以此来告诉Dagger我们想要构造对象并提供这些依赖。

@Module
public class HttpBizModule {


    @Provides
    protected HttpBizService provideHttpBizService(Retrofit retrofit) {
        Log.d("HttpApiModule","provideHttpBizService...retrofit="+retrofit);
        return retrofit.create(HttpBizService.class);
    }


}
