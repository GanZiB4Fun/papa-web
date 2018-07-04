package com.ganzib.papa.sprider.thread;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-04
 * Time: 下午2:34
 * Email: ganzib4fun@gmail.com
 */
@Service("SpriderThreadPool")
public class SpriderThreadPool {
    private byte[] lock = new byte[0];
    @Value("#{configProperties['saveBillThread.corePoolSize']}")
    private int corePoolSize = 5;
    @Value("#{configProperties['saveBillThread.maximumPoolSize']}")
    private int maximumPoolSize = 10;
    private long keepAliveTimeInMillSeconds = 1000;
    @Value("#{configProperties['saveBillThread.queueSize']}")
    private int queueSize = 2048;
    private ThreadPoolExecutor threadPoolExecutor;

    private static final Logger logger = Logger.getLogger(SpriderThreadPool.class);

    @PostConstruct
    public void init() {
        synchronized (lock) {
            BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(
                    queueSize);
            threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize, keepAliveTimeInMillSeconds,
                    TimeUnit.MILLISECONDS, workQueue);
        }
    }

    public int addTask(Runnable task) {
        synchronized (lock) {
            if (task == null) {
                logger.warn("save article record task is null");
                return 0;
            }
            int activeCount = threadPoolExecutor.getActiveCount();
            int maxCount = threadPoolExecutor.getMaximumPoolSize();
            if (activeCount >= maxCount) {
                logger.info("The pool of save article record add [" + task + "] FAIL, work queue full");
                return 2;
            }
            threadPoolExecutor.execute(task);
            return 0;
        }
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setKeepAliveTimeInMillSeconds(long keepAliveTimeInMillSeconds) {
        this.keepAliveTimeInMillSeconds = keepAliveTimeInMillSeconds;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
