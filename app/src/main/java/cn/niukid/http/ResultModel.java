package cn.niukid.http;

import java.net.HttpURLConnection;

/**
 * Created by bill on 8/21/17.
 */

public class ResultModel<T> {
    public int status;//200成功
    public String message;
    public T data;

    public boolean isOk() {
        return status == HttpURLConnection.HTTP_OK;
    }
}
