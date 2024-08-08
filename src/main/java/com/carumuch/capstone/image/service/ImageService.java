package com.carumuch.capstone.image.service;

import com.carumuch.capstone.image.common.SetImageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {
    private final S3Client s3Client;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile image) throws IOException{

        String storePath = "C:/imgStore/";
        File Folder = new File(storePath);

        /*로컬 저장소가 없을 시 생성*/
        if(!Folder.exists()){
            try{
                Folder.mkdir();
            } catch (Exception e){
                e.getStackTrace();
            }
        }
        /*로컬 이미지 파일 업로드*/
        String imagePath = getImagePath(image);
        String savePath = storePath + imagePath;

        image.transferTo(new File(savePath));

        return imagePath;

        /*S3에 이미지 파일 업로드*/
        /*try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .contentType(image.getContentType())
                    .contentLength(image.getSize())
                    .key(imagePath)
                    .build();
            RequestBody requestBody = RequestBody.fromBytes(image.getBytes());
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException e) {
            System.out.println("cannot upload image" + e);
            throw new RuntimeException(e);
        }

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucket)
                .key(imagePath)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest).toString();*/

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

    public void deleteImage(String imageKey){
        String storePath = "C:/imgStore/";
        String savePath = storePath + imageKey;
        File checkImage = new File(savePath);
        if(checkImage.exists()){
            try {
                checkImage.delete();
            } catch(Exception e){
                e.getStackTrace();
            }
        }

    }
}
