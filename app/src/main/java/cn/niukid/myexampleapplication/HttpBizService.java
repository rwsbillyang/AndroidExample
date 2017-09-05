package cn.niukid.myexampleapplication;

import java.util.ArrayList;

import cn.niukid.myexampleapplication.bean.Repo;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by bill on 8/21/17.
 */

public interface HttpBizService {

    //@Headers("Cache-Control: max-age=640000")
    @GET("/users/{user}/repos")
    Observable<ArrayList<Repo>> getRepoData(@Path("user") String user);

    //@Url复用原有base url，应该不会改变原base url.若改变的话，需要去掉Module中provideRetrofit的单例注解
    @GET("/users/{user}/repos")
    Observable<ArrayList<Repo>> getOtherSiteData(@Url String baseUrl, @Path("user") String user);
}
