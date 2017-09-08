package cn.niukid.httpclient;

/**
 * Created by bill on 8/21/17.
 */

import android.app.Application;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import cn.niukid.GlobalConfig;
import cn.niukid.utils.NetworkUtil;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class CaheInterceptor implements Interceptor {
    //private final static String TAG="MyApp/CaheInterceptor";
    private Application application;


    public CaheInterceptor(Application application)
    {
        this.application = application;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetworkUtil.isNetworkAvailable(application)) {
            Response response = chain.proceed(request);
            // read from cache for 60 s

            String cacheControl = request.cacheControl().toString();
            Logger.i( "60s load cache" + cacheControl);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + GlobalConfig.HTTP_CACHE_TIME)
                    .build();
        } else {
            Logger.w(" no network, load from http cache");
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response response = chain.proceed(request);
            //set cahe times is 3 days
            int maxStale = 60 * 60 * 24 * 3;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    }
}
