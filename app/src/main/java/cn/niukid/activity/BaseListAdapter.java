package cn.niukid.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * 简化RecyclerView.Adapter的使用
 * Created by bill on 9/9/17.
 */

public abstract class BaseListAdapter<ItemType> extends RecyclerView.Adapter<BaseListAdapter.BaseViewHolder<ItemType>> {


    private List<ItemType> list;

    public BaseListAdapter() {
        list = new ArrayList<>();
    }

    public void setList(List<ItemType> list) {
        if(list==null||list.size()==0)
        {
            Logger.w("the list is null or empty");
        }else
        {
            this.list = list;
            notifyItemInserted(this.list.size() - 1);
        }
    }

    /**
     * item的layout：R.layout.item_xxx
     * */
    public abstract int getItemLayout();

    /**
     * 使用new XXXViewHolder();
     * */
    public abstract BaseViewHolder createViewHolder(View itemView);

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(getItemLayout(), parent, false);
        return createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindTo(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public  abstract static class BaseViewHolder<ItemType> extends RecyclerView.ViewHolder{
        /*
        @BindView(R.id.item_iv_repo_name)
        TextView mIvRepoName;
        @BindView(R.id.item_iv_repo_detail)
        TextView mIvRepoDetail;
        */
        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public abstract void bindTo(ItemType item);
        /*
        {
            // mIvRepoName.setText(repo.name );
            // mIvRepoDetail.setText(String.valueOf(repo.description + "(" + repo.language + ")"));
        }
        */
    }

}
