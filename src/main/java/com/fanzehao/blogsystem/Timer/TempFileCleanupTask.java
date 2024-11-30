package com.fanzehao.blogsystem.Timer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

@Component
public class TempFileCleanupTask {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TempFileCleanupTask.class);
    @Value("${temp.directory}")
    private String tempDirectory;
    //秒 分 时 日 月 星期 年(可选)
    //为了测试5分钟执行一次
    @Scheduled(cron = "0 0 * * * ?")
    //@Scheduled用法
    public void cleanupTempFiles() {
        logger.info("开始清理临时文件");
        File file = new File(tempDirectory);
        logger.info(file.getAbsolutePath());
        if (file.exists() && file.isDirectory()) {
            for (File tempFile : Objects.requireNonNull(file.listFiles())) {
                if (tempFile.isFile()) {
                    logger.info("删除临时文件：" + tempFile.getName());
                    if (tempFile.delete())
                        logger.info("删除成功");
                }
            }
        }
    }
}
