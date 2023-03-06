package space.jachen.gmall.product.controller;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import space.jachen.gmall.common.result.Result;

import java.util.UUID;

/**
 * 文件上传前端控制器
 *
 * @author JaChen
 * @date 2023/3/6 18:55
 */
@RestController
@RequestMapping("admin/product")
@Slf4j
public class FileUploadController {

    //  获取文件上传对应的地址
    @Value("${minio.endpointUrl}")
    public String endpointUrl;

    @Value("${minio.accessKey}")
    public String accessKey;

    @Value("${minio.secreKey}")
    public String secreKey;

    @Value("${minio.bucketName}")
    public String bucketName;

    //  文件上传控制器
    @PostMapping("/fileUpload")
    public Result<String> fileUpload(MultipartFile file) throws Exception{
        //  准备获取到上传的文件路径！
        String url = "";

        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(endpointUrl)
                        .credentials(accessKey, secreKey)
                        .build();
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if(isExist) {
            log.info("Bucket already exists.");
        } else {
            // 创建一个名为 asiatrip 的存储桶，用于存储照片的zip文件。
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        //  定义一个文件的名称 : 文件上传的时候，名称不能重复
        String fileName = System.currentTimeMillis()+ UUID.randomUUID().toString();
        // 使用putObject上传一个文件到存储桶中。
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                        file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
        //  文件上传之后的路径： http://jachen.com:9000/gmall/xxxxxx
        url = endpointUrl+"/"+bucketName+"/"+fileName;
        log.info("url:{}"+url);

        // 将文件上传之后的路径返回给页面
        return Result.ok(url);
    }
}
