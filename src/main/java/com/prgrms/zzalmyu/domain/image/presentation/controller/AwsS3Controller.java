package com.prgrms.zzalmyu.domain.image.presentation.controller;

import com.prgrms.zzalmyu.domain.image.application.AwsS3Service;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3RequestDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    @PostMapping("/resource")
    public AwsS3ResponseDto upload(@RequestPart("file") MultipartFile multipartFile, @AuthenticationPrincipal User user) throws IOException {
        return awsS3Service.upload(user, multipartFile);
    }

    @DeleteMapping("/resource")
    public ResponseEntity remove(@RequestBody AwsS3RequestDto awsS3RequestDto, @AuthenticationPrincipal User user) {
        awsS3Service.remove(user, awsS3RequestDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
