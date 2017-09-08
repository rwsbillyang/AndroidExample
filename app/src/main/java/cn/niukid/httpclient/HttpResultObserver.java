package cn.niukid.httpclient;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import cn.niukid.activity.MessageEvent;
import io.reactivex.observers.DefaultObserver;


/**
 * RxJava2中的对结果进行处理的Observer，不支持backpressure
 *
 * RxJava2中backpressure：
 * http://www.dundunwen.com/article/275b1d92-f9da-4bb8-b111-3aa8a6ace245.html
 *
 * API reference:
 * http://reactivex.io/RxJava/2.x/javadoc/
 *
 *  Created by bill on 8/21/17.
 */

public  class HttpResultObserver<T> extends DefaultObserver<T> {

    private IResultCallback callback;

    public HttpResultObserver(IResultCallback callback) {
        this.callback = callback;
    }

    /**
     * 取消请求
     * */
    public void cancelRequest()
    {
        cancel();
    }

    private void showProgressDialog()
    {
        Logger.v("post event LOADING_START");
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOADING_START));
    }
    private void dismissProgressDialog()
    {
        Logger.v("post event LOADING_FINISH");
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOADING_FINISH));
    }

    @Override public void onStart()
    {
        showProgressDialog();
    }

    @Override public void onComplete()
    {
        dismissProgressDialog();
    }


    /**
     * 预处理无非就是当根据返回数据BaseResponse的isOk()方法判断为是否为true，
     * 若为true则正常处理，否则抛出异常让ExceptionHandle进一步处理，判断异常为何种异常。
     * */
    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        if(e instanceof ExceptionHandle.ResponeThrowable){
            handleError((ExceptionHandle.ResponeThrowable)e);
        } else {
            Logger.e("should not come here: "+e.getMessage());
            handleError(new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }
    /**
     * 调用callback进行结果处理
     * */
    @Override public void onNext(T t)
    {
        if (callback != null)
        {
            callback.handleResult(t);
        }
    }
    /**
     * 出错时的处理
     * */
    private void handleError(ExceptionHandle.ResponeThrowable e){
        EventBus.getDefault().post(new MessageEvent(e.code,e.message));
    }

}
