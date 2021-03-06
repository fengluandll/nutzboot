package io.nutz.demo.simple;

import org.nutz.boot.NbApp;
import org.nutz.boot.starter.fastdfs.FastdfsService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

@IocBean(create = "init")
public class MainLauncher {
    private static final Log log = Logs.get();
    @Inject
    private FastdfsService fastdfsService;

    public static void main(String[] args) {
        new NbApp().setPrintProcDoc(true).start();
    }

    public void init() {
        try {
            String fileUrl = fastdfsService.uploadFile("大鲨鱼宇宙最帅".getBytes(), "txt", null);
            if (Strings.isNotBlank(fileUrl)) {
                log.info("上传成功::" + fileUrl);
                log.info("文件Token访问路径::" + fastdfsService.getFileTokenUrl(fileUrl));
            } else {
                log.info("上传失败");
            }
            byte[] fileTxt = fastdfsService.downLoadFile(fileUrl);
            log.info("下载文件:::" + new String(fileTxt));
            File file = File.createTempFile("test", ".txt");
            Files.write(file, "wizzer.cn".getBytes());
            log.info("文件绝对路径::" + file.getAbsolutePath());
            String fileUrl2 = fastdfsService.uploadFile(file.getAbsolutePath(), "txt", null);
            if (Strings.isNotBlank(fileUrl2)) {
                log.info("上传成功::" + fileUrl2);
                log.info("文件Token访问路径::" + fastdfsService.getFileTokenUrl(fileUrl2));
            } else {
                log.info("上传失败");
            }
//            OutputStream outputStream = new ByteArrayOutputStream();
//            fastdfsService.downLoadFile(fileUrl2, outputStream);
            InputStream inputStreamDemo = this.getClass().getResourceAsStream("/demo.png");
            InputStream inputStreamWater = this.getClass().getResourceAsStream("/water.png");
            byte[] demo = Streams.readBytes(inputStreamDemo);
            byte[] water = Streams.readBytes(inputStreamWater);
            String imageUrl = fastdfsService.uploadImage(demo, water, "png", null);
            log.info("原图路径:::" + fastdfsService.getImageTokenUrl(imageUrl, 0));
            log.info("水印图路径:::" + fastdfsService.getImageTokenUrl(imageUrl, 1));
            log.info("缩略图路径:::" + fastdfsService.getImageTokenUrl(imageUrl, 2));
        } catch (Exception e) {
            log.error(e);
        }
    }
}
