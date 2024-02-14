package com.prgrms.zzalmyu.domain.tag.presentation.controller;


import com.prgrms.zzalmyu.domain.tag.application.TagService;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TagController {

    private final TagService tagService;

    @GetMapping("/user/like/tag")
    public List<TagResponseDto> getTopTagsFromLikeImages(@AuthenticationPrincipal User user) {
        return tagService.getTopTagsFromLikeImages(user);
    }

    @GetMapping("/user/my/upload/tag")
    public List<TagResponseDto> getTopTagsFromUploadImages(@AuthenticationPrincipal User user) {
        return tagService.getTopTagsFromUploadImages(user);
    }

    @GetMapping("/tag/popular")
    public List<TagResponseDto> getTopTagsFromUsersUsed() {
        return tagService.getTopTagsFromUsersUsed();
    }

    @PostMapping("/user/upload/tag")
    public TagResponseDto createTag(@RequestBody String tagName) {
        return tagService.createTag(tagName);
    }
}
