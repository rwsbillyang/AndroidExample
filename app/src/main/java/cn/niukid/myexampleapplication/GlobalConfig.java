package cn.niukid.myexampleapplication;

/**
 * Created by bill on 8/21/17.
 */

public class GlobalConfig {
    public  static String BASE_URL = "https://api.github.com";
    public  static boolean enableArouterDebug = true;
    public  static boolean enableOkHttpDebugMode = true;
    public  static boolean enableOkHttpGzipRequest = true;
    public  static boolean enableMODERN_TLS = true;//Android5.0版本以上 否则https报错

    /**自定义证书*/
    public  static boolean enableCustomTrust = false;
    /**自定义证书文件，放在res/raw目录下，Java本身支持的证书格式jks，
     * 但是遗憾的是在android当中并不支持jks格式正式，而是需要bks格式的证书。
     * 因此我们需要将jks证书转换成bks格式证书
     * */
    public  static int[] CERTIFICATES = new int[]{/*R.raw.media*/};

    public  static long CACHE_SIZE = 10*1024*1024;
    public  static String HTTP_CACHE_PATH = "http";
    public  static int HTTP_CACHE_TIME = 60;
    public  static String RX_CACHE_PATH = "rx";
    public  static int OKHTTP_CONNECT_TIMEOUT = 60 * 1000; //ms
    public  static int OKHTTP_READ_TIMEOUT = 100 * 1000;//ms
    public  static int OKHTTP_WRITE_TIMEOUT = 60 * 1000;//ms



}
