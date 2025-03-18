package com.rock.micro.base.util;

import com.rock.micro.base.data.UserDO;

/**
 * 用户 扩展工具包
 *
 * @Author ayl
 * @Date 2022-12-06
 */
public class UserExtraUtils {

    /**
     * 给用户实体脱敏
     *
     * @param userDO 用户实体
     * @return
     */
    public static <T extends UserDO> void desensitization(T userDO) {
        //判空
        if (userDO == null) {
            //过
            return;
        }
        //脱敏
        userDO.setPwd(null);
    }

}
