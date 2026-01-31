package br.mt.artists.service;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class StorageService {
    private final MinioClient minioClient;
    private final MinioClient minioClientPublic;
    private final String bucket;

    public StorageService(@Qualifier("minioClient") MinioClient minioClient,
                          @Qualifier("minioClientPublic") MinioClient minioClientPublic,
                          @Value("${minio.bucket}") String bucket){
        this.minioClient = minioClient;
        this.minioClientPublic = minioClientPublic;
        this.bucket = bucket;
    }

    public String upload(String objectName, InputStream inputStream, String contentType){
        try{
            ensureBucketExists();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(inputStream, -1, 10 * 1024 * 1024)
                            .contentType(contentType)
                            .build()
            );

            return objectName;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao realizar upload da capa do álbum");
        }
    }

    public String generatePresignedUrl(String objectName){
        try {
            // USA O CLIENTE PÚBLICO para gerar a URL
            String url = minioClientPublic.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(30*60) // 30 minutos
                            .build()
            );

            return url;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar URL pré-assinada", e);
        }
    }

    private void ensureBucketExists() throws Exception {
        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucket).build()
        );

        if(!exists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    public String getPresignedUrlZ(String objectName){
        try{
            return minioClientPublic.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.POST)
                            .bucket(bucket)
                            .object(objectName)
                            .expiry(10, TimeUnit.MINUTES)
                            .build()
            );

        }catch (Exception e){
            throw new RuntimeException("Error generating presigned URL", e);
        }
    }
}