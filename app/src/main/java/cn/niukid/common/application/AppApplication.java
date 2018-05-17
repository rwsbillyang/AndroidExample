package cn.niukid.common.application;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import org.greenrobot.greendao.database.Database;

import cn.niukid.myexampleapplication.GlobalConfig;
import cn.niukid.common.httpclient.HttpClientModule;
import cn.niukid.myexampleapplication.BuildConfig;
import cn.niukid.myexampleapplication.HttpBizModule;
import cn.niukid.myexampleapplication.note.DaoMaster;
import cn.niukid.myexampleapplication.note.DaoSession;


/**
 * 1.创建application时，需同时将其更新到manifest.xml中的标签<application></application>中：
 * android:name="cn.niukid.common.application.AppApplication"
 * 2.添加各种权限，如：
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
 <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"></uses-permission>
 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" ></uses-permission>
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" ></uses-permission>
 <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"></uses-permission>

 * 3.创建新的Activity时，需向manifest.xml添加activity标签
 *
 *  Created by bill on 8/21/17.
 */

public class AppApplication extends Application {
    //private final static String TAG="MyApp/AppApplication";

    private  AppComponent appComponent;

    private static AppApplication instance;
    public static AppApplication get(){return instance;}
    public  AppComponent getAppComponent(){
        return appComponent;
    }




    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;

        setupLog();
        Logger.i("create AppApplication....");

        setupArouter();
        setupComponent();
        setupGreenDao();
    }



    /**
     * ARouter CheckList:
     * 1.此处配置及初始化
     * 2.为各Activity添加@Route注解，指定path和group属性值，两种属性值建议提取到RoutePathConfig类中；
     * 3.在需要跳转的地方，使用 ARouter.getInstance().build(thePath, theGroup).withXXX(theKey,theValue)navigation();进行跳转
     * 若需传递参数，更多参见ARouter官方文档：
     * 4.若目标类Activity中需要使用@Autowired注解绑定接收的参数值，则需在onCreate中调用ARouter.getInstance().inject(this);进行注入；
     *  若继承自BaseActivity父类，因已调用，子类中可忽略，不再调用
     *
     *  更多用法，参见官方文档
     *
     *  Conclusion: 故在添加新的Activity时，只需上述2和3两步，即为Activity添加@Route注解,而注解的属性值放在RoutePathConfig
     *
     * */
    private void setupArouter()
    {
        if (GlobalConfig.enableArouterDebug) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }


    /**
    * Dagger2 CheckList：
    * 1.此处创建component，以及各Module，并指定给component
    * 2.component是一个接口，使用@Component注解，注解中指定了各种Module类；并提供成员方法inject（XXX xxx），
    * 其中XXX是使用对象的类，如各种Activity或Fragment，比如这些类注入了下面第三条中的Retrofit的http service，则
    * 需在Component接口中声明inject（XXX xxx）;
    * 3.各Module类使用@Module注解，并提供各种创建第三方库中的类或接口示例的方法，并使用@Provide注解,如创建Retrofit,
    * OKHttpClient,Retrofit提供http业务访问的service等；
    * 4.在第二条中输入了依赖对象的类中，在其onCreate中调用AppApplication.getAppComponent().inject(this);从而注入对象；
     *
     * Conclusion:添加新的Module和Service时，需从步骤1走到步骤4
    * */
    private void setupComponent()
    {
        if(appComponent==null)
            appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .httpClientModule(new HttpClientModule())
                .httpBizModule(new HttpBizModule())
                .build();
        else
            Logger.w("setupComponent again");
    }

    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;
    public DaoSession getDaoSession() {
        return daoSession;
    }
    /**
     * http://greenrobot.org/greendao/documentation/
     *  Now make the project, for example by using Build > Make project in Android Studio.
     *  This triggers greenDAO to generate DAO classes, like NoteDao.java, that will help us add notes to a database.
     *  此处的DaoSession和DaoMaster等需要Build > Make project 后才会生成，然后再import class，将DevOpenHelper导入
     * */
    private void setupGreenDao()
    {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }


    protected void setupLog()
    {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(1)         // (Optional) How many method line to show. Default 2
                //.methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("MyApp")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        FormatStrategy csvFormatStrategy = CsvFormatStrategy.newBuilder()
                .tag("MyApp")
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(csvFormatStrategy){
            @Override public boolean isLoggable(int priority, String tag) {
                return priority>=Logger.WARN;
            }
        });

    }

}
