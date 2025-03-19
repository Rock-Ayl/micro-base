package com.rock.micro.base.util;

import com.rock.micro.base.data.User;
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

}
