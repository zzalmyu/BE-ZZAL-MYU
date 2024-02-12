package com.prgrms.zzalmyu.domain.tag.application;

import com.prgrms.zzalmyu.domain.tag.infrastructure.TagRepository;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.TagResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{
    private final TagRepository tagRepository;
    private static final int RANK_NUM = 5;
    @Override
    public List<TagResponseDto> getTopTags(User user) {
        return tagRepository.getTopTagsFromUserUsed(RANK_NUM);
    }

    @Override
    public List<TagResponseDto> getTopTagsFromLikeImages(User user) {
        return tagRepository.getTopTagsFromLikedImages(user.getId(), RANK_NUM);
    }

    @Override
    public void getTopTagsFromUploadImages(User user) {

    }

    @Override
    public void createTag(User user, String tagName) {

    }
}
