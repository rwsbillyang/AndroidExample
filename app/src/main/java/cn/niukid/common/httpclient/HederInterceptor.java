package cn.niukid.common.httpclient;

/**
 * Created by bill on 8/21/17.
 */

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
@Headers({
"Accept: application/vnd.github.v3.full+json",
"User-Agent: Retrofit-Sample-App"
})
 * */


public class HederInterceptor implements Interceptor {
    private Map<String, String> headers;
    public HederInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request()
                .newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey)).build();
            }
        }
        return chain.proceed(builder.build());

    }
}