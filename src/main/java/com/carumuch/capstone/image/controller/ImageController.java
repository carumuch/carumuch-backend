package com.carumuch.capstone.image.controller;

import com.carumuch.capstone.global.common.ResponseDto;
import com.carumuch.capstone.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController implements ImageControllerDocs {

    private final ImageService imageService;

    /**
     *이미지 업로드
     */
    @Override
    public ResponseEntity<?> s3Upload(@RequestParam("image") MultipartFile image) throws IOException {
        String imageUrl = imageService.uploadImage(image);
        return ResponseEntity
                .status(CREATED)
                .body(ResponseDto.success(CREATED,imageUrl));
    }

    @Override
    public ResponseEntity<?> s3Load(@RequestParam("imageKey") String imageKey){
        String imageUrl = imageService.getImage(imageKey);
        return ResponseEntity
                .status(OK)
                .body(ResponseDto.success(OK,imageUrl));
    }

}
