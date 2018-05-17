package cn.niukid.myexampleapplication.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import cn.niukid.common.activity.BaseActivity;
import cn.niukid.common.application.GlideApp;
import cn.niukid.myexampleapplication.R;
import cn.niukid.myexampleapplication.RoutePathConfig;

/**
 * Created by bill on 8/22/17.
 */

@Route(path = RoutePathConfig.AROUTER_PATH_LOADIMAGE, group = RoutePathConfig.AROUTER_GROUP_APP)
public class ImageActivity  extends BaseActivity {

    @BindView(R.id.imageView) ImageView imageView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_load_image;
    }

    //需在目标Activity中调用ARouter.getInstance().inject(this);才可实现注入
    @Autowired
    public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //component().inject(this);
        //ARouter.getInstance().inject(this);//父类中已调用

        Logger.i("url="+url);


        //Glide requires a minimum API level of 10.
        // Glide will automatically clear the load and recycle any
        // resources used by the load when the Activity or Fragment you pass in to
        // Glide.with() is destroyed.
        //http://bumptech.github.io/glide/doc/getting-started.html
        //Glide.with(this).load(url).into(imageView);

        //The API is generated in the same package as the AppGlideModule and is named GlideApp by default.
        GlideApp.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .into(imageView);
    }

}
