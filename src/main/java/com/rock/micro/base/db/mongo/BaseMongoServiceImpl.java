package com.rock.micro.base.db.mongo;

import com.rock.micro.base.data.BaseDocument;
import com.rock.micro.base.util.ArrayExtraUtils;
import com.rock.micro.base.util.ListExtraUtils;
import com.rock.micro.base.util.MongoExtraUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * mongo 服务基底实现
 *
 * @Author ayl
 * @Date 2022-03-09
 */
@Service
public class BaseMongoServiceImpl<T extends BaseDocument> implements BaseMongoService<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseMongoServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 用反射,获取当前服务的泛型
     *
     * @return
     */
    private Class<T> getEntityClass() {
        //获取泛型类型
        Type type = getClass().getGenericSuperclass();
        //如果不是
        if (type instanceof ParameterizedType == false) {
            //抛
            throw new NullPointerException("mongo基类不是泛型类");
        }
        //强转,获取泛型的类
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        //如果没有
        if (actualTypeArguments == null || actualTypeArguments.length < 1) {
            //抛
            throw new NullPointerException("mongo基类泛型不存在");
        }
        //获取第一个
        Type actualTypeArgument = actualTypeArguments[0];
        //如果不是
        if (actualTypeArgument instanceof Class == false) {
            //抛
            throw new NullPointerException("mongo基类泛型类型不正确");
        }
        //返回
        return (Class<T>) actualTypeArgument;
    }

    @Override
    public T create(T document) {
        //判空
        if (document == null) {
            //过
            return null;
        }
        //创建前初始化
        BaseDocument.createBuild(document);
        //插入
        return this.mongoTemplate.insert(document);
    }

    @Override
    public List<T> create(Collection<T> documentList) {
        //判空
        if (CollectionUtils.isEmpty(documentList)) {
            //过
            return new ArrayList<>();
        }
        //初始化插入列表
        List<T> insertList = new ArrayList<>();
        //循环
        for (T document : documentList) {
            //判空
            if (document == null) {
                //本轮过
                continue;
            }
            //创建前初始化
            BaseDocument.createBuild(document);
            //记录
            insertList.add(document);
        }
        //批量插入
        return new ArrayList<>(this.mongoTemplate.insertAll(insertList));
    }

    @Override
    public T getById(String id) {
        //实现
        return getById(id, null);
    }

    @Override
    public T getById(String id, String fields) {
        //判空
        if (StringUtils.isBlank(id)) {
            //过
            return null;
        }
        //初始化查询
        Query query = MongoExtraUtils.initQueryAndBase(id);
        //限制返回参数
        MongoExtraUtils.setFields(query, fields);
        //实现
        return this.mongoTemplate.findOne(query, getEntityClass());
    }

    @Override
    public List<T> listByIdList(Collection<String> idList) {
        //实现
        return listByIdList(idList, null);
    }

    @Override
    public List<T> listByIdList(Collection<String> idList, String fields) {
        //判空
        if (CollectionUtils.isEmpty(idList)) {
            //过
            return new ArrayList<>();
        }
        //限制条件
        Query query = MongoExtraUtils.initQueryAndBase(idList);
        //限制返回参数
        MongoExtraUtils.setFields(query, fields);
        //根据id列表查询
        return this.mongoTemplate.find(query, getEntityClass());
    }

    @Override
    public List<T> listAll() {
        //实现
        return listAll(null);
    }

    @Override
    public List<T> listAll(String fields) {
        //初始化查询
        Query query = new Query();
        //限制
        MongoExtraUtils.setFields(query, fields);
        //实现
        return mongoTemplate.find(query, getEntityClass());
    }

    @Override
    public List<String> listAllId() {
        //实现
        return listAll("id")
                .stream()
                .map(BaseDocument::getId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(String id) {
        //判空
        if (StringUtils.isBlank(id)) {
            //过
            return false;
        }
        //根据id删除
        return this.mongoTemplate.remove(MongoExtraUtils.initQueryAndBase(id), getEntityClass()).getDeletedCount() == 1L;
    }

    @Override
    public boolean deleteByIdList(Collection<String> idList) {
        //判空
        if (CollectionUtils.isEmpty(idList)) {
            //过
            return false;
        }
        //根据id列表删除
        return idList.size() == this.mongoTemplate.remove(MongoExtraUtils.initQueryAndBase(idList), getEntityClass()).getDeletedCount();
    }

    @Override
    public boolean updateSkipNullById(T document) {
        //判空
        if (document == null) {
            //过
            return false;
        }
        //id
        String id = document.getId();
        //判空
        if (StringUtils.isBlank(id)) {
            //过
            return false;
        }
        //限制条件
        Query query = MongoExtraUtils.initQueryAndBase(id);
        //更新
        Update update = MongoExtraUtils.initUpDateAndBase();
        //组装更新字段
        MongoExtraUtils.updateSkipNullByDocumentNoExtends(update, document);
        //只更新一个
        return this.mongoTemplate.updateFirst(query, update, getEntityClass()).getModifiedCount() > 0L;
    }

    @Override
    public boolean batchUpdateSkipNullById(Collection<T> documentList) {
        //判空
        if (CollectionUtils.isEmpty(documentList)) {
            //过
            return false;
        }
        //更新数量
        int count = 0;
        //批量update操作
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, getEntityClass());
        //循环
        for (T document : documentList) {
            //获取id
            String id = Optional.ofNullable(document)
                    .map(BaseDocument::getId)
                    .orElse("");
            //判空
            if (StringUtils.isBlank(id)) {
                //本轮过
                continue;
            }
            //初始化查询
            Query query = MongoExtraUtils.initQueryAndBase(id);
            //初始化更新
            Update update = MongoExtraUtils.initUpDateAndBase();
            //组装批量更新
            MongoExtraUtils.updateSkipNullByDocumentNoExtends(update, document);
            //组装批量修改
            bulkOperations.updateOne(query, update);
            //+1
            count++;
        }
        //如果有更新
        if (count > 0) {
            //批量更新
            bulkOperations.execute();
            //成功
            return true;
        }
        //默认失败
        return false;
    }

    @Override
    public void batchCreateOrUpdateSkipNullById(Collection<T> documentList) {

        /**
         * 判空
         */

        //判空
        if (CollectionUtils.isEmpty(documentList)) {
            //过
            return;
        }

        /**
         * 批量 创建或更新
         */

        //批量编辑
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, getEntityClass());
        //要处理的数量
        int count = 0;
        //循环
        for (T document : documentList) {

            /**
             * 判空
             */

            //判空
            if (document == null) {
                //本轮过
                continue;
            }
            //获取id
            String id = document.getId();
            //如果没有id
            if (StringUtils.isBlank(id)) {
                //本轮过
                continue;
            }

            /**
             * 查询
             */

            //限制条件
            Query query = MongoExtraUtils.initQueryAndBase(id);

            /**
             * 创建或更新
             */

            //初始化更新及基类
            Update update = MongoExtraUtils.initUpsertAndBase(id);

            //更新字段
            MongoExtraUtils.updateSkipNullByDocumentNoExtends(update, document);

            //组装创建或更新
            bulkOperations.upsert(query, update);
            //+1
            count++;
        }
        //判空
        if (count > 0) {
            //执行
            bulkOperations.execute();
        }

    }

    @Override
    public void rollPageParamBuilder(MongoRollPageParam param, List<Criteria> criteriaList) {

        /**
         * id
         */

        //拆分id 1
        List<String> idsList = ListExtraUtils.split(param.getIds());
        //如果要限制id 1
        if (CollectionUtils.isNotEmpty(idsList)) {
            //限制id
            criteriaList.add(Criteria.where("_id").in(idsList));
        }
        //如果要限制id 2
        if (CollectionUtils.isNotEmpty(param.getIdList())) {
            //限制id
            criteriaList.add(Criteria.where("_id").in(param.getIdList()));
        }

        /**
         * 处理时间区间
         */

        //如果要限制时间范围
        if (StringUtils.isNotBlank(param.getTimeType()) && param.getStartTime() != null && param.getEndTime() != null) {
            //限制时间范围
            criteriaList.add(Criteria.where(param.getTimeType()).gte(new Date(param.getStartTime())).lte(new Date(param.getEndTime())));
        }

        /**
         * 处理一般关键词
         */

        //获取关键词列表
        List<String> keywordList = param.getKeywordList();
        //获取查询模式,默认查询产品SKU
        String keywordType = Optional.ofNullable(param)
                .map(MongoRollPageParam::getKeywordType)
                .orElse("");
        //如果需要限制关键词
        if (CollectionUtils.isNotEmpty(keywordList) && StringUtils.isNotBlank(keywordType)) {
            //查询精度,默认精确
            String searchType = Optional.ofNullable(param)
                    .map(MongoRollPageParam::getSearchType)
                    .orElse("exact");
            //判断是模糊还是确定还是其他
            switch (searchType) {
                //精确
                case "exact":
                    //支持多关键词
                    criteriaList.add(Criteria.where(keywordType).in(keywordList));
                    break;
                //简单模糊查询(适用90%情况)
                case "dim":
                    //模糊查不支持多关键词
                    criteriaList.add(Criteria.where(keywordType).regex(keywordList.stream().findFirst().get()));
                    break;
                //复杂模糊查询(消耗性能但是模糊准确,忽略大小写并适配特殊字符)
                case "complexDim":
                    //模糊查不支持多关键词
                    criteriaList.add(Criteria.where(keywordType).regex(Pattern.compile("^.*" + MongoExtraUtils.escapeExprSpecialWord(keywordList.stream().findFirst().get()) + ".*$", Pattern.CASE_INSENSITIVE)));
                    break;
            }
        }

    }

    @Override
    public RollPageResult<T> rollPage(MongoRollPageParam param) {
        //实现
        return rollPage(param, null);
    }

    @Override
    public RollPageResult<T> rollPage(MongoRollPageParam param, List<Criteria> criteriaList) {

        /**
         * 初始化
         */

        //and条件列表
        List<Criteria> andCriteriaList = new ArrayList<>();
        //如果有额外的条件
        if (CollectionUtils.isNotEmpty(criteriaList)) {
            //组装
            andCriteriaList.addAll(criteriaList);
        }
        //构建通用查询参数
        rollPageParamBuilder(param, andCriteriaList);

        /**
         * 排序
         */

        //排序key,默认更新时间
        String sortKey = Optional.ofNullable(param)
                .map(MongoRollPageParam::getSortKey)
                .orElse("updateDate");
        //排序方式,默认倒序
        String sortOrder = Optional.ofNullable(param)
                .map(MongoRollPageParam::getSortOrder)
                .orElse("desc");

        /**
         * 是否需要分页
         */

        //是否需要count,默认false
        boolean needCount = Optional.ofNullable(param)
                .map(MongoRollPageParam::getNeedCount)
                .orElse(false);

        /**
         * 最终实现
         */

        //查询实现
        return rollPage(
                //组装各种条件
                andCriteriaList,
                //限制返回字段
                ArrayExtraUtils.toArray(param.getFields()),
                //分页,默认20
                param.getPageNum() == null ? 1 : param.getPageNum(),
                param.getPageSize() == null ? 20 : param.getPageSize(),
                //指定排序
                Sort.by(Sort.Direction.fromString(sortOrder), sortKey),
                //是否返回count
                needCount
        );
    }

    /**
     * 翻页查询 底层实现
     *
     * @param criteriaList 参数条件列表
     * @param fields       限制参数
     * @param pageNum      分页
     * @param pageSize     分页
     * @param sort         排序
     * @param needCount    是否需要count(额外一次count查询)
     * @return
     */
    private RollPageResult<T> rollPage(List<Criteria> criteriaList, String[] fields, Integer pageNum, Integer pageSize, Sort sort, boolean needCount) {

        //初始化响应对象
        RollPageResult<T> result = new RollPageResult<>();

        //初始化条件
        Criteria criteria = new Criteria();
        //如果存在条件列表
        if (CollectionUtils.isNotEmpty(criteriaList)) {
            //组装条件列表
            criteria.andOperator(criteriaList.toArray(new Criteria[]{}));
        }

        //获取当前泛型
        Class<T> clazz = getEntityClass();

        //初始化查询
        Query query = new Query(criteria);

        //如果需要count
        if (needCount) {
            //查询count
            long total = this.mongoTemplate.count(query, clazz);
            //组装
            result.setTotal(total);
        } else {
            //默认
            result.setTotal(-1L);
        }

        //如果需要排序
        if (sort != null) {
            //按照规则
            query.with(sort);
        }

        //设置分页
        MongoExtraUtils.setPage(query, pageNum, pageSize);
        //限制返回字段
        MongoExtraUtils.setFields(query, fields);

        //日志
        LOG.info("Mongo RollPage Query Execute:[{}]", query.toString());

        //查询数据
        List<T> docList = this.mongoTemplate.find(query, clazz);
        //组装数据
        result.setList(docList);
        //返回
        return result;
    }

}

