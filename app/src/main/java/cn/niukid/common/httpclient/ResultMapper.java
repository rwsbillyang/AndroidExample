package cn.niukid.common.httpclient;

/**
 * Created by bill on 8/21/17.
 */

import com.orhanobut.logger.Logger;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by bill on 8/21/17.
 *
 * 将数据类型从ResultModel<T> 转化为T，即从http请求结果ResultModel<T> 中提取T
 */

public  class ResultMapper<T> implements Function<ResultModel<T>,T>
{
    @Override
    public T apply(@NonNull ResultModel<T> resultModel) throws Exception {
        if(resultModel.isOk())
        {
            return resultModel.data;
        }
        else
        {
            String msg="请求失败(code=" + resultModel.status + ",message=" + resultModel.message+ ")";
            Logger.w(msg);
            throw new RuntimeException(msg);
        }
    }
}
