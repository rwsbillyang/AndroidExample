package cn.niukid.common.httpclient;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by bill on 8/21/17.
 */

public interface BaseHttpService<T> {
    @GET("{url}")
    Observable<ResultModel<T>> executeGet(
            @Path("url") String url,
            @QueryMap Map<String, String> maps
    );
    @POST("{url}")
    Observable<ResultModel<T>> executePost(
            @Path("url") String url,
            @QueryMap Map<String, String> maps);

    @POST("{url}")
    Observable<ResponseBody> json(
            @Path("url") String url,
            @Body RequestBody jsonStr);

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> upLoadFile(
            @Path("url") String url,
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody);

    @POST("{url}")
    Observable<ResponseBody> uploadFiles(
            @Path("url") String url,
            @Part("userName") String description,
            @PartMap() Map<String, RequestBody> maps);
}
