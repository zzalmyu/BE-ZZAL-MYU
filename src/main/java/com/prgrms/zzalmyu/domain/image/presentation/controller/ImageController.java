package com.prgrms.zzalmyu.domain.image.presentation.controller;

import com.prgrms.zzalmyu.domain.image.application.ImageRemoveService;
import com.prgrms.zzalmyu.domain.image.application.ImageSearchService;
import com.prgrms.zzalmyu.domain.image.application.ImageService;
import com.prgrms.zzalmyu.domain.image.application.ImageUploadService;
import com.prgrms.zzalmyu.domain.image.presentation.dto.req.TagListRequestDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3RequestDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageDetailResponse;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final ImageUploadService imageUploadService;
    private final ImageRemoveService imageRemoveService;
    private final ImageSearchService imageSearchService;

    @ApiResponse(description = "짤 상세보기 페이지에 반환 될 값")
    @GetMapping("/{imageId}")
    public ImageDetailResponse getImage(@AuthenticationPrincipal User user, @PathVariable Long imageId) {
        return imageService.getImageDetail(imageId, user);
    }
    @ApiResponse(description = "좋아요 누른 짤 리스트 반환")
    @GetMapping("/like")
    public List<AwsS3ResponseDto> getImageLikes(@AuthenticationPrincipal User user) {
        return imageService.getLikeImages(user);
    }
    @ApiResponse(description = "업로드한 짤 리스트 반환")
    @GetMapping("/upload")
    public List<AwsS3ResponseDto> getImageUploads(@AuthenticationPrincipal User user) {
        return imageService.getUploadImages(user);
    }

    @ApiResponse(description = "짤 좋아요 버튼 클릭")
    @PostMapping("/{imageId}/like")
    public void likeImage(@AuthenticationPrincipal User user,@PathVariable Long imageId){
        imageService.likeImage(imageId,user);
    }

    @ApiResponse(description = "좋아요 누른 짤 페이지에서 태그 검색")
    @PostMapping("/me/like")
    List<AwsS3ResponseDto> searchLikeImages(@AuthenticationPrincipal User user, @RequestBody TagListRequestDto dto) {
        return imageSearchService.searchLikeImages(user, dto.getTagIdList());
    }

    @ApiResponse(description = "업로드한 짤 페이지에서 태그 검색")
    @PostMapping("/me/upload")
    List<AwsS3ResponseDto> searchUploadImages(@AuthenticationPrincipal User user, @RequestBody TagListRequestDto dto) {
        return imageSearchService.searchUploadImages(user, dto.getTagIdList());
    }

    @ApiResponse(description = "짤 업로드")
    @PostMapping
    public AwsS3ResponseDto upload(@RequestPart(name = "file") MultipartFile file,
                                   @RequestPart(name = "dto") TagListRequestDto tagListRequestDto,
                                   @AuthenticationPrincipal User user) throws IOException {
        return imageUploadService.uploadImage(user, file, tagListRequestDto.getTagIdList());
    }

    @ApiResponse(description = "(유저 본인이)업로드한 짤 삭제 ")
    @DeleteMapping
    public ResponseEntity remove(@RequestBody AwsS3RequestDto awsS3RequestDto, @AuthenticationPrincipal User user) {
        imageRemoveService.deleteUploadImages(user, awsS3RequestDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
