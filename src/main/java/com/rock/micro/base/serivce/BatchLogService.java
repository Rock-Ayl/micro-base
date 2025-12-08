package com.rock.micro.base.serivce;

import com.rock.micro.base.data.doc.ApiLogDoc;

/**
 * 批量写入 api 日志服务
 *
 * @Author ayl
 * @Date 2025-08-08
 */
public interface BatchLogService {

    /**
     * 将日志实体丢入的方法
     *
     * @param apiLogDoc api日志实体
     */
    void addLog(ApiLogDoc apiLogDoc);

}