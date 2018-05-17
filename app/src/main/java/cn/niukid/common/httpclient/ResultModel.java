package cn.niukid.common.httpclient;

import java.net.HttpURLConnection;

/**
 * Created by bill on 8/21/17.
 * 需要使用RxWrapper setObservableUsingResultMapper(Observable observable) 指定源
 */

public class ResultModel<T> {
    public int status;//200成功
    public String message;
    public T data;//实际的返回数据结果

    public boolean isOk() {
        return status == HttpURLConnection.HTTP_OK;
    }
}
