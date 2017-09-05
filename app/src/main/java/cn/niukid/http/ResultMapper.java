package cn.niukid.http;

/**
 * Created by bill on 8/21/17.
 */

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
            throw new RuntimeException("请求失败(code=" + resultModel.status + ",message=" + resultModel.message+ ")");
        }
    }
}
