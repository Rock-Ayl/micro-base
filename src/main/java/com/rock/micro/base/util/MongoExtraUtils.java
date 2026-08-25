package com.rock.micro.base.util;

import com.mongodb.bulk.BulkWriteInsert;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.bulk.BulkWriteUpsert;
import com.rock.micro.base.data.BaseDocument;
import com.rock.micro.base.db.mongo.BaseMongoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.FacetOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.*;

/**
 * mongo 扩展工具包
 *
 * @Author ayl
 * @Date 2021-12-24
 */
public class MongoExtraUtils {

    private static final Logger LOG = LoggerFactory.getLogger(MongoExtraUtils.class);

    /**
     * 为 mongo {@link Query} 对象组装限制返回参数
     *
     * @param query  mongo query 对象
     * @param fields 限制参数 eg:   "id,state,sellerSku"
     */
    public static void setFields(Query query, String fields) {
        //实现
        setFields(query, ArrayExtraUtils.toArray(fields));
    }

    /**
     * 为 mongo {@link Query} 对象组装限制返回参数
     *
     * @param query     mongo query 对象
     * @param fieldList 限制参数 eg:   ["id,state,sellerSku"]
     */
    public static void setFields(Query query, List<String> fieldList) {
        //判空
        if (CollectionUtils.isEmpty(fieldList)) {
            //过
            return;
        }
        //实现
        setFields(query, fieldList.toArray(new String[]{}));
    }

    /**
     * 为 mongo {@link Query} 对象组装限制返回参数
     *
     * @param query    mongo query 对象
     * @param fieldArr 限制参数 eg:   "id,state,sellerSku"
     */
    public static void setFields(Query query, String[] fieldArr) {
        //判空
        if (query == null || fieldArr == null || fieldArr.length < 1) {
            //过
            return;
        }
        //组装
        query.fields().include(fieldArr);
    }

    /**
     * 为 mongo {@link Query} 对象组装限制返回参数
     *
     * @param query  mongo query 对象
     * @param fields 限制返回参数 Lambda表达式格式
     */
    public static <T> void setFieldsLambda(Query query, List<LambdaParseFieldNameExtraUtils.MFunction<T, ?>> fields) {
        //实现
        setFields(query, String.join(",", LambdaParseFieldNameExtraUtils.getMongoColumnList(fields)));
    }

    /**
     * 为 mongo {@link Query} 对象组装限制返回参数
     *
     * @param query  mongo query 对象
     * @param fields 限制参数 eg:   "id,state,sellerSku"
     */
    public static void setExcludeFields(Query query, String fields) {
        //实现
        setExcludeFields(query, ArrayExtraUtils.toArray(fields));
    }

    /**
     * 为 mongo {@link Query} 对象组装限制返回参数
     *
     * @param query     mongo query 对象
     * @param fieldList 限制参数 eg:   ["id,state,sellerSku"]
     */
    public static void setExcludeFields(Query query, List<String> fieldList) {
        //判空
        if (CollectionUtils.isEmpty(fieldList)) {
            //过
            return;
        }
        //实现
        setExcludeFields(query, fieldList.toArray(new String[]{}));
    }

    /**
     * 为 mongo {@link Query} 对象组装限制返回参数
     *
     * @param query    mongo query 对象
     * @param fieldArr 限制参数 eg:   "id,state,sellerSku"
     */
    public static void setExcludeFields(Query query, String[] fieldArr) {
        //判空
        if (query == null || fieldArr == null || fieldArr.length < 1) {
            //过
            return;
        }
        //组装
        query.fields().exclude(fieldArr);
    }

    /**
     * 为 mongo {@link Query} 对象组装限制返回参数
     *
     * @param query  mongo query 对象
     * @param fields 限制返回参数 Lambda表达式格式
     */
    public static <T> void setExcludeFieldsLambda(Query query, List<LambdaParseFieldNameExtraUtils.MFunction<T, ?>> fields) {
        //实现
        setExcludeFields(query, String.join(",", LambdaParseFieldNameExtraUtils.getMongoColumnList(fields)));
    }

    /**
     * 为 mongo {@link Query} 设置常用分页
     *
     * @param query
     * @param pageNum  分页,可为空
     * @param pageSize 分页,可为空
     * @return
     */
    public static void setPage(Query query, Integer pageNum, Integer pageSize) {
        //判空
        if (query == null) {
            //过
            return;
        }
        //如果需要限制分页
        if (pageSize != null && pageNum != null && pageNum > 0 && pageSize > 0) {
            //限制分页
            query.limit(pageSize).skip((pageNum - 1L) * pageSize);
        }
    }

    /**
     * 为 mongo {@link Query} 设置常用分页
     *
     * @param query     查询条件
     * @param sortOrder 排序-规则枚举
     * @param sortKey   排序-key
     * @return
     */
    public static <T1, R1> void setSort(Query query, Sort.Direction sortOrder, LambdaParseFieldNameExtraUtils.MFunction<T1, R1> sortKey) {
        //判空
        if (query == null || sortOrder == null || sortKey == null) {
            //过
            return;
        }
        //指定排序
        query.with(Sort.by(sortOrder, LambdaParseFieldNameExtraUtils.getMongoColumn(sortKey)));
    }

    /**
     * 为 mongo Query 操作 初始化一个关于基类的 {@link Query}
     *
     * @param id 主键id
     * @return
     */
    public static Query initQueryAndBase(String id) {
        //限制条件
        Query query = new Query(Criteria
                .where(LambdaParseFieldNameExtraUtils.getMongoColumn(BaseDocument::getId)).is(id)
        );
        //返回
        return query;
    }

    /**
     * 为 mongo Query 操作 初始化一个关于基类的 {@link Query}
     *
     * @param id         主键id
     * @param updateDate 更新时间
     * @return
     */
    public static Query initQueryAndBase(String id, Date updateDate) {
        //限制条件
        Query query = new Query(Criteria
                .where(LambdaParseFieldNameExtraUtils.getMongoColumn(BaseDocument::getId)).is(id)
                .and(LambdaParseFieldNameExtraUtils.getMongoColumn(BaseDocument::getUpdateDate)).is(updateDate)
        );
        //返回
        return query;
    }

    /**
     * 为 mongo Query 操作 初始化一个关于基类的 {@link Query}
     *
     * @param idList 主键id列表
     * @return
     */
    public static Query initQueryAndBase(Collection<String> idList) {
        //限制条件
        Query query = new Query(Criteria
                .where(LambdaParseFieldNameExtraUtils.getMongoColumn(BaseDocument::getId)).in(idList)
        );
        //返回
        return query;
    }

    /**
     * 为 mongo update 操作 初始化一个关于基类的 {@link Update}
     *
     * @return
     */
    public static Update initUpDateAndBase() {
        //初始化更新实体
        Update update = new Update();

        //固定更新最后更新时间
        update.set(LambdaParseFieldNameExtraUtils.getMongoColumn(BaseDocument::getUpdateDate), new Date());

        //返回实体
        return update;
    }

    /**
     * 为 mongo upsert 操作 初始化一个关于基类的 {@link Update}
     *
     * @param id    主键id
     * @param clazz 要映射的类
     * @return
     */
    public static Update initUpsertAndBase(String id, Class<?> clazz) {
        //初始化更新实体
        Update update = new Update();

        //仅创建唯一id
        update.setOnInsert(LambdaParseFieldNameExtraUtils.getMongoColumn(BaseDocument::getId), id);
        //仅创建创建时间
        update.setOnInsert(LambdaParseFieldNameExtraUtils.getMongoColumn(BaseDocument::getCreateDate), new Date());
        //仅创建是否删除
        update.setOnInsert("del", false);
        //映射class
        update.setOnInsert("_class", clazz.getName());

        //固定创建或更新最后更新时间
        update.set(LambdaParseFieldNameExtraUtils.getMongoColumn(BaseDocument::getUpdateDate), new Date());

        //返回实体
        return update;
    }

    /**
     * 根据分页条件,初始化一个分页条件的 {@link  FacetOperation}
     * 注:自己保证翻页参数的准确性
     *
     * @param pageNum  页码
     * @param pageSize 单页数据量
     * @return
     */
    public static FacetOperation initRollPageFacet(int pageNum, int pageSize) {
        //获取key
        String totalKey = LambdaParseFieldNameExtraUtils.getMongoColumn(BaseMongoService.RollPageResult<Object>::getTotal);
        String listKey = LambdaParseFieldNameExtraUtils.getMongoColumn(BaseMongoService.RollPageResult<Object>::getList);
        //分页+返回count
        return Aggregation.facet()
                //分页列表
                .and(
                        Aggregation.skip((pageNum - 1L) * pageSize),
                        Aggregation.limit(pageSize)
                ).as(listKey)
                //返回count
                .and(
                        Aggregation.count().as(totalKey)
                ).as(totalKey);
    }

    /**
     * Lambda表达式 设置更新字段
     *
     * @param update   更新实体
     * @param function Lambda表达式
     * @param value    任意对象
     */
    public static <T, R> void updateSet(Update update, LambdaParseFieldNameExtraUtils.MFunction<T, R> function, Object value) {
        //判空
        if (update == null) {
            //过
            return;
        }
        //实现
        update.set(LambdaParseFieldNameExtraUtils.getMongoColumn(function), value);
    }

    /**
     * Lambda表达式 设置 unset 字段
     *
     * @param update   更新实体
     * @param function Lambda表达式
     */
    public static <T, R> void updateUnset(Update update, LambdaParseFieldNameExtraUtils.MFunction<T, R> function) {
        //判空
        if (update == null) {
            //过
            return;
        }
        //实现
        update.unset(LambdaParseFieldNameExtraUtils.getMongoColumn(function));
    }

    /**
     * 根据实体,为 mongo {@link Update} set 该实体所有不为空的字段
     * 注意:不包含继承对象的参数
     *
     * @param update   要update的对象
     * @param document 实体
     * @return
     */
    public static <T extends BaseDocument> void updateSkipNullByDocumentNoExtends(Update update, T document) {
        //实现
        updateSkipNullByDocumentNoExtends(update, document, null);
    }

    /**
     * 根据实体,为 mongo {@link Update} set 该实体所有不为空的字段
     * 注意:不包含继承对象的参数
     *
     * @param update             要update的对象
     * @param document           实体
     * @param onInsertFieldsList 指定需要 onInsert 的字段,如果这些字段不为空,则仅在创建处理,选填
     * @return
     */
    public static <T extends BaseDocument> void updateSkipNullByDocumentNoExtends(Update update, T document, Collection<String> onInsertFieldsList) {
        //判空
        if (update == null || document == null) {
            //过
            return;
        }
        //获取本类的Field数组,继承无效
        Field[] fields = document.getClass().getDeclaredFields();
        //判空
        if (fields == null || fields.length < 1) {
            //过
            return;
        }
        //初始化 onInsert 字段集合
        Set<String> onInsertSet = new HashSet<>();
        //如果存在指定 onInsert 的字段
        if (CollectionUtils.isNotEmpty(onInsertFieldsList)) {
            //加入所有
            onInsertSet.addAll(onInsertFieldsList);
        }
        //循环
        for (Field field : fields) {
            try {
                //字段名
                String fieldName = field.getName();
                //判空
                if (StringUtils.isBlank(fieldName)) {
                    //本轮过
                    continue;
                }
                //过滤掉一些特殊的
                switch (fieldName) {
                    //预留字段,一定不会更新
                    case "id":
                    case "serialVersionUID":
                    case "createDate":
                    case "updateDate":
                    case "del":
                        continue;
                        //其他过
                    default:
                        break;
                }
                //限强制访问私有字段
                field.setAccessible(true);
                //获取内容
                Object value = field.get(document);
                //判空
                if (value != null) {
                    //如果仅创建更新
                    if (onInsertSet.contains(fieldName)) {
                        //设置仅创建更新
                        update.setOnInsert(fieldName, value);
                    } else {
                        //创建或更新都更新
                        update.set(fieldName, value);
                    }
                }
            } catch (Exception e) {
                LOG.error("updateSkipNullByDocumentNoExtends error", e);
            }
        }
    }

    /**
     * 关键词搜索时,转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return
     */
    public static String escapeExprSpecialWord(String keyword) {
        //判空
        if (keyword == null) {
            //过
            return null;
        }
        //一般特殊
        String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
        //循环
        for (String key : fbsArr) {
            //一般的特殊字符
            if (keyword.contains(key)) {
                //替换
                keyword = keyword.replace(key, "\\" + key);
            }
        }
        //返回
        return keyword;
    }

    /**
     * 返回一个默认的 {@link BulkWriteResult} 实例
     * -
     * 用于无实际批量写入操作时，返回一个空结果对象。
     */
    public static BulkWriteResult defaultBulkWriteResult() {
        //初始化
        return new BulkWriteResult() {

            /**
             * 是否收到 MongoDB 服务端确认
             *
             * @return true
             */
            @Override
            public boolean wasAcknowledged() {
                return true;
            }

            /**
             * 插入数量
             *
             * @return 0
             */
            @Override
            public int getInsertedCount() {
                return 0;
            }

            /**
             * 匹配到的文档数量
             *
             * @return 0
             */
            @Override
            public int getMatchedCount() {
                return 0;
            }

            /**
             * 删除数量
             *
             * @return 0
             */
            @Override
            public int getDeletedCount() {
                return 0;
            }

            /**
             * 实际修改数量
             *
             * @return 0
             */
            @Override
            public int getModifiedCount() {
                return 0;
            }

            /**
             * 插入数据列表
             *
             * @return 空集合
             */
            @Override
            public List<BulkWriteInsert> getInserts() {
                return Collections.emptyList();
            }

            /**
             * Upsert数据列表
             *
             * @return 空集合
             */
            @Override
            public List<BulkWriteUpsert> getUpserts() {
                return Collections.emptyList();
            }

        };
    }

}