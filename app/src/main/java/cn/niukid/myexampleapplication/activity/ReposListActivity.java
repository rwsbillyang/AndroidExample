package cn.niukid.myexampleapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import cn.niukid.http.HttpManager;
import cn.niukid.http.IResultDataHandler;
import cn.niukid.myexampleapplication.HttpBizService;
import cn.niukid.myexampleapplication.R;
import cn.niukid.myexampleapplication.RoutePathConfig;
import cn.niukid.myexampleapplication.bean.Repo;

/**
 * Created by bill on 8/21/17.
 * http://www.cnblogs.com/zhuyp1015/p/5119727.html
 */

//必须给定group，否则编译出错,跳转时也需指定分组，否则找不到
@Route(path = RoutePathConfig.AROUTER_PATH_REPOLIST, group = RoutePathConfig.AROUTER_GROUP_APP)
public class ReposListActivity extends BaseActivity {

    @BindView(R.id.repos_rv_list)
    RecyclerView mRvList;

    @Inject
    HttpBizService httpBizService;

    //需在目标Activity中调用ARouter.getInstance().inject(this);才可实现注入
    @Autowired
    public String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);
        //ARouter.getInstance().inject(this);//父类中已调用
        initView();
    }

    @Override
    public int getLayoutId(){
        return R.layout.activity_repo_list;
    }



    private void initView(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvList.setLayoutManager(manager);

        ListAdapter adapter = new ListAdapter();
        mRvList.setAdapter(adapter);
        loadData(adapter);
    }



    private void loadData(final ListAdapter adapter)
    {
        //https://api.github.com/users/tom/repos
        HttpManager.get().with(this)
                .setObservableWithoutResultMapper(httpBizService.getRepoData(getUser())).setResultHandler(
               // (IResultDataHandler)(list)->{adapter.setRepos(list);}
                new IResultDataHandler<ArrayList<Repo>>()
                {
                    @Override public void handleResult(ArrayList<Repo> list)
                    {
                        adapter.setRepos(list);
                    }
                }
                );
    }


    private String getUser(){
        if(user==null||user.isEmpty()){
            Log.w("ReposListActivity","user is null, use default tom");
            return "tom";
        }
        return user;
    }


}
