package com.rock.micro.base.common.mongo;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

/**
 * 自定义的 Mongo operation 实体
 * 解决复杂语句专用
 *
 * @Author ayl
 * @Date 2022-09-28
 */
public class CustomAggOperation implements AggregationOperation {

    private String json;

    public CustomAggOperation(String json) {
        this.json = json;
    }

    @Override
    public Document toDocument(AggregationOperationContext aggregationOperationContext) {
        return aggregationOperationContext.getMappedObject(Document.parse(json));
    }

    /**
     * 使用该class的demo
     *
     * @param args
     */
    public static void main(String[] args) {
        //聚合搜索
        Aggregation aggregation = Aggregation.newAggregation(
                //如果不能用代码拼出来,就这么使用写
                new CustomAggOperation("{$lookup:{from:'uni.task',let:{preTaskId:'$preTaskId'},pipeline:[{'$match':{'state':{$in:['finished','termination']}}},{'$project':{id:1,state:1}},{$match:{$expr:{$and:[{$eq:['$_id','$$preTaskId']}]}}}],as:'preTask'}}")
        );
    }

}
