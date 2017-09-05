package cn.niukid.application;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bill on 8/21/17.
 */

@Module
public class AppModule {
    private Application application;

    public AppModule(Application application){
        this.application=application;
    }

    @Provides
    public Application provideApplication(){
        return application;
    }
}
