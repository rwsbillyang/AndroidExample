package cn.niukid.httpclient;


import android.app.Application;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import cn.niukid.GlobalConfig;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by bill on 8/21/17.
 */


@Module
public class HttpClientModule {

    private Cache cache = null;
    private File httpCacheDirectory = null;

    /**
     * 将拥有一个全局的OkHttpClient实例
     * */
    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(Application application) {
        Logger.d("provideOkHttpClient...");
        //缓存地址
        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(application.getCacheDir(), GlobalConfig.HTTP_CACHE_PATH);
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, GlobalConfig.CACHE_SIZE);
            }
        } catch (Exception e) {
            Logger.e( "Could not create http cache");
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(GlobalConfig.OKHTTP_READ_TIMEOUT,TimeUnit.MILLISECONDS)//设置读取超时时间
                .writeTimeout(GlobalConfig.OKHTTP_WRITE_TIMEOUT,TimeUnit.MILLISECONDS)//设置写的超时时间
                .connectTimeout(GlobalConfig.OKHTTP_CONNECT_TIMEOUT,TimeUnit.MILLISECONDS)//设置连接超时时间
                .retryOnConnectionFailure(true)
                .cache(cache)
                //.addInterceptor(new HederInterceptor(headers))
                .addInterceptor(new CaheInterceptor(application))
                .addNetworkInterceptor(new CaheInterceptor(application));

        //OkHttp's interceptors require OkHttp 2.2 or better.
        /// Unfortunately, interceptors do not work with OkUrlFactory,
        /// or the libraries that build on it, including Retrofit ≤ 1.8 and Picasso ≤ 2.4.
        //compress request
        if (GlobalConfig.enableOkHttpGzipRequest){
            GzipRequestInterceptor interceptor = new GzipRequestInterceptor();
            builder.addInterceptor(interceptor);
        }

        //debug模式打印网络请求日志
        if (GlobalConfig.enableOkHttpDebugMode){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        //http://www.jianshu.com/p/5ebcd282ea56
        //Android5.0版本以上以及一个类似先进网路服务器
        if (GlobalConfig.enableMODERN_TLS){
            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .cipherSuites(
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                    ).build();
            builder.connectionSpecs(Collections.singletonList(spec));
        }
        if (GlobalConfig.enableCustomTrust){
            builder.socketFactory(getSSLSocketFactory(application, GlobalConfig.CERTIFICATES));
        }

        return  builder.build();
    }



    /**
     * 由@Provides 提供的方法需要输入参数,需要其它用@Provides注解修饰的方法生成
     * */
    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient){
        Log.d(TAG,"provideRetrofit...okHttpClient="+okHttpClient);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(okHttpClient)
                .build();
        return retrofit;
    }



    /** Dangerous interceptor that rewrites the server's cache-control header. */
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", "max-age=60")
                    .build();
        }
    };

    /**
     * 比如现在我们有个证书media.bks，首先需要将其放在res/raw目录下，当然你可以可以放在assets目录下。
     * Java本身支持的证书格式jks，但是遗憾的是在android当中并不支持jks格式正式，而是需要bks格式的证书。
     * 因此我们需要将jks证书转换成bks格式证书
     *
     * http://blog.csdn.net/dd864140130/article/details/52625666
     * */
    protected  SSLSocketFactory getSSLSocketFactory(Application application, int[] certificates) {


        //CertificateFactory用来证书生成
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            //Create a KeyStore containing our trusted CAs
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                //读取本地证书
                InputStream is = application.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(is));

                if (is != null) {
                    is.close();
                }
            }
            //Create a TrustManager that trusts the CAs in our keyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }
}
