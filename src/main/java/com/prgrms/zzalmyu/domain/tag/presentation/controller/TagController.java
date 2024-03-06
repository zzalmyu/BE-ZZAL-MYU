package com.prgrms.zzalmyu.domain.tag.presentation.controller;


import com.prgrms.zzalmyu.domain.tag.application.TagService;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.req.TagCreateRequestDto;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagMeResponseDto;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tag")
public class TagController {

    private final TagService tagService;

    @ApiResponse(description = "좋아요한 이미지 중 많이 사용한 태그 5개")
    @GetMapping("/me/like")
    public List<TagMeResponseDto> getTopTagsFromLikeImages(@AuthenticationPrincipal User user) {
        return tagService.getTopTagsFromLikeImages(user);
    }

    @ApiResponse(description = "업로드한 이미지 중 많이 사용한 태그 5개")
    @GetMapping("/me/upload")
    public List<TagMeResponseDto> getTopTagsFromUploadImages(@AuthenticationPrincipal User user) {
        return tagService.getTopTagsFromUploadImages(user);
    }

    @ApiResponse(description = "전체 유저가 (좋아요,업로드할 때)많이 사용한 태그 5개")
    @GetMapping("/popular")
    public List<TagResponseDto> getTopTagsFromUsersUsed() {
        return tagService.getTopTagsFromUsersUsed();
    }

    @ApiResponse(description = "메인페이지 검색창에서 태그 추천 (로그인/비로그인에 따라 다르게 나옴)")
    @GetMapping
    public List<TagResponseDto> getRecommendationTags(@AuthenticationPrincipal User user) {
        if (user == null) {
            return tagService.getTopTagsFromUsersUsed();
        }
        return tagService.getRecommendationTags(user);
    }

    @ApiResponse(description = "태그 생성")
    @PostMapping
    public TagResponseDto createTag(@RequestBody TagCreateRequestDto dto) {
        return tagService.createTag(dto.getName());
    }

    @ApiResponse(description = "태그 자동 검색")
    @GetMapping("/search")
    public List<TagResponseDto> searchTag(@RequestParam String keyword) {
        return tagService.searchTag(keyword);
    }

    @ApiResponse(description = "유저가 좋아요한 사진들의 태그 자동 검색")
    @GetMapping("/like/search")
    public List<TagResponseDto> searchTagFromLikeImages(@AuthenticationPrincipal User user, @RequestParam String keyword) {
        return tagService.searchTagFromLikeImages(user, keyword);
    }

    @ApiResponse(description = "유저가 업로드한 사진들의 태그 자동 검색")
    @GetMapping("/upload/search")
    public List<TagResponseDto> searchTagFromUploadImages(@AuthenticationPrincipal User user, @RequestParam String keyword) {
        return tagService.searchTagFromUploadImages(user, keyword);
    }

    @ApiResponse(description = "태그 사용 횟수 증가")
    @PostMapping("/use")
    public TagResponseDto increaseTagCount(@AuthenticationPrincipal User user, @RequestParam String newTagName) {
        return tagService.increaseTagCount(user, newTagName);
    }
}
