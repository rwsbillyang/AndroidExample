package cn.niukid.myexampleapplication.repo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;

/**
 * Created by bill on 9/7/17.
 */

interface RepoCacheProviders {
    @LifeCache(duration = 20, timeUnit = TimeUnit.HOURS)
    Observable<List<Repo>> getRepos(Observable<List<Repo>> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);
}
