package cn.niukid.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;


import io.reactivex.observers.DefaultObserver;


/**
 * Created by bill on 8/21/17.
 *
 * RxJava中backpressure：
 * http://www.dundunwen.com/article/275b1d92-f9da-4bb8-b111-3aa8a6ace245.html
 *
 * API reference:
 * http://reactivex.io/RxJava/2.x/javadoc/
 */

public  class HttpResultObserver<T> extends DefaultObserver<T> {
    private IResultDataHandler resultHandler;
    private WeakReference<Context> context;
    private ProgressDialog dialog;

    public HttpResultObserver(IResultDataHandler resultHandler, Context context)
    {
        this.resultHandler = resultHandler;
        this.context = new WeakReference<>(context);
        initProgressDialog();
    }

    /**
     * 自定义ProgressDialog提示文字
     * */
    public HttpResultObserver(IResultDataHandler resultHandler, Context context, String message)
    {
        this.resultHandler = resultHandler;
        this.context = new WeakReference<>(context);
        initProgressDialog(message);
    }
    /**
    * 自定义ProgressDialog
    * */
    public HttpResultObserver(IResultDataHandler resultHandler, Context context, ProgressDialog dialog)
    {
        this.resultHandler = resultHandler;
        this.context = new WeakReference<>(context);
        this.dialog = dialog;
    }

    /**
     * 取消请求
     * */
    public void cancelRequest()
    {
        cancel();
    }

    private void initProgressDialog() {
        Context context = this.context.get();
        if (dialog == null && context != null)
        {
            dialog = new ProgressDialog(context);
            dialog.setMessage("加载中……");
            dialog.setCancelable(false);
        }
    }

    private void initProgressDialog(String message)
    {
        Context context = this.context.get();
        if (dialog == null && context != null)
        {
            dialog = new ProgressDialog(context);
            dialog.setMessage(message);
            dialog.setCancelable(false);
        }
    }
    private void showProgressDialog()
    {
        Context context = this.context.get();
        if (dialog == null || context == null)
            return;
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
    private void dismissProgressDialog()
    {
        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
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
            Log.e("HttpResultObserver", "should not come here: "+e.getMessage());
            handleError(new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    @Override public void onNext(T t)
    {
        if (resultHandler != null)
        {
            resultHandler.handleResult(t);
        }
    }

    private void handleError(ExceptionHandle.ResponeThrowable e){
        Log.e("HttpResultObserver", e.message);

        Context context = this.context.get();
        if (context != null)
        Toast.makeText(context, e.message+"(code="+e.code+")", Toast.LENGTH_SHORT).show();
    }

}
