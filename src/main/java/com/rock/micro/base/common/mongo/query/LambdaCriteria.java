package com.rock.micro.base.common.mongo.query;

import com.rock.micro.base.util.LambdaParseFieldNameExtraUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 使用 Lambda表达式 构建 {@link Criteria}
 *
 * @Author ayl
 * @Date 2024-01-30
 */
public class LambdaCriteria {

    //被封装的,链路中最新的,每一个操作后,和之前的都不会是一个对象
    private Criteria criteria;

    /**
     * 重写初始化
     */
    public LambdaCriteria() {
        //初始化默认
        this.criteria = new Criteria();
    }

    /**
     * 重写初始化,这里私有化
     *
     * @param key 字段key
     */
    private LambdaCriteria(String key) {
        //初始化默认
        this.criteria = Criteria.where(key);
    }

    /**
     * 实现 where 一级的情况 eg: sku
     *
     * @param key1 第一级key
     * @return
     */
    public static <T1, R1> LambdaCriteria where(LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1) {
        //生成对应路径
        String path = path(key1, new int[]{});
        //实现
        return new LambdaCriteria(path);
    }

    /**
     * 实现 where 两级的情况 eg: productList.sku
     *
     * @param key1 第一级key
     * @param key2 第二级key
     * @return
     */
    public static <T1, R1, T2, R2> LambdaCriteria where(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            LambdaParseFieldNameExtraUtils.MFunction<T2, R2> key2) {
        //生成对应路径
        String path = path(key1, key2, new int[]{});
        //实现
        return new LambdaCriteria(path);
    }

    /**
     * 实现 where 三级的情况 eg: productList.cat.sku
     *
     * @param key1 第一级key
     * @param key2 第二级key
     * @param key3 第三级key
     * @return
     */
    public static <T1, R1, T2, R2, T3, R3> LambdaCriteria where(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            LambdaParseFieldNameExtraUtils.MFunction<T2, R2> key2,
            LambdaParseFieldNameExtraUtils.MFunction<T3, R3> key3) {
        //生成对应路径
        String path = path(key1, key2, key3, new int[]{});
        //实现
        return new LambdaCriteria(path);
    }

    /**
     * 实现 where 一级的情况 eg: sku
     *
     * @param key1              第一级key
     * @param firstUpperCaseArr 强制指定索引的key第一个字母大写(大驼峰)(解决特殊情况)
     * @return
     */
    public static <T1, R1> LambdaCriteria where(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            int[] firstUpperCaseArr) {
        //生成对应路径
        String path = path(key1, firstUpperCaseArr);
        //实现
        return new LambdaCriteria(path);
    }

    /**
     * 实现 where 两级的情况 eg: productList.sku
     *
     * @param key1              第一级key
     * @param key2              第二级key
     * @param firstUpperCaseArr 强制指定索引的key第一个字母大写(大驼峰)(解决特殊情况)
     * @return
     */
    public static <T1, R1, T2, R2> LambdaCriteria where(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            LambdaParseFieldNameExtraUtils.MFunction<T2, R2> key2,
            int[] firstUpperCaseArr) {
        //生成对应路径
        String path = path(key1, key2, firstUpperCaseArr);
        //实现
        return new LambdaCriteria(path);
    }

    /**
     * 实现 where 三级的情况 eg: productList.cat.sku
     *
     * @param key1              第一级key
     * @param key2              第二级key
     * @param key3              第三级key
     * @param firstUpperCaseArr 强制指定索引的key第一个字母大写(大驼峰)(解决特殊情况)
     * @return
     */
    public static <T1, R1, T2, R2, T3, R3> LambdaCriteria where(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            LambdaParseFieldNameExtraUtils.MFunction<T2, R2> key2,
            LambdaParseFieldNameExtraUtils.MFunction<T3, R3> key3,
            int[] firstUpperCaseArr) {
        //生成对应路径
        String path = path(key1, key2, key3, firstUpperCaseArr);
        //实现
        return new LambdaCriteria(path);
    }

    /**
     * 实现 and 一级的情况 eg: sku
     *
     * @param key1 第一级key
     * @return
     */
    public <T1, R1> LambdaCriteria and(LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1) {
        //生成对应路径
        String path = path(key1, new int[]{});
        //实现
        this.criteria = this.criteria.and(path);
        //返回
        return this;
    }

    /**
     * 实现 and 两级的情况 eg: productList.sku
     *
     * @param key1 第一级key
     * @param key2 第二级key
     * @return
     */
    public <T1, R1, T2, R2> LambdaCriteria and(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            LambdaParseFieldNameExtraUtils.MFunction<T2, R2> key2) {
        //生成对应路径
        String path = path(key1, key2, new int[]{});
        //实现
        this.criteria = this.criteria.and(path);
        //返回
        return this;
    }

    /**
     * 实现 and 三级的情况 eg: productList.cat.sku
     *
     * @param key1 第一级key
     * @param key2 第二级key
     * @param key3 第三级key
     * @return
     */
    public <T1, R1, T2, R2, T3, R3> LambdaCriteria and(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            LambdaParseFieldNameExtraUtils.MFunction<T2, R2> key2,
            LambdaParseFieldNameExtraUtils.MFunction<T3, R3> key3) {
        //生成对应路径
        String path = path(key1, key2, key3, new int[]{});
        //实现
        this.criteria = this.criteria.and(path);
        //返回
        return this;
    }

    /**
     * 实现 and 一级的情况 eg: sku
     *
     * @param key1              第一级key
     * @param firstUpperCaseArr 强制指定索引的key第一个字母大写(大驼峰)(解决特殊情况)
     * @return
     */
    public <T1, R1> LambdaCriteria and(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            int[] firstUpperCaseArr) {
        //生成对应路径
        String path = path(key1, firstUpperCaseArr);
        //实现
        this.criteria = this.criteria.and(path);
        //返回
        return this;
    }

    /**
     * 实现 and 两级的情况 eg: productList.sku
     *
     * @param key1              第一级key
     * @param key2              第二级key
     * @param firstUpperCaseArr 强制指定索引的key第一个字母大写(大驼峰)(解决特殊情况)
     * @return
     */
    public <T1, R1, T2, R2> LambdaCriteria and(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            LambdaParseFieldNameExtraUtils.MFunction<T2, R2> key2,
            int[] firstUpperCaseArr) {
        //生成对应路径
        String path = path(key1, key2, firstUpperCaseArr);
        //实现
        this.criteria = this.criteria.and(path);
        //返回
        return this;
    }

    /**
     * 实现 and 三级的情况 eg: productList.cat.sku
     *
     * @param key1              第一级key
     * @param key2              第二级key
     * @param key3              第三级key
     * @param firstUpperCaseArr 强制指定索引的key第一个字母大写(大驼峰)(解决特殊情况)
     * @return
     */
    public <T1, R1, T2, R2, T3, R3> LambdaCriteria and(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            LambdaParseFieldNameExtraUtils.MFunction<T2, R2> key2,
            LambdaParseFieldNameExtraUtils.MFunction<T3, R3> key3,
            int[] firstUpperCaseArr) {
        //生成对应路径
        String path = path(key1, key2, key3, firstUpperCaseArr);
        //实现
        this.criteria = this.criteria.and(path);
        //返回
        return this;
    }

    /**
     * 实现 andOperator
     *
     * @param mongoLambdaCriteria
     * @return
     */
    public LambdaCriteria andOperator(LambdaCriteria... mongoLambdaCriteria) {
        //实现
        this.criteria = this.criteria.andOperator(Arrays
                //装箱
                .stream(mongoLambdaCriteria)
                //拆出对应Criteria
                .map(LambdaCriteria::getCriteria)
                //转化为列表
                .collect(Collectors.toList())
                //然后转数组
                .toArray(new Criteria[]{}));
        //返回
        return this;
    }

    /**
     * 实现 orOperator
     *
     * @param mongoLambdaCriteria
     * @return
     */
    public LambdaCriteria orOperator(LambdaCriteria... mongoLambdaCriteria) {
        //实现
        this.criteria = this.criteria.orOperator(Arrays
                //装箱
                .stream(mongoLambdaCriteria)
                //拆出对应Criteria
                .map(LambdaCriteria::getCriteria)
                //转化为列表
                .collect(Collectors.toList())
                //然后转数组
                .toArray(new Criteria[]{}));
        //返回
        return this;
    }

    /**
     * 实现 is
     *
     * @param value value
     * @return
     */
    public LambdaCriteria is(Object value) {
        //实现
        this.criteria = this.criteria.is(value);
        //返回
        return this;
    }

    /**
     * 实现 in
     *
     * @param values values
     * @return
     */
    public LambdaCriteria in(Collection<?> values) {
        //实现
        this.criteria = this.criteria.in(values);
        //返回
        return this;
    }

    /**
     * 实现 in
     *
     * @param values values
     * @return
     */
    public LambdaCriteria in(Object... values) {
        //实现
        this.criteria = this.criteria.in(values);
        //返回
        return this;
    }

    /**
     * 实现 all
     *
     * @param values values
     * @return
     */
    public LambdaCriteria all(Collection<?> values) {
        //实现
        this.criteria = this.criteria.all(values);
        //返回
        return this;
    }

    /**
     * 实现 all
     *
     * @param values values
     * @return
     */
    public LambdaCriteria all(Object... values) {
        //实现
        this.criteria = this.criteria.all(values);
        //返回
        return this;
    }

    /**
     * 实现 not
     *
     * @return
     */
    public LambdaCriteria not() {
        //实现
        this.criteria = this.criteria.not();
        //返回
        return this;
    }

    /**
     * 实现 ne
     *
     * @param value value
     * @return
     */
    public LambdaCriteria ne(Object value) {
        //实现
        this.criteria = this.criteria.ne(value);
        //返回
        return this;
    }

    /**
     * 实现 nin
     *
     * @param values values
     * @return
     */
    public LambdaCriteria nin(Collection<?> values) {
        //实现
        this.criteria = this.criteria.nin(values);
        //返回
        return this;
    }

    /**
     * 实现 nin
     *
     * @param values values
     * @return
     */
    public LambdaCriteria nin(Object... values) {
        //实现
        this.criteria = this.criteria.nin(values);
        //返回
        return this;
    }

    /**
     * 实现 gt
     *
     * @param value value
     * @return
     */
    public LambdaCriteria gt(Object value) {
        //实现
        this.criteria = this.criteria.gt(value);
        //返回
        return this;
    }

    /**
     * 实现 gte
     *
     * @param value value
     * @return
     */
    public LambdaCriteria gte(Object value) {
        //实现
        this.criteria = this.criteria.gte(value);
        //返回
        return this;
    }

    /**
     * 实现 lt
     *
     * @param value value
     * @return
     */
    public LambdaCriteria lt(Object value) {
        //实现
        this.criteria = this.criteria.lt(value);
        //返回
        return this;
    }

    /**
     * 实现 lte
     *
     * @param value value
     * @return
     */
    public LambdaCriteria lte(Object value) {
        //实现
        this.criteria = this.criteria.lte(value);
        //返回
        return this;
    }

    /**
     * 实现 exists
     *
     * @param value value
     * @return
     */
    public LambdaCriteria exists(boolean value) {
        //实现
        this.criteria = this.criteria.exists(value);
        //返回
        return this;
    }

    /**
     * 实现 regex
     *
     * @param regex 值
     * @return
     */
    public LambdaCriteria regex(String regex) {
        //实现
        this.criteria = this.criteria.regex(regex);
        //返回
        return this;
    }

    /**
     * 实现 regex
     *
     * @param regex   值
     * @param options 可选操作
     * @return
     */
    public LambdaCriteria regex(String regex, String options) {
        //实现
        this.criteria = this.criteria.regex(regex, options);
        //返回
        return this;
    }

    /**
     * 实现 regex
     *
     * @param pattern 正则
     * @return
     */
    public LambdaCriteria regex(Pattern pattern) {
        //实现
        this.criteria = this.criteria.regex(pattern);
        //返回
        return this;
    }

    /**
     * 实现 elemMatch
     *
     * @param lambdaCriteria 嵌套对象
     * @return
     */
    public LambdaCriteria elemMatch(LambdaCriteria lambdaCriteria) {
        //实现
        this.criteria = this.criteria.elemMatch(lambdaCriteria.getCriteria());
        //返回
        return this;
    }

    /**
     * 返回内部 {@link Criteria}
     *
     * @return
     */
    public Criteria getCriteria() {
        //返回
        return this.criteria;
    }

    /**
     * 返回内部 {@link Query}
     *
     * @return
     */
    public Query getQuery() {
        //返回
        return new Query(this.criteria);
    }

    /**
     * 实现 path 一级的情况 eg: sku
     *
     * @param key1              第一级key
     * @param firstUpperCaseArr 强制指定索引的key第一个字母大写(大驼峰)(解决特殊情况)
     * @return
     */
    private static <T1, R1> String path(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            int[] firstUpperCaseArr) {

        //转为索引Set
        Set<Integer> firstUpperCaseIndexSet = Arrays.stream(firstUpperCaseArr).boxed().collect(Collectors.toSet());

        /**
         * 解析名称
         */

        //解析key对应名称
        String key1Name = firstUpperCaseIndexSet.contains(0) == true ?
                LambdaParseFieldNameExtraUtils.getMongoColumnBigHump(key1) :
                LambdaParseFieldNameExtraUtils.getMongoColumn(key1);

        /**
         * 组装路径
         */

        //生成对应路径
        String path = key1Name;
        //实现
        return path;
    }

    /**
     * 实现 path 两级的情况 eg: productList.sku
     *
     * @param key1              第一级key
     * @param key2              第二级key
     * @param firstUpperCaseArr 强制指定索引的key第一个字母大写(大驼峰)(解决特殊情况)
     * @return
     */
    private static <T1, R1, T2, R2> String path(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            LambdaParseFieldNameExtraUtils.MFunction<T2, R2> key2,
            int[] firstUpperCaseArr) {

        //转为索引Set
        Set<Integer> firstUpperCaseIndexSet = Arrays.stream(firstUpperCaseArr).boxed().collect(Collectors.toSet());

        /**
         * 解析名称
         */

        //解析key对应名称
        String key1Name = firstUpperCaseIndexSet.contains(0) == true ?
                LambdaParseFieldNameExtraUtils.getMongoColumnBigHump(key1) :
                LambdaParseFieldNameExtraUtils.getMongoColumn(key1);
        String key2Name = firstUpperCaseIndexSet.contains(1) == true ?
                LambdaParseFieldNameExtraUtils.getMongoColumnBigHump(key2) :
                LambdaParseFieldNameExtraUtils.getMongoColumn(key2);

        /**
         * 组装路径
         */

        //生成对应路径
        String path = String.format("%s.%s", key1Name, key2Name);
        //返回
        return path;
    }

    /**
     * 实现 path 三级的情况 eg: productList.cat.sku
     *
     * @param key1              第一级key
     * @param key2              第二级key
     * @param key3              第三级key
     * @param firstUpperCaseArr 强制指定索引的key第一个字母大写(大驼峰)(解决特殊情况)
     * @return
     */
    private static <T1, R1, T2, R2, T3, R3> String path(
            LambdaParseFieldNameExtraUtils.MFunction<T1, R1> key1,
            LambdaParseFieldNameExtraUtils.MFunction<T2, R2> key2,
            LambdaParseFieldNameExtraUtils.MFunction<T3, R3> key3,
            int[] firstUpperCaseArr) {

        //转为索引Set
        Set<Integer> firstUpperCaseIndexSet = Arrays.stream(firstUpperCaseArr).boxed().collect(Collectors.toSet());

        /**
         * 解析名称
         */

        //解析key对应名称
        String key1Name = firstUpperCaseIndexSet.contains(0) == true ?
                LambdaParseFieldNameExtraUtils.getMongoColumnBigHump(key1) :
                LambdaParseFieldNameExtraUtils.getMongoColumn(key1);
        String key2Name = firstUpperCaseIndexSet.contains(1) == true ?
                LambdaParseFieldNameExtraUtils.getMongoColumnBigHump(key2) :
                LambdaParseFieldNameExtraUtils.getMongoColumn(key2);
        String key3Name = firstUpperCaseIndexSet.contains(2) == true ?
                LambdaParseFieldNameExtraUtils.getMongoColumnBigHump(key3) :
                LambdaParseFieldNameExtraUtils.getMongoColumn(key3);

        /**
         * 组装路径
         */

        //生成对应路径
        String path = String.format("%s.%s.%s", key1Name, key2Name, key3Name);
        //返回
        return path;
    }

    @Override
    public String toString() {
        //直接使用
        return this.criteria.toString();
    }

}