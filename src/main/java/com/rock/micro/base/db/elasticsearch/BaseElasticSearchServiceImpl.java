package com.rock.micro.base.db.elasticsearch;

import com.rock.micro.base.data.BaseIndex;
import com.rock.micro.base.util.FastJsonExtraUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * mongo 服务基底实现
 *
 * @Author ayl
 * @Date 2022-03-09
 */
@Service
public class BaseElasticSearchServiceImpl<T extends BaseIndex> implements BaseElasticSearchService<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseElasticSearchServiceImpl.class);

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public T create(T index) {
        //创建前初始化
        BaseIndex.createBuild(index);
        //插入
        elasticsearchRestTemplate.save(index);
        //返回
        return index;
    }

    @Override
    public Collection<T> create(List<T> indexList) {
        //循环
        for (T index : indexList) {
            //创建前初始化
            BaseIndex.createBuild(index);
        }
        //批量插入
        elasticsearchRestTemplate.save(indexList);
        //返回
        return indexList;
    }

    @Override
    public T get(Class<T> clazz, String id) {
        //查询
        return elasticsearchRestTemplate.get(id, clazz);
    }

    @Override
    public List<T> list(Class<T> clazz, List<String> idList) {
        //初始化id查询
        IdsQueryBuilder idBuilder = new IdsQueryBuilder();
        //组装id
        idBuilder.addIds(idList.toArray(new String[]{}));
        //查询
        return rollPage(clazz, idBuilder, null, null, null, null, null).getList();
    }

    @Override
    public List<T> list(Class<T> clazz, QueryBuilder query) {
        //查询
        return rollPage(clazz, query, null, null, null, null, null).getList();
    }

    @Override
    public void delete(Class<T> clazz, String id) {
        //删除
        elasticsearchRestTemplate.delete(id, clazz);
    }

    @Override
    public void updateSkipNull(T index) {
        //获取id
        String id = index.getId();
        //判空
        if (StringUtils.isBlank(id)) {
            //过
            return;
        }
        //将实体解析为document
        Document document = Document.parse(FastJsonExtraUtils.toJSONString(index));
        //初始化updateQuery
        UpdateQuery updateQuery = UpdateQuery.builder(id).withDocument(document).build();
        //更新
        elasticsearchRestTemplate.update(updateQuery, getIndex(index));
    }

    @Override
    public void batchUpdateSkipNull(List<T> indexList) {
        //判空
        if (CollectionUtils.isEmpty(indexList)) {
            //过
            return;
        }
        //批量更新列表
        List<UpdateQuery> updateQueryList = new ArrayList<>();
        //循环
        for (T index : indexList) {
            //获取id
            String id = index.getId();
            //判空
            if (StringUtils.isBlank(id)) {
                //本轮过
                continue;
            }
            //将实体解析为document
            Document document = Document.parse(FastJsonExtraUtils.toJSONString(index));
            //初始化updateQuery
            UpdateQuery updateQuery = UpdateQuery.builder(id).withDocument(document).build();
            //组装
            updateQueryList.add(updateQuery);
        }
        //如果存在需要更新的内容
        if (CollectionUtils.isNotEmpty(updateQueryList)) {
            //批量更新
            elasticsearchRestTemplate.bulkUpdate(updateQueryList, getIndex(indexList.get(0)));
        }
    }

    @Override
    public RollPageResult<T> rollPage(Class<T> clazz, QueryBuilder query, Integer pageNum, Integer pageSize) {
        //实现
        return rollPage(clazz, query, null, null, pageNum, pageSize, null);
    }

    @Override
    public RollPageResult<T> rollPage(Class<T> clazz, QueryBuilder query, AbstractAggregationBuilder abstractAggregationBuilder, Integer pageNum, Integer pageSize) {
        //实现
        return rollPage(clazz, query, abstractAggregationBuilder, null, pageNum, pageSize, null);
    }

    @Override
    public RollPageResult<T> rollPage(Class<T> clazz, QueryBuilder query, String[] fields, Integer pageNum, Integer pageSize) {
        //实现
        return rollPage(clazz, query, null, fields, pageNum, pageSize, null);
    }

    @Override
    public RollPageResult<T> rollPage(Class<T> clazz, QueryBuilder query, Integer pageNum, Integer pageSize, SortBuilder sort) {
        //实现
        return rollPage(clazz, query, null, null, pageNum, pageSize, sort);
    }

    @Override
    public RollPageResult<T> rollPage(Class<T> clazz, QueryBuilder query, AbstractAggregationBuilder abstractAggregationBuilder, String[] fields, Integer pageNum, Integer pageSize, SortBuilder sort) {
        //初始化searchBuilder
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //组装查询条件
        nativeSearchQueryBuilder.withQuery(query);
        //如果要限制返回字段
        if (fields != null && fields.length > 0) {
            //限制返回字段
            nativeSearchQueryBuilder.withFields(fields);
        }
        //如果要限制排序
        if (sort != null) {
            //限制排序
            nativeSearchQueryBuilder.withSort(sort);
        }
        //如果要加入聚合
        if (abstractAggregationBuilder != null) {
            //聚合搜索
            nativeSearchQueryBuilder.addAggregation(abstractAggregationBuilder);
        }
        //build
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        //初始化
        RollPageResult<T> result = new RollPageResult<T>();
        //如果需要分页
        if (pageNum != null && pageSize != null && pageNum > 0 && pageSize > 0) {
            //初始化分页
            PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
            //设置分页
            nativeSearchQuery.setPageable(pageRequest);
        }
        //查询
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, clazz, getIndex(clazz));
        //记录总数
        result.setTotal(searchHits.getTotalHits());
        //将返回实体拆包
        result.setList(searchHits.getSearchHits().stream().map(p -> p.getContent()).collect(Collectors.toList()));
        //获取聚合内容并组装
        result.setAggregations(searchHits.getAggregations());
        //返回
        return result;
    }

    /**
     * 根据实体,获取索引对象
     *
     * @param index
     * @return
     */
    private IndexCoordinates getIndex(T index) {
        //获取索引settings
        Map<String, Object> settings = elasticsearchRestTemplate.indexOps(index.getClass()).getSettings();
        //获取索引名
        String indexName = settings.getOrDefault("index.provided_name", "none").toString();
        //返回
        return IndexCoordinates.of(indexName);
    }

    /**
     * 根据类,获取索引对象
     *
     * @param clazz
     * @return
     */
    private IndexCoordinates getIndex(Class<T> clazz) {
        //获取索引settings
        Map<String, Object> settings = elasticsearchRestTemplate.indexOps(clazz).getSettings();
        //获取索引名
        String indexName = settings.getOrDefault("index.provided_name", "none").toString();
        //返回
        return IndexCoordinates.of(indexName);
    }

}

