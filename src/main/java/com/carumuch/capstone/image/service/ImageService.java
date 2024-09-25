package com.carumuch.capstone.image.service;

import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.image.common.SetImageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {
    private final S3Client s3Client;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile image){
        /*로컬 이미지 파일 업로드*/
        /*
        try {
            String filePath = Paths.get(
                    System.getProperty("user.dir"),
                    "src/main/resources/static").toString();



            String imagePath = getImagePath(image);
            String savePath = filePath + imagePath;

            image.transferTo(new File(savePath));

            return imagePath;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        */

        /*S3에 이미지 파일 업로드*/
        String imagePath = getImagePath(image);
        try {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .contentType(image.getContentType())
                    .contentLength(image.getSize())
                    .key(imagePath)
                    .build();
            RequestBody requestBody = RequestBody.fromBytes(image.getBytes());
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucket)
                .key(imagePath)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest).toString();

    }

    /*이미지 파일 업로드 시 파일 저장 경로와 이름으로 이미지 로드 시 필요한 이미지 키를 생성*/
    public String getImagePath(MultipartFile image){
        return SetImageKey.buildImageKey(Objects.requireNonNull(image.getOriginalFilename()));
    }

    /*이미지 로드*/
    public String getImage(String imageKey){
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucket)
                .key(imageKey)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }

    public void deleteImage(String imagePath) {
        //로컬저장소에서 이미지 삭제
        /*
        String filePath = Paths.get(
                System.getProperty("user.dir"),
                "src/main/resources/static").toString();
        String savePath = filePath + imageKey;
        File checkImage = new File(savePath);
        if(checkImage.exists()){
            try {
                checkImage.delete();
            } catch(Exception e){
                e.getStackTrace();
            }
        }
         */
        // S3에서 이미지 삭제
        String imageKey = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        System.out.println(imageKey);
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                            .bucket(bucket)
                                    .key(imageKey).build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
