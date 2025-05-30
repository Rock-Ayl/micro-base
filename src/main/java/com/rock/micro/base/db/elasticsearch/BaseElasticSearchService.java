package com.rock.micro.base.db.elasticsearch;

import com.rock.micro.base.data.BaseIndex;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.elasticsearch.core.AggregationsContainer;

import java.util.Collection;
import java.util.List;

/**
 * elastic search 服务基底
 *
 * @Author ayl
 * @Date 2022-3-9
 */
public interface BaseElasticSearchService<T extends BaseIndex> {

    /**
     * 单个创建
     *
     * @param index
     * @return
     */
    T create(T index);

    /**
     * 批量创建
     *
     * @param indexList
     * @return
     */
    Collection<T> create(List<T> indexList);

    /**
     * 根据id查询单个
     *
     * @param clazz
     * @param id
     * @return
     */
    T get(Class<T> clazz, String id);

    /**
     * 根据id列表查询多个
     *
     * @param clazz
     * @param idList id列表
     * @return
     */
    List<T> list(Class<T> clazz, List<String> idList);

    /**
     * 根据条件查询多个
     *
     * @param clazz
     * @param query 限制条件
     * @return
     */
    List<T> list(Class<T> clazz, QueryBuilder query);

    /**
     * 根据id真实删除
     *
     * @param id
     * @param clazz
     * @return
     */
    void delete(Class<T> clazz, String id);

    /**
     * 根据实体更新单个实体,跳过NULL的字段
     *
     * @param index
     */
    void updateSkipNull(T index);

    /**
     * 根据多个实体批量更新实体,跳过NULL的字段
     *
     * @param indexList
     */
    void batchUpdateSkipNull(List<T> indexList);

    @Getter
    @Setter
    @ApiModel("ES查询响应对象实体")
    class RollPageResult<T> {

        @ApiModelProperty("总数")
        private long total;

        @ApiModelProperty("数据列表")
        private List<T> list;

        @ApiModelProperty("聚合搜索内容(有待更新)")
        private AggregationsContainer aggregations;

    }

    /**
     * 条件分页查询
     *
     * @param clazz    实体类
     * @param query    条件对象
     * @param pageNum  分页-页码(可以为空)
     * @param pageSize 分页-数量(可以为空)
     * @return
     */
    RollPageResult<T> rollPage(Class<T> clazz, QueryBuilder query, Integer pageNum, Integer pageSize);

    /**
     * 条件分页查询
     *
     * @param clazz                      实体类
     * @param query                      条件对象
     * @param abstractAggregationBuilder 聚合条件(可以为空)
     * @param pageNum                    分页-页码(可以为空)
     * @param pageSize                   分页-数量(可以为空)
     * @return
     */
    RollPageResult<T> rollPage(Class<T> clazz, QueryBuilder query, AbstractAggregationBuilder abstractAggregationBuilder, Integer pageNum, Integer pageSize);

    /**
     * 条件分页查询
     *
     * @param clazz    实体类
     * @param query    条件对象
     * @param fields   限制返回字段(可以为空)
     * @param pageNum  分页-页码(可以为空)
     * @param pageSize 分页-数量(可以为空)
     * @return
     */
    RollPageResult<T> rollPage(Class<T> clazz, QueryBuilder query, String[] fields, Integer pageNum, Integer pageSize);

    /**
     * 条件分页查询
     *
     * @param clazz    实体类
     * @param query    条件对象
     * @param pageNum  分页-页码(可以为空)
     * @param pageSize 分页-数量(可以为空)
     * @param sort     限制排序(可以为空)
     * @return
     */
    RollPageResult<T> rollPage(Class<T> clazz, QueryBuilder query, Integer pageNum, Integer pageSize, SortBuilder sort);

    /**
     * 条件分页查询
     *
     * @param clazz                      实体类
     * @param query                      条件对象
     * @param abstractAggregationBuilder 聚合条件(可以为空)
     * @param fields                     限制返回字段(可以为空)
     * @param pageNum                    分页-页码(可以为空)
     * @param pageSize                   分页-数量(可以为空)
     * @param sort                       限制排序(可以为空)
     * @return
     */
    RollPageResult<T> rollPage(Class<T> clazz, QueryBuilder query, AbstractAggregationBuilder abstractAggregationBuilder, String[] fields, Integer pageNum, Integer pageSize, SortBuilder sort);

}
