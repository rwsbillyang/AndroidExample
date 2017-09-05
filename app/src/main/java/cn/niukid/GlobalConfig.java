package cn.niukid;

/**
 * Created by bill on 8/21/17.
 */

public class GlobalConfig {
    public final static String BASE_URL = "https://api.github.com";
    public final static boolean enableArouterDebug = true;
    public final static boolean enableOkHttpDebugMode = true;
    public final static boolean enableOkHttpGzipRequest = false;
    public final static boolean enableMODERN_TLS = false;

    /**自定义证书*/
    public final static boolean enableCustomTrust = false;
    /**自定义证书文件，放在res/raw目录下，Java本身支持的证书格式jks，
     * 但是遗憾的是在android当中并不支持jks格式正式，而是需要bks格式的证书。
     * 因此我们需要将jks证书转换成bks格式证书
     * */
    public final static int[] CERTIFICATES = new int[]{/*R.raw.media*/};

    public final static long CACHE_SIZE = 10*1024*1024;
    public final static String CACHE_PATH = "appCache";
    public final static int OKHTTP_CONNECT_TIMEOUT = 60 * 1000; //ms
    public final static int OKHTTP_READ_TIMEOUT = 100 * 1000;//ms
    public final static int OKHTTP_WRITE_TIMEOUT = 60 * 1000;//ms



}
