package cn.niukid.myexampleapplication.repo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by bill on 8/21/17.
 */

public interface RepoHttpService {
    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    //@Headers("Cache-Control: max-age=640000")
    @GET("/users/{user}/repos")
    Observable<List<Repo>> getRepos(@Path("user") String user);

    @Headers({HEADER_API_VERSION})
    @GET("/users/{username}") Observable<Response<User>> getUser(@Path("username") String username);

    @Headers({HEADER_API_VERSION})
    @GET("/users")
    Observable<List<User>> getUsers(@Query("since") int lastIdQueried, @Query("per_page") int perPage);

    //@Url复用原有base url，应该不会改变原base url.若改变的话，需要去掉Module中provideRetrofit的单例注解
    @GET("/users/{user}/repos")
    Observable<List<Repo>> getOtherSiteData(@Url String baseUrl, @Path("user") String user);
}
