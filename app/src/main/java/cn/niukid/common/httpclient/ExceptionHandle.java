package cn.niukid.common.httpclient;

/**
 * Created by bill on 8/21/17.
 */

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.util.List;

import cn.niukid.common.application.AppApplication;
import cn.niukid.common.utils.NetworkUtil;
import io.reactivex.exceptions.CompositeException;
import retrofit2.HttpException;


public class ExceptionHandle {
    //private final static String TAG="MyApp/ExceptionHandle";
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    /**
     * 约定异常
     */
    class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;

        /**
         *
         */
        public static final int NETWORK_UNAVAILABLE_ERROR = 1007;
    }

    public static class ResponeThrowable extends Exception {
        public int code;
        public String message;

        public ResponeThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;

        }
    }

    public static ResponeThrowable handleException(Throwable e) {
        ResponeThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponeThrowable(e, httpException.code());
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex.message = "未授权访问";
                    break;
                case FORBIDDEN:
                    ex.message = "禁止访问";
                    break;
                case NOT_FOUND:
                    ex.message = "未找到";
                    break;
                case REQUEST_TIMEOUT:
                    ex.message = "请求超时";
                    break;
                case GATEWAY_TIMEOUT:
                    ex.message = "网关超时";
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.message = "内部错误";
                    break;
                case BAD_GATEWAY:
                    ex.message = "网关错误";
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.message = "服务不可用";
                    break;
                default:
                    ex.message = "网络错误";
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponeThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponeThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponeThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = "连接失败，请检查网络";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponeThrowable(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException){
            ex = new ResponeThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "连接超时，请检查网络";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponeThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "套接字超时，请检查网络";
            return ex;
        }else if(e instanceof io.reactivex.exceptions.CompositeException)
        {
            List<Throwable> list=  ((CompositeException) e).getExceptions();
            for(Throwable t:list)
            {
                Logger.w("message:"+t.getMessage());
                //t.printStackTrace();
            }
            ex = new ResponeThrowable(e, ERROR.UNKNOWN);
            ex.message = "请求失败";
            return ex;
        }else{
            if (NetworkUtil.isNetworkAvailable(AppApplication.get()))
            {
                ex = new ResponeThrowable(e, ERROR.UNKNOWN);
                ex.message = "未知错误:"+ e.getClass().getName();
            }else
            {
                ex = new ResponeThrowable(e, ERROR.NETWORK_UNAVAILABLE_ERROR);
                ex.message = "网络不可用，请检查";
            }

            Logger.e("unknown exception: "+e.getClass().getName()+","+ e.getMessage());
            //e.printStackTrace();
            return ex;
        }
    }




    public class ServerException extends RuntimeException {
        public int code;
        public String message;
    }
}
