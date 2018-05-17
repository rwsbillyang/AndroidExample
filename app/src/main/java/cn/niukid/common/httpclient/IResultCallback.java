package cn.niukid.common.httpclient;

/**
 * Created by bill on 8/21/17.
 *
 * RxJava2最后将调用此callback对Retrofit的请求结果进行处理
 */

public interface IResultCallback<T> {
    void handleResult(T t);
}
