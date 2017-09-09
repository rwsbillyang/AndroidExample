package cn.niukid.myexampleapplication.repo;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import cn.niukid.activity.BaseListAdapter;
import cn.niukid.myexampleapplication.R;

/**
 * Created by bill on 9/9/17.
 */

public class RepoListAdapter extends BaseListAdapter<Repo> {

    @Override
    public int getItemLayout() {
        return R.layout.item_repo;
    }

    @Override
    public BaseViewHolder createViewHolder(View itemView) {
        return new RepoViewHolder(itemView);
    }


    public  static class RepoViewHolder extends BaseListAdapter.BaseViewHolder<Repo>{

        @BindView(R.id.item_iv_repo_name)
        TextView repoName;
        @BindView(R.id.item_iv_repo_detail)
        TextView repoDetail;

        public RepoViewHolder(View itemView) {
            super(itemView);
        }


        @Override
        public void bindTo(Repo item) {
            repoName.setText(item.name );
            repoDetail.setText(String.valueOf(item.description + "(" + item.language + ")"));
        }

    }
}
