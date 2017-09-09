package cn.niukid.myexampleapplication.repo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import cn.niukid.activity.ShowProgressActivity;
import cn.niukid.myexampleapplication.R;
import cn.niukid.myexampleapplication.RoutePathConfig;

/**
 * Created by bill on 8/21/17.
 * http://www.cnblogs.com/zhuyp1015/p/5119727.html
 */

//必须给定group，否则编译出错,跳转时也需指定分组，否则找不到
@Route(path = RoutePathConfig.AROUTER_PATH_REPOLIST, group = RoutePathConfig.AROUTER_GROUP_APP)
public class ReposListActivity extends ShowProgressActivity {

    @BindView(R.id.repos_rv_list)
    RecyclerView recyclerView;


    //需在目标Activity中调用ARouter.getInstance().inject(this);才可实现注入
    @Autowired
    public String user;


    @Override
    public int getLayoutId(){
        return R.layout.activity_repo_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ARouter.getInstance().inject(this);//父类中已调用


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        final RepoListAdapter  adapter = new RepoListAdapter();
        recyclerView.setAdapter(adapter);


        RepoViewModel viewModel = ViewModelProviders.of(this).get(RepoViewModel.class);

        viewModel.getRepoList(getUser(),true).observe(this, new Observer<List<Repo>>(){
            @Override
            public void onChanged(@Nullable List<Repo> repos) {
                if(repos==null||repos.size()==0)
                {
                    Toast.makeText(ReposListActivity.this,"没有数据了",Toast.LENGTH_LONG).show();
                }else
                    adapter.setList(repos);
            }
        } );
    }



    private String getUser(){
        if(user==null||user.isEmpty()){
            Logger.w("user is null, use default tom");
            return "tom";
        }
        return user;
    }


}
