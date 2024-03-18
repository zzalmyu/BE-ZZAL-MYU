package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageLikeRepository;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageTagRepository;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
import com.prgrms.zzalmyu.domain.tag.infrastructure.TagRepository;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageSearchServiceImpl implements ImageSearchService {
    private final ImageTagRepository imageTagRepository;
    private final ImageLikeRepository imageLikeRepository;
    private final TagRepository tagRepository;

    @Override
    public List<ImageResponseDto> searchLikeImages(User user, List<String> tagNames, Pageable pageable) {
        List<Long> tagIdList = tagRepository.findTagIdListByTagNameList(tagNames);
        List<Image> imageList = imageTagRepository.findLikeImagesByUserIdAndTagIdList(user.getId(), tagIdList, pageable);
        return convertLikeListToResponseDtoList(imageList);
    }

    @Override
    public List<ImageResponseDto> searchUploadImages(User user, List<String> tagNames, Pageable pageable) {
        List<Long> tagIdList = tagRepository.findTagIdListByTagNameList(tagNames);
        return imageTagRepository.findUploadImagesByUserIdAndTagIdList(user.getId(), tagIdList, pageable);
    }

    @Override
    public List<ImageResponseDto> searchImagesByUser(List<String> tagNames, User user, Pageable pageable) {
        List<Long> tagIdList = tagRepository.findTagIdListByTagNameList(tagNames);
        return imageTagRepository.findImagesByTagIdListAndUser(tagIdList, user.getId(), pageable);
    }

    @Override
    public List<ImageResponseDto> searchImages(List<String> tagNames, Pageable pageable) {
        List<Long> tagIdList = tagRepository.findTagIdListByTagNameList(tagNames);
        return imageTagRepository.findImagesByTagIdList(tagIdList, pageable).stream()
                .map(image -> ImageResponseDto.of(image, false))
                .toList();
    }

    private List<ImageResponseDto> convertLikeListToResponseDtoList(List<Image> imageList) {
        return imageList.stream()
                .map(image -> ImageResponseDto.of(image, true))
                .toList();
    }
}
