package com.yunting.client.common.utils;

import com.yunting.client.common.config.minio.Fileinfo;
import com.yunting.client.common.exception.AppException;
import com.yunting.client.common.results.ResponseEnum;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yunting.client.common.utils.FS.IMG_TYPE;

@Component("MinIoUtils")
@Slf4j
public class MinIoUtils {

    @Resource(name = "minioClient")
    MinioClient minioClient;


    /***
     * 检测bucket是否存在
     * <p>
     * 不存在就创建bucket
     */
    public void detectBucket(String bucket) throws IOException {
        try {
            BucketExistsArgs existsArgs = BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build();
            Boolean isExists = minioClient.bucketExists(existsArgs);

            // 公共访问策略 JSON
            String policyJson = "{"
                    + "  \"Version\": \"2012-10-17\","
                    + "  \"Statement\": ["
                    + "    {"
                    + "      \"Effect\": \"Allow\","
                    + "      \"Principal\": \"*\","
                    + "      \"Action\": ["
                    + "        \"s3:GetBucketLocation\","
                    + "        \"s3:ListBucket\""
                    + "      ],"
                    + "      \"Resource\": ["
                    + "        \"arn:aws:s3:::" + bucket + "\""
                    + "      ]"
                    + "    },"
                    + "    {"
                    + "      \"Effect\": \"Allow\","
                    + "      \"Principal\": \"*\","
                    + "      \"Action\": \"s3:GetObject\","
                    + "      \"Resource\": ["
                    + "        \"arn:aws:s3:::" + bucket + "/*\""
                    + "      ]"
                    + "    }"
                    + "  ]"
                    + "}";

            MakeBucketArgs buildBucket = MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build();

            SetBucketPolicyArgs policy = SetBucketPolicyArgs.builder()
                    .bucket(bucket)
                    .config(policyJson)
                    .build();
            minioClient.setBucketPolicy(policy);
            if (isExists.booleanValue() == false) {
                minioClient.makeBucket(buildBucket);
                log.info("bucket已创建:" + bucket);
            }
        } catch (AppException e) {
            log.error("上传至minio服务端失败,请检查详细情况", new AppException(e.getCode(), e.getMessage()));
            throw new AppException(e.getCode(), e.getMessage());
        } catch (ServerException e) {
            log.error("minio服务器异常,请检查相应服务器", new AppException(ResponseEnum.MINIO_SERVER_ERROR));
            throw new AppException(ResponseEnum.MINIO_SERVER_ERROR);
        } catch (InsufficientDataException e) {
            log.error("数据不足无法完成该种请求操作,请检查上传对象数据是否完整或有丢失", new AppException(ResponseEnum.MINIO_INSUFFICIENT_DATA_ERROR));
            throw new AppException(ResponseEnum.MINIO_INSUFFICIENT_DATA_ERROR);
        } catch (ErrorResponseException e) {
            log.error("错误的响应", new AppException(ResponseEnum.MINIO_ERROR_RESPONSE));
            throw new AppException(ResponseEnum.MINIO_ERROR_RESPONSE);
        } catch (NoSuchAlgorithmException e) {
            log.error("特定加密算法在环境中不可用", new AppException(ResponseEnum.MINIO_NO_SUCH_ALGORITHM_ERROR));
            throw new AppException(ResponseEnum.MINIO_NO_SUCH_ALGORITHM_ERROR);
        } catch (InvalidKeyException e) {
            log.error("无效的key,请检查key和授权令牌是否正确", new AppException(ResponseEnum.MINIO_INVALID_KEY_ERROR));
            throw new AppException(ResponseEnum.MINIO_INVALID_KEY_ERROR);
        } catch (InvalidResponseException e) {
            log.error("无效或未知的响应错误", new AppException(ResponseEnum.MINIO_INVALID_ERROR_RESPONSE));
            throw new AppException(ResponseEnum.MINIO_INVALID_ERROR_RESPONSE);
        } catch (XmlParserException e) {
            log.error("xml文件解析失败", new AppException(ResponseEnum.MINIO_XML_PARSE_ERROR));
            throw new AppException(ResponseEnum.MINIO_XML_PARSE_ERROR);
        } catch (InternalException e) {
            log.error("MinIO服务内部发生错误或异常", new AppException(ResponseEnum.MINIO_INTERNAL_ERROR));
            throw new AppException(ResponseEnum.MINIO_INTERNAL_ERROR);
        }
    }

    /***
     * 获取该图片预签url
     */
    public String getPreviewUrl(String bucket, String objectName) throws IOException {

        GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                .object(objectName)
                .bucket(bucket)
                .method(Method.GET)
                .build();
        try {
            String objectUrl = this.minioClient.getPresignedObjectUrl(urlArgs);
            return objectUrl;
        } catch (AppException e) {
            log.error("上传至minio服务端失败,请检查详细情况", new AppException(e.getCode(), e.getMessage()));
            throw new AppException(e.getCode(), e.getMessage());
        } catch (ServerException e) {
            log.error("minio服务器异常,请检查相应服务器", new AppException(ResponseEnum.MINIO_SERVER_ERROR));
            throw new AppException(ResponseEnum.MINIO_SERVER_ERROR);
        } catch (InsufficientDataException e) {
            log.error("数据不足无法完成该种请求操作,请检查上传对象数据是否完整或有丢失", new AppException(ResponseEnum.MINIO_INSUFFICIENT_DATA_ERROR));
            throw new AppException(ResponseEnum.MINIO_INSUFFICIENT_DATA_ERROR);
        } catch (ErrorResponseException e) {
            log.error("错误的响应", new AppException(ResponseEnum.MINIO_ERROR_RESPONSE));
            throw new AppException(ResponseEnum.MINIO_ERROR_RESPONSE);
        } catch (NoSuchAlgorithmException e) {
            log.error("特定加密算法在环境中不可用", new AppException(ResponseEnum.MINIO_NO_SUCH_ALGORITHM_ERROR));
            throw new AppException(ResponseEnum.MINIO_NO_SUCH_ALGORITHM_ERROR);
        } catch (InvalidKeyException e) {
            log.error("无效的key,请检查key和授权令牌是否正确", new AppException(ResponseEnum.MINIO_INVALID_KEY_ERROR));
            throw new AppException(ResponseEnum.MINIO_INVALID_KEY_ERROR);
        } catch (InvalidResponseException e) {
            log.error("无效或未知的响应错误", new AppException(ResponseEnum.MINIO_INVALID_ERROR_RESPONSE));
            throw new AppException(ResponseEnum.MINIO_INVALID_ERROR_RESPONSE);
        } catch (XmlParserException e) {
            log.error("xml文件解析失败", new AppException(ResponseEnum.MINIO_XML_PARSE_ERROR));
            throw new AppException(ResponseEnum.MINIO_XML_PARSE_ERROR);
        } catch (InternalException e) {
            log.error("MinIO服务内部发生错误或异常", new AppException(ResponseEnum.MINIO_INTERNAL_ERROR));
            throw new AppException(ResponseEnum.MINIO_INTERNAL_ERROR);
        }
    }

    /***
     * 列出该bucket中的所有文件和目录
     * @param bucket 哪个bucket
     * @return 文件的信息(文件名, 是否是目录, 时效性访问链接)
     */
    public List<Fileinfo> listFiles(String bucket) {
        Iterable<Result<Item>> results = this.minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).recursive(true).build());
        List<Fileinfo> infos = new ArrayList();

        results.forEach((r) -> {
            Fileinfo info = new Fileinfo();

            try {
                Item item = r.get();
                info.setFilename(item.objectName());
                info.setDirectory(item.isDir());

                GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                        .object(item.objectName())
                        .bucket(bucket)
                        .method(Method.GET)
                        .expiry(5, TimeUnit.MINUTES)
                        .build();
                String objectUrl = this.minioClient.getPresignedObjectUrl(urlArgs); //5分钟该文件的访问链接
                info.setPreviewUrl(objectUrl);
                infos.add(info);
            } catch (Exception var4) {
                log.error("获取文件列表失败,错误信息" + var4);
            }

        });
        return infos;
    }


    /***
     * 找出某bucket下某文件夹下的所有文件
     * @param fileName 文件夹名称
     * @param bucket    哪个bucket
     */
    public List<Fileinfo> listFiles(String fileName, String bucket) {

        return null;
    }


    /***
     * 在bucket中创建一个空目录
     * <p>
     * 若存在则不会在创建 ,但也不报错
     * @param bucket 哪个bucket
     * @param directory 目录名称
     * @throws IOException
     */
    public void createDirectory(String bucket, String directory) throws IOException {
        // 创建一个空文件作为目录占位符
        String emptyFileName = directory + "/";  // 用于模拟文件夹的占位符文件
        byte[] emptyFileContent = new byte[0];  // 空文件内容

        InputStream emptyFileInputStream = new ByteArrayInputStream(emptyFileContent);

        // 上传空文件，模拟创建文件夹
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(emptyFileName)  // 上传的路径（带文件夹结构）
                    .stream(emptyFileInputStream, emptyFileInputStream.available(), -1)
                    .contentType("application/octet-stream")  // 设置内容类型为二进制
                    .build());
        } catch (AppException e) {
            log.error("上传至minio服务端失败,请检查详细情况", new AppException(e.getCode(), e.getMessage()));
            throw new AppException(e.getCode(), e.getMessage());
        } catch (ServerException e) {
            log.error("minio服务器异常,请检查相应服务器", new AppException(ResponseEnum.MINIO_SERVER_ERROR));
            throw new AppException(ResponseEnum.MINIO_SERVER_ERROR);
        } catch (InsufficientDataException e) {
            log.error("数据不足无法完成该种请求操作,请检查上传对象数据是否完整或有丢失", new AppException(ResponseEnum.MINIO_INSUFFICIENT_DATA_ERROR));
            throw new AppException(ResponseEnum.MINIO_INSUFFICIENT_DATA_ERROR);
        } catch (ErrorResponseException e) {
            log.error("错误的响应", new AppException(ResponseEnum.MINIO_ERROR_RESPONSE));
            throw new AppException(ResponseEnum.MINIO_ERROR_RESPONSE);
        } catch (NoSuchAlgorithmException e) {
            log.error("特定加密算法在环境中不可用", new AppException(ResponseEnum.MINIO_NO_SUCH_ALGORITHM_ERROR));
            throw new AppException(ResponseEnum.MINIO_NO_SUCH_ALGORITHM_ERROR);
        } catch (InvalidKeyException e) {
            log.error("无效的key,请检查key和授权令牌是否正确", new AppException(ResponseEnum.MINIO_INVALID_KEY_ERROR));
            throw new AppException(ResponseEnum.MINIO_INVALID_KEY_ERROR);
        } catch (InvalidResponseException e) {
            log.error("无效或未知的响应错误", new AppException(ResponseEnum.MINIO_INVALID_ERROR_RESPONSE));
            throw new AppException(ResponseEnum.MINIO_INVALID_ERROR_RESPONSE);
        } catch (XmlParserException e) {
            log.error("xml文件解析失败", new AppException(ResponseEnum.MINIO_XML_PARSE_ERROR));
            throw new AppException(ResponseEnum.MINIO_XML_PARSE_ERROR);
        } catch (InternalException e) {
            log.error("MinIO服务内部发生错误或异常", new AppException(ResponseEnum.MINIO_INTERNAL_ERROR));
            throw new AppException(ResponseEnum.MINIO_INTERNAL_ERROR);
        }

        emptyFileInputStream.close();
        log.info("创建目录成功,名称:" + directory);
    }

    /***
     * 计算文件的SHA-256哈希值
     * @param inputStream 文件对象的流
     * @return 文件的SHA-256哈希值
     */
    public String calculateSHA256(InputStream inputStream) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int read;
            while ((read = inputStream.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] hashBytes = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            inputStream.close();
            return hexString.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            log.info("文件哈希值计算失败", e);
            return null;
        }
    }


    private final Map NONE_MAP = new HashMap();

    /***
     * 上传文件到minio服务器
     * @param file 上传的文件
     * @param bucket 上传到哪个桶子里
     * @param obj 上传的图片
     * @return 上传的该文件的预览url
     * @throws IOException 文件流读取的时抛出的异常
     */
    @Transactional(rollbackFor = Exception.class)
    public String uploadFile(MultipartFile file, String bucket, String obj) throws IOException {
        InputStream inputStream = file.getInputStream();// 要上传文件的文件流

        //要上传的文件对象构建
        PutObjectArgs args = PutObjectArgs.builder()
                .stream(inputStream, file.getSize(), PutObjectArgs.MIN_MULTIPART_SIZE) //上传文件流
                .bucket(bucket)
                .object(obj)              //桶 + 上传文件路径
                .userMetadata(NONE_MAP)                 //文件元数据
                .contentType(file.getContentType())     //文件类型
                .build();

        // 上传文件到minio服务端
        try {
            minioClient.putObject(args);

            GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                    .object(obj)
                    .bucket(bucket)
                    .method(Method.GET)
                    .build();
            String objectUrl = this.minioClient.getPresignedObjectUrl(urlArgs);
            int i = objectUrl.indexOf(IMG_TYPE);
            String img = objectUrl.substring(0, i) + IMG_TYPE; //切割了元数据的图片访问路径+图片后缀名 = 预览url
            log.info("上传成功,文件路径为:" + objectUrl + "上传的图片" + obj);
            //关闭minio客户端
            inputStream.close();
            return img;
        } catch (AppException e) {
            log.error("上传至minio服务端失败,请检查详细情况", new AppException(e.getCode(), e.getMessage()));
            throw new AppException(e.getCode(), e.getMessage());
        } catch (ServerException e) {
            log.error("minio服务器异常,请检查相应服务器", new AppException(ResponseEnum.MINIO_SERVER_ERROR));
            throw new AppException(ResponseEnum.MINIO_SERVER_ERROR);
        } catch (InsufficientDataException e) {
            log.error("数据不足无法完成该种请求操作,请检查上传对象数据是否完整或有丢失", new AppException(ResponseEnum.MINIO_INSUFFICIENT_DATA_ERROR));
            throw new AppException(ResponseEnum.MINIO_INSUFFICIENT_DATA_ERROR);
        } catch (ErrorResponseException e) {
            log.error("错误的响应", new AppException(ResponseEnum.MINIO_ERROR_RESPONSE));
            throw new AppException(ResponseEnum.MINIO_ERROR_RESPONSE);
        } catch (NoSuchAlgorithmException e) {
            log.error("特定加密算法在环境中不可用", new AppException(ResponseEnum.MINIO_NO_SUCH_ALGORITHM_ERROR));
            throw new AppException(ResponseEnum.MINIO_NO_SUCH_ALGORITHM_ERROR);
        } catch (InvalidKeyException e) {
            log.error("无效的key,请检查key和授权令牌是否正确", new AppException(ResponseEnum.MINIO_INVALID_KEY_ERROR));
            throw new AppException(ResponseEnum.MINIO_INVALID_KEY_ERROR);
        } catch (InvalidResponseException e) {
            log.error("无效或未知的响应错误", new AppException(ResponseEnum.MINIO_INVALID_ERROR_RESPONSE));
            throw new AppException(ResponseEnum.MINIO_INVALID_ERROR_RESPONSE);
        } catch (XmlParserException e) {
            log.error("xml文件解析失败", new AppException(ResponseEnum.MINIO_XML_PARSE_ERROR));
            throw new AppException(ResponseEnum.MINIO_XML_PARSE_ERROR);
        } catch (InternalException e) {
            log.error("MinIO服务内部发生错误或异常", new AppException(ResponseEnum.MINIO_INTERNAL_ERROR));
            throw new AppException(ResponseEnum.MINIO_INTERNAL_ERROR);
        }
    }

}
