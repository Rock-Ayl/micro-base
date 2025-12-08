package com.rock.micro.base.serivce.impl;

import com.rock.micro.base.data.doc.ApiLogDoc;
import com.rock.micro.base.serivce.ApiLogDocService;
import com.rock.micro.base.serivce.BatchLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class BatchLogServiceImpl implements BatchLogService {

    private static final Logger logger = LoggerFactory.getLogger(BatchLogServiceImpl.class);

    @Autowired
    private ApiLogDocService apiLogDocService;

    //每批最多写 50 条
    private static final int BATCH_SIZE = 50;

    //3秒刷新一次
    private static final int FLUSH_INTERVAL_MS = 3000;

    //日志队列,等待插入数据库
    private final BlockingQueue<ApiLogDoc> logQueue = new LinkedBlockingQueue<>();

    @Override
    public void addLog(ApiLogDoc apiLogDoc) {
        //不阻塞,满了就丢弃
        this.logQueue.offer(apiLogDoc);
    }

    /**
     * 启动一个线程，负责批量写入日志
     */
    @PostConstruct
    public void startBatchWriter() {

        /**
         * 一个线程
         */

        Thread writerThread = new Thread(() -> {

            /**
             * 参数
             */

            //列表,即将插入数据库
            List<ApiLogDoc> createLogDocList = new ArrayList<>(BATCH_SIZE);
            //上次刷新时间
            long lastFlushTime = System.currentTimeMillis();

            try {

                /**
                 * 死循环,不断读取日志并插入
                 */

                while (true) {

                    //拉取实体
                    ApiLogDoc logEntity = logQueue.poll(FLUSH_INTERVAL_MS, TimeUnit.MILLISECONDS);
                    //如果存在试题
                    if (logEntity != null) {
                        //插入列表
                        createLogDocList.add(logEntity);
                    }
                    //当前时间
                    long now = System.currentTimeMillis();
                    //如果 单次插入达到目标size or 间隔超过指定时间
                    if (createLogDocList.size() >= BATCH_SIZE || (now - lastFlushTime >= FLUSH_INTERVAL_MS && !createLogDocList.isEmpty())) {
                        try {
                            //批量写入
                            apiLogDocService.create(createLogDocList);
                        } catch (Exception e) {
                            logger.warn("批量写日志失败: {}", e.getMessage());
                        }
                        //清理列表
                        createLogDocList.clear();
                        //更新上次刷新时间
                        lastFlushTime = now;
                    }
                }

            } catch (InterruptedException e) {
                logger.error("api log 写入线程崩溃: {}", e.getMessage());
                //线程中断标记
                Thread.currentThread().interrupt();
            }

        }, "mongo-api-log-writer");
        //守护线程
        writerThread.setDaemon(true);
        //启动
        writerThread.start();
    }

}