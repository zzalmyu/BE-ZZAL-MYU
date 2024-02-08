package com.prgrms.zzalmyu.domain.image.presentation.controller;

import com.prgrms.zzalmyu.domain.image.application.AwsS3Service;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3RequestDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    @PostMapping("/resource")
    public AwsS3ResponseDto upload(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        return awsS3Service.upload(multipartFile, "upload");
    }

    @DeleteMapping("/resource")
    public void remove(@RequestBody AwsS3RequestDto awsS3RequestDto) {
        awsS3Service.remove(awsS3RequestDto);
    }
}
