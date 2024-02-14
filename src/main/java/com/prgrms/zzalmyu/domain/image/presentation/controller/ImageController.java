package com.prgrms.zzalmyu.domain.image.presentation.controller;

import com.prgrms.zzalmyu.domain.image.application.ImageRemoveService;
import com.prgrms.zzalmyu.domain.image.application.ImageSearchService;
import com.prgrms.zzalmyu.domain.image.application.ImageService;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3RequestDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageDetailResponse;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final ImageRemoveService imageRemoveService;
    private final ImageSearchService imageSearchService;

    @GetMapping("/{id}")
    public ImageDetailResponse getImage(@AuthenticationPrincipal User user,@PathVariable Long id){
        return imageService.getImageDetail(id, user);
    }

    @GetMapping("/like")
    public List<AwsS3ResponseDto>getImageLikes(@AuthenticationPrincipal User user) {
        return imageService.getLikeImages(user);
    }

    @GetMapping("/upload")
    public List<AwsS3ResponseDto>getImageUploads(@AuthenticationPrincipal User user) {
        return imageService.getUploadImages(user);
    }

    @PostMapping("/user/like")
    List<AwsS3ResponseDto> searchLikeImages(@AuthenticationPrincipal User user, List<Long> tagIdList) {
        List<AwsS3ResponseDto> result = imageSearchService.searchLikeImages(user, tagIdList);
        return result;
    }

    @PostMapping("/user/my-upload")
    List<AwsS3ResponseDto> searchUploadImages(@AuthenticationPrincipal User user, List<Long> tagIdList) {
        List<AwsS3ResponseDto> result = imageSearchService.searchUploadImages(user, tagIdList);
        return result;
    }

    @PostMapping("/resource")
    public AwsS3ResponseDto upload(@RequestPart("file") MultipartFile multipartFile, @AuthenticationPrincipal User user) throws IOException {
        return imageService.uploadImage(user, multipartFile);
    }

    @DeleteMapping("/resource")
    public ResponseEntity remove(@RequestBody AwsS3RequestDto awsS3RequestDto, @AuthenticationPrincipal User user) {
        imageRemoveService.deleteUploadImages(user, awsS3RequestDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
