package com.harsh.Ecom.Service;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class MinioService {
    private final MinioClient minioClient;

    public String urlProvider(String objectName){
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                            .bucket("product-images")
                            .object(objectName)
                            .expiry(5, TimeUnit.MINUTES)
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException |
                 ServerException e) {
            throw new RuntimeException(e);
        }
    }

    public String putImage(MultipartFile image){
        String objectName = "product" + image.getName() + UUID.randomUUID();
        try {
            minioClient.putObject(PutObjectArgs.builder()
                            .bucket("product-images")
                            .object(objectName)
                            .stream(
                                    image.getInputStream(),
                                    image.getSize(),
                                    -1
                            )
                            .contentType(image.getContentType())
                    .build());
        } catch (ErrorResponseException | IOException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e)
        {
            throw new RuntimeException(e);
        }
        return objectName;
    }

    public GetObjectResponse getImage(String objectName){
        try{
            return minioClient.getObject(GetObjectArgs.builder()
                            .bucket("product-images")
                            .object(objectName)
                    .build());
        } catch (ServerException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeImage(String objectName){
        try{
            minioClient.removeObject(RemoveObjectArgs.builder()
                            .bucket("product-images")
                            .object(objectName)
                    .build());
        } catch (ServerException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
    }
}
