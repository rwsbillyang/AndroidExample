package cn.niukid.myexampleapplication.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.orhanobut.logger.Logger;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import cn.niukid.common.activity.BaseActivity;
import cn.niukid.myexampleapplication.R;
import cn.niukid.myexampleapplication.RoutePathConfig;

//Alt+Enter
@Route(path = RoutePathConfig.AROUTER_PATH_HOMEMAIN, group = RoutePathConfig.AROUTER_GROUP_APP)
public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    /*
    @BindString(R.string.title) String title;
    @BindDrawable(R.drawable.graphic) Drawable graphic;
    @BindColor(R.color.red) int red; // int or ColorStateList field
    @BindDimen(R.dimen.spacer) Float spacer; // int (for pixel size) or float (for exact value) field
    */

    @BindString(R.string.main_mock_data) String str;


    @OnClick({R.id.button,R.id.button2,R.id.button3,R.id.textView1,R.id.fab})
    public void onButtonClicked(View view)
    {

        switch (view.getId())
        {
            case R.id.button:
                //Intent intent = new Intent();
                //intent.setClass(MainActivity.this, ReposListActivity.class);
                //intent.setClassName(MainActivity.this,"cn.niukid.myexampleapplication.Repo.ReposListActivity");
                //startActivity(intent);

                ARouter.getInstance().build(RoutePathConfig.AROUTER_PATH_REPOLIST, RoutePathConfig.AROUTER_GROUP_APP)
                        //.withLong("key1", 666L)
                        .withString("user", "tom")  //需在目标Activity中调用ARouter.getInstance().inject(this);才可实现注入
                        //.withObject("key4", new Test("Jack", "Rose"))
                        .navigation();
                break;
            case R.id.button2:
                ARouter.getInstance().build(RoutePathConfig.AROUTER_PATH_LOADIMAGE, RoutePathConfig.AROUTER_GROUP_APP)
                        .withString("url", "https://gd1.alicdn.com/imgextra/i1/TB1YmxjRXXXXXafXpXXXXXXXXXX_!!0-item_pic.jpg_400x400.jpg")
                        .navigation();
                break;
            case R.id.button3:
                ARouter.getInstance().build(RoutePathConfig.AROUTER_PATH_GREENDAO_NOTE, RoutePathConfig.AROUTER_GROUP_APP)
                        .navigation();
                break;
            case R.id.textView1:
                Snackbar.make(view, "hello world", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;
            case R.id.fab:
                Snackbar.make(view, str, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
                break;
            default:
                Logger.w("unknown id: "+view.getId());
        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
