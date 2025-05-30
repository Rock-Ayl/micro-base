package com.rock.micro.base.util;

import com.rock.micro.base.data.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户 扩展工具包
 *
 * @Author ayl
 * @Date 2022-12-06
 */
public class UserExtraUtils {

    /**
     * 给用户登录信息脱敏
     *
     * @param user 用户实体
     * @return
     */
    public static <T extends User> void desensitization(T user) {
        //判空
        if (user == null) {
            //过
            return;
        }
        //脱敏
        user.setPwd(null);
    }

    /**
     * 生成一个用户简单的token
     *
     * @return
     */
    public static String creatUserToken(String userId) {
        //判空
        if (StringUtils.isBlank(userId)) {
            //过
            return "";
        }
        //组装并返回
        return userId + ":" + System.currentTimeMillis() + ":" + IdExtraUtils.genGUID();
    }

    /**
     * token 拆解出来的对象
     */
    @Getter
    @Setter
    public static class TokenObject {

        @ApiModelProperty("用户id")
        private String userId;

        @ApiModelProperty("生成的时间戳")
        private Long time;

        @ApiModelProperty("生成的uuid")
        private String uuId;

    }

    /**
     * 切割 token 为所需信息
     *
     * @param token 用户token
     * @return
     */
    public static TokenObject cutToken(String token) {
        //初始化
        TokenObject tokenObject = new TokenObject();
        //判空
        if (StringUtils.isBlank(token)) {
            //直接返回
            return tokenObject;
        }
        //拆分
        String[] split = token.split(":");
        //如果长度不对
        if (split.length != 3) {
            //直接返回
            return tokenObject;
        }
        //组装
        tokenObject.setUserId(split[0]);
        tokenObject.setTime(Long.valueOf(split[1]));
        tokenObject.setUuId(split[2]);
        //返回
        return tokenObject;
    }

}
