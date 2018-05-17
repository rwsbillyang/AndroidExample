package cn.niukid.common.httpclient;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import cn.niukid.common.activity.MessageEvent;
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

public  class HttpResultHandler<T> extends DefaultObserver<T> {

    private IResultCallback callback;

    public HttpResultHandler(IResultCallback callback) {
        this.callback = callback;
    }


    /**
     * 开始时只显示动态条
     * */
    @Override public void onStart()
    {
        showProgressDialog();
    }

    /**
     * 结束请求后关闭动态条
     * */
    @Override public void onComplete()
    {
        dismissProgressDialog();
    }



    /**
     * 调用callback进行结果处理
     * */
    @Override public void onNext(T t)
    {
        if (callback != null)
        {
            Logger.i("to handle the response result...");
            callback.handleResult(t);
        }else
        {
            Logger.w("no callback to handle result");
        }
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
     * 取消请求
     * */
    public void cancelRequest()
    {
        cancel();
    }


    /**
     * 出错时的处理
     * */
    private void handleError(ExceptionHandle.ResponeThrowable e){
        Logger.w("handleError");
        EventBus.getDefault().post(new MessageEvent(e.code,e.message));
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


}
