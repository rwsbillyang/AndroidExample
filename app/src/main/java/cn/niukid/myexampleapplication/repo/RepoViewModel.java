package cn.niukid.myexampleapplication.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import cn.niukid.myexampleapplication.GlobalConfig;
import cn.niukid.common.application.AppApplication;
import cn.niukid.common.httpclient.IResultCallback;
import cn.niukid.common.httpclient.RxWrapper;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by bill on 9/5/17.
 */

public class RepoViewModel extends ViewModel {

    private final RepoCacheProviders cacheProviders;

    @Inject
    public RepoHttpService httpRepoService;

    private MutableLiveData<List<Repo>> liveData = null;

    public RepoViewModel() {
        AppApplication.get().getAppComponent().inject(this);
        //缓存地址
        String dir = AppApplication.get().getCacheDir() +"/"+ GlobalConfig.RX_CACHE_PATH + "/repo";

        Logger.i("dir=" + dir);
        File cacheDirectory = new File(dir);
        if (!cacheDirectory.exists())
            cacheDirectory.mkdirs();
        cacheProviders = new RxCache.Builder()
                .persistence(cacheDirectory, new GsonSpeaker())
                .using(RepoCacheProviders.class);
    }

    public LiveData<List<Repo>> getRepoList(@NonNull String userName, final boolean update) {
        if (liveData == null) {
            liveData = new MutableLiveData<List<Repo>>();
            //TODO:大量的结构化数据可以使用GreenDao

            //https://api.github.com/users/tom/repos
            //以userName为DynamicKey,如果update为true,将会重新获取数据并清理缓存。
            RxWrapper
                    .get().setObservable(
                            cacheProviders.getRepos(
                                    httpRepoService.getRepos(userName),
                                    new DynamicKey(userName),
                                    new EvictDynamicKey(update)))
                    .subscribe(
                            // (IResultCallback)(list)->{adapter.setRepos(list);}
                            new IResultCallback<List<Repo>>() {
                                @Override
                                public void handleResult(List<Repo> list) {
                                    liveData.setValue(list);
                                    //TODO:大量的结构化数据可以使用GreenDao保存
                                }
                            }
                    );
        }

        return liveData;

    }

}

