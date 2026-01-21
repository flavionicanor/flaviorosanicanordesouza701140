package br.mt.artists.service;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class StorageService {
    private final MinioClient minioClient;
    private final String bucket;

    public StorageService(MinioClient minioClient, @Value("${minio.bucket}") String bucket){
        this.minioClient = minioClient;
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
            throw new RuntimeException("Error uploading file", e);
        }
    }

    public String generatePresignedUrl(String objectName){
        try{
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(30 * 60) // 30 minutes
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error generating presigned URL", e);
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

    public String getPresignedUrl(String objectName){
        try{
            return minioClient.getPresignedObjectUrl(
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
