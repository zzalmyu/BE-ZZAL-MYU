package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageTagRepository;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageSearchServiceImpl implements ImageSearchService {
    private final ImageTagRepository imageTagRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public List<AwsS3ResponseDto> searchLikeImages(User user, List<Long> tagIdList) {
        List<Image> imageList = imageTagRepository.findLikeImagesByUserIdAndTagIdList(user.getId(), tagIdList);
        return convertListToResponseDtoList(imageList);
    }

    @Override
    public List<AwsS3ResponseDto> searchUploadImages(User user, List<Long> tagIdList) {
        List<Image> imageList = imageTagRepository.findUploadImagesByUserIdAndTagIdList(user.getId(), tagIdList);
        return convertListToResponseDtoList(imageList);
    }

    private List<AwsS3ResponseDto> convertListToResponseDtoList(List<Image> imageList) {
        return imageList.stream()
                .map(AwsS3ResponseDto::new)
                .toList();
    }
}
