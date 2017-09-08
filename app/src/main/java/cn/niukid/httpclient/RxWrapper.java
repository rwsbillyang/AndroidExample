package cn.niukid.httpclient;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by bill on 8/21/17.
 */

public class RxWrapper {

    private Observable observable;
    private HttpResultObserver subscriber;

    private volatile static RxWrapper singleton;
    private RxWrapper() { }
    public static RxWrapper get()
    {
        if (singleton == null)
        {
            synchronized (RxWrapper.class)
            {
                if (singleton == null)
                {
                    singleton = new RxWrapper();
                }
            }
        }
        return singleton;
    }

    /**
     * 默认结果变换函数，默认结果为 ResultModel<T> 类型，变换成T类型
     * */
    public RxWrapper setObservable(Observable observable)
    {
        this.observable = observable.compose(schedulersTransformer)
                .map(new ResultMapper()).onErrorResumeNext(new ThrowableToObserverSource());
        return singleton;
    }

    /**
     * 不需结果变换函数，请求返回的结果直接是所需的数据
     * */
    public RxWrapper setObservableWithoutResultMapper(Observable observable)
    {
        this.observable = observable.compose(schedulersTransformer)
                .onErrorResumeNext(new ThrowableToObserverSource());
        return singleton;
    }

    /**
     * 自定义结果变换函数。当返回结果数据格式不一，ResultModel<T> 不满足时，需自定义转换函数
     * */
    public RxWrapper setObservableWihtCustomResultMapper(Observable observable, Function trFunction)
    {
        this.observable = observable.compose(schedulersTransformer)
                .map(trFunction).onErrorResumeNext(new ThrowableToObserverSource());
        return singleton;
    }
    //处理线程调度的变换
    ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return ((Observable) upstream).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    /**
     * 在上面的几行代码中map(new ResultMapper())将结果转化为T时，若发生异常，将导致原观察者Observer（即HttpResultObserver）
     * 的onError执行。现在需区分判断各种异常，给出不同的提示信息，而这种判断处理在ExceptionHandle.handleException(throwable)
     * 中，故map调用后再调用onErrorResumeNext，将原Throwable转化为新的ObserverSource（见下面的函数）：将原发
     * 生错误时的Throwable经由ExceptionHandle.handleException(throwable)处理后抛出新的异常信息，从而成为新的源，原观察者
     * HttpResultObserver的onError将在这时被调用，并对其进行进一步处理，并最终即HttpResultObserver.handleError进行Toast显示处理。
     *
     * 若不采用ExceptionHandle和onErrorResumeNext再发射异常进行处理，则需将ExceptionHandle中对异常的处理挪到HttpResultObserver.onError
     * 中。此处只是拦截了map后的异常将其处理变换后，再发射出来而已。
     * 参见：http://www.jianshu.com/p/9c3f0af1180d
     * */
    public static class ThrowableToObserverSource<T> implements Function<Throwable, Observable<T>>
    {
        @Override public Observable<T> apply(Throwable throwable) throws Exception
        {
            return Observable.error(ExceptionHandle.handleException(throwable));
        }
    }


    /**
     * 为subscriber指定数据处理Listener
     * */
    public void subscribe(IResultCallback resultHandler)
    {
        subscriber = new HttpResultObserver(resultHandler);
        observable.subscribe(subscriber);
    }


    /**
     * 取消请求
     */
    public void cancelRequest()
    {
        subscriber.cancelRequest();
    }


}
