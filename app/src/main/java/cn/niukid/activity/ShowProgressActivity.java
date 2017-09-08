package cn.niukid.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindString;
import cn.niukid.myexampleapplication.R;

import static org.greenrobot.eventbus.ThreadMode.MAIN;


/**
 * 用于loadData时的进度条显示控制。目前使用ProgressDialog实现
 * 有网络加载需要显示加载提示的Activity需要使用此类作为基类
 */

public abstract class ShowProgressActivity extends BaseActivity {
   // private final  static String TAG = "MyApp/ShowProgressActivity";



    //private boolean enableProgress = false;

    //private ProgressBar porgressBar;
    //TODO: use ProgressBar
    private ProgressDialog progressDialog;

    @BindString(R.string.loading)
    String message = "加载中...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);

        //子类在onCreate时就加载数据，就开始准备显示加载提示
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = MAIN)
    public  void onMessageEvent(MessageEvent event)
    {
        switch(event.type)
        {
            case MessageEvent.LOADING_START:
                showProgress();
                break;
            case MessageEvent.LOADING_FINISH:
                hideProgress();
                break;
            case MessageEvent.LOADING_ERROR:
                handleError(event.code,event.message);
                break;
            default:
                Logger.w("unknown EventBus event type "+event.type);
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 获取默认的提示文字
     * */
    public String getMessage() {
        return message;
    }
    /**
     * 指定自己的提示文字
     * */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * 获取默认的progressDialog
     * */
    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }
    /**
     * 指定自己的progressDialog
     * */
    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }



    public void showProgress(){
        if(progressDialog!=null&&!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }
    public void hideProgress(){
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
    }

    private void handleError(int code, String message){
        Logger.e( message);
        hideProgress();
        Toast.makeText(this, message+"(code="+code+")", Toast.LENGTH_SHORT).show();
    }


}
