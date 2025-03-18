package com.rock.micro.base.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Mongo 实体的基底
 *
 * @Author ayl
 * @Date 2022-03-10
 */
@Getter
@Setter
public class BaseDocument extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
}
