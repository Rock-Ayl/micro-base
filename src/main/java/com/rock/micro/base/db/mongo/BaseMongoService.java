package com.rock.micro.base.db.mongo;

import com.rock.micro.base.data.BaseDocument;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Collection;
import java.util.List;

/**
 * mongo服务基底
 *
 * @Author ayl
 * @Date 2022-03-09
 */
public interface BaseMongoService<T extends BaseDocument> {

    /**
     * 单个创建
     *
     * @param document 实体
     * @return
     */
    T create(T document);

    /**
     * 单个创建
     *
     * @param document   实体
     * @param coveringId 是否覆盖id
     * @return
     */
    T create(T document, boolean coveringId);

    /**
     * 批量创建
     *
     * @param documentList 实体 列表
     * @return
     */
    List<T> create(Collection<T> documentList);

    /**
     * 批量创建
     *
     * @param documentList 实体 列表
     * @param coveringId   是否覆盖id
     * @return
     */
    List<T> create(Collection<T> documentList, boolean coveringId);

    /**
     * 根据id,查询单个
     *
     * @param id id
     * @return
     */
    T getById(String id);

    /**
     * 根据id,查询单个,限制返回参数
     *
     * @param id     id
     * @param fields 限制返回参数
     * @return
     */
    T getById(String id, String fields);

    /**
     * 根据id列表,查询多个
     *
     * @param idList id列表
     * @return
     */
    List<T> listByIdList(Collection<String> idList);

    /**
     * 根据id列表,查询多个,限制返回参数
     *
     * @param idList id列表
     * @param fields 限制返回参数
     * @return
     */
    List<T> listByIdList(Collection<String> idList, String fields);

    /**
     * 获取所有
     *
     * @return
     */
    List<T> listAll();

    /**
     * 获取所有,限制返回参数
     *
     * @param fields 限制返回参数
     * @return
     */
    List<T> listAll(String fields);

    /**
     * 获取所有id
     *
     * @return
     */
    List<String> listAllId();

    /**
     * 根据id,真实删除
     *
     * @param id id
     * @return
     */
    boolean deleteById(String id);

    /**
     * 根据id列表,批量真实删除
     *
     * @param idList id列表
     * @return
     */
    boolean deleteByIdList(Collection<String> idList);

    /**
     * 根据实体,使用id,更新单个实体,跳过NULL的字段
     *
     * @param document 实体
     * @return
     */
    boolean updateSkipNullById(T document);

    /**
     * 根据实体列表,使用id,批量更新多个实体,跳过NULL的字段
     *
     * @param documentList 实体列表
     * @return
     */
    boolean batchUpdateSkipNullById(Collection<T> documentList);

    /**
     * 根据实体,使用id,创建或更新实体,跳过NULL的字段
     *
     * @param document 实体
     */
    void createOrUpdateSkipNullById(T document);

    /**
     * 根据实体列表,使用id,批量创建或更新多个实体,跳过NULL的字段
     *
     * @param documentList 实体列表
     */
    void batchCreateOrUpdateSkipNullById(Collection<T> documentList);

    @Getter
    @Setter
    @ApiModel("Mongo常用模板查询参数")
    public static class MongoRollPageParam {

        /**
         * 限制 id
         */

        @ApiModelProperty("限制id 1")
        private String ids;

        @ApiModelProperty("限制id 2")
        private List<String> idList;

        /**
         * 限制时间区间
         */

        @ApiModelProperty("限制时间类型")
        private String timeType;

        @ApiModelProperty("开始时间")
        private Long startTime;

        @ApiModelProperty("结束时间")
        private Long endTime;

        /**
         * 限制关键词
         */

        @ApiModelProperty("精确/模糊搜索[exact=精确][dim=简单模糊(性能高效果差)][complexDim=精确模糊(性能差效果好)]")
        private String searchType;

        @ApiModelProperty("关键字搜索类型")
        private String keywordType;

        @ApiModelProperty("关键字列表")
        private List<String> keywordList;

        /**
         * 分页
         */

        @ApiModelProperty("分页-数量")
        private Integer pageSize;

        @ApiModelProperty("分页-页码")
        private Integer pageNum;

        @ApiModelProperty("是否需要count(尽量不用,性能能省则省)")
        private Boolean needCount;

        /**
         * 排序
         */

        @ApiModelProperty("排序-key")
        private String sortKey;

        @ApiModelProperty("排序-正序 or 倒序 [desc][asc]")
        private String sortOrder;

        /**
         * 限制返回字段
         */

        @ApiModelProperty("限制返回字段 eg: id,name,sku,tags")
        private String fields;

    }

    @Getter
    @Setter
    @ApiModel("Mongo查询响应对象实体")
    public static class RollPageResult<T> {

        @ApiModelProperty("总数")
        private Long total;

        @ApiModelProperty("数据列表")
        private List<T> list;

    }

    /**
     * 构建通用查询参数,某些情况下会用到
     *
     * @param param        参数
     * @param criteriaList 要放入的列表
     */
    void rollPageParamBuilder(MongoRollPageParam param, List<Criteria> criteriaList);

    /**
     * 翻页查询
     *
     * @param param 模板参数
     * @return
     */
    RollPageResult<T> rollPage(MongoRollPageParam param);

    /**
     * 翻页查询
     *
     * @param param        模板参数
     * @param criteriaList 除了模板参数,其他的限制条件
     * @return
     */
    RollPageResult<T> rollPage(MongoRollPageParam param, List<Criteria> criteriaList);

}
