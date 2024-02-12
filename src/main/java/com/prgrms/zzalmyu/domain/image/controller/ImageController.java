package com.prgrms.zzalmyu.domain.image.controller;

import com.prgrms.zzalmyu.domain.image.application.ImageSearchService;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ImageController {
    private final ImageSearchService imageSearchService;

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
}
