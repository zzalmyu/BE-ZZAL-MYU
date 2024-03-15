package com.prgrms.zzalmyu.domain.image.presentation.controller;

import com.amazonaws.util.CollectionUtils;
import com.prgrms.zzalmyu.domain.image.application.*;
import com.prgrms.zzalmyu.domain.image.presentation.dto.req.ImageUploadRequestDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageDetailResponse;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final ImageUploadService imageUploadService;
    private final ImageRemoveService imageRemoveService;
    private final ImageSearchService imageSearchService;
    private final ImageMainService imageMainService;

    @ApiResponse(description = "짤 상세보기 페이지에 반환 될 값")
    @GetMapping("/{imageId}")
    public ImageDetailResponse getImage(@AuthenticationPrincipal User user, @PathVariable Long imageId) {
        return imageService.getImageDetail(imageId, user);
    }
    @ApiResponse(description = "전체 이미지 조회 (실사용) 또는 태그 검색")
    @GetMapping
    List<ImageResponseDto> getImages(@AuthenticationPrincipal User user,@RequestParam(name = "tagName", required = false) List<String> tagNames, @PageableDefault(page = 0, size = 15) Pageable pageable) {
        if (CollectionUtils.isNullOrEmpty(tagNames)) {
            if (user == null) {
                return imageMainService.getTopUserUsedImage(pageable);
            }
            return imageMainService.getRecommendedImage(user, pageable);
        }
        return imageSearchService.searchImages(tagNames, pageable);
    }

    @ApiResponse(description = "좋아요 누른 짤 전체 또는 태그 검색")
    @GetMapping("/like")
    public List<ImageResponseDto> getImageLikes(@AuthenticationPrincipal User user, @RequestParam(name = "tagName", required = false) List<String> tagNames, @PageableDefault(page = 0,size = 15) Pageable pageable) {
        if (CollectionUtils.isNullOrEmpty(tagNames)) {
            return imageService.getLikeImages(user, pageable);
        }
        return imageSearchService.searchLikeImages(user, tagNames, pageable);
    }

    @ApiResponse(description = "업로드한 짤 전체 또는 태그 검색")
    @GetMapping("/upload")
    public List<ImageResponseDto> getImageUploads(@AuthenticationPrincipal User user, @RequestParam(name = "tagName", required = false) List<String> tagNames, @PageableDefault(page = 0,size = 15) Pageable pageable) {
        if (CollectionUtils.isNullOrEmpty(tagNames)) {
            return imageService.getUploadImages(user, pageable);
        }
        return imageSearchService.searchUploadImages(user, tagNames, pageable);
    }

    @ApiResponse(description = "짤 좋아요 클릭")
    @PostMapping("/{imageId}/like")
    public ImageResponseDto likeImage(@AuthenticationPrincipal User user, @PathVariable Long imageId) {
        return imageService.likeImage(imageId, user);
    }

    @ApiResponse(description = "짤 좋아요 취소 클릭")
    @PostMapping("/{imageId}/like/cancel")
    public ImageResponseDto cancelLikeImage(@AuthenticationPrincipal User user, @PathVariable Long imageId) {
        return imageService.cancelLikeImage(imageId, user);
    }

    @ApiResponse(description = "짤 업로드")
    @PostMapping
    public AwsS3ResponseDto upload(@ModelAttribute ImageUploadRequestDto imageUploadRequestDto,
                                   @AuthenticationPrincipal User user) throws IOException {
        return imageUploadService.uploadImage(user, imageUploadRequestDto.getFile(), imageUploadRequestDto);
    }

    @ApiResponse(description = "(유저 본인이)업로드한 짤 삭제 ")
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Long> remove(@AuthenticationPrincipal User user,@PathVariable Long imageId) {
        imageRemoveService.deleteUploadImages(user, imageId);
        return ResponseEntity.ok(imageId);
    }

    @ApiResponse(description = "전체 이미지 조회(테스트용,삭제 예정)")
    @GetMapping("/all")
    List<AwsS3ResponseDto> getAllUploadImages(@PageableDefault(page = 0,size = 10) Pageable pageable) {
        return imageService.getAllImages(pageable);
    }



}
