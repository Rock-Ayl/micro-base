package com.rock.micro.base.data;

import com.rock.micro.base.util.IdExtraUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Mysql 实体的基底
 *
 * @Author ayl
 * @Date 2022-03-09
 */
@Getter
@Setter
public class BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("唯一id")
    private String id;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("最后修改时间")
    private Date updateDate;

    @ApiModelProperty("状态删除字段")
    private boolean del;

    /**
     * 给新增数据的基底赋予基础数据
     *
     * @param baseDO 基类
     */
    public static <T extends BaseDO> void createBuild(T baseDO) {

        //判空
        if (baseDO == null) {
            //过
            return;
        }

        //创建、更新时间
        baseDO.setCreateDate(new Date());
        baseDO.setUpdateDate(new Date());

        //统一id
        baseDO.setId(IdExtraUtils.genGUID());

    }

    /**
     * 给修改数据的基底赋予基础数据
     *
     * @param baseDO 基类
     */
    public static <T extends BaseDO> void updateBuild(T baseDO) {

        //判空
        if (baseDO == null) {
            //过
            return;
        }

        //更新时间
        baseDO.setUpdateDate(new Date());

    }

}