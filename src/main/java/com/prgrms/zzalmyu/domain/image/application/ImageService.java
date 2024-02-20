package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageDetailResponse;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    /**
     * 짤의 상세 페이지를 볼 수 있다.
     */
    ImageDetailResponse getImageDetail(Long imageId, User user);


    /**
     * 로그인 한 유저는 짤의 좋아요를 누를 수 있다.
     */
    void likeImage(Long imageId, User user);


    /**
     * 좋아요한 짤 조회
     */
    List<AwsS3ResponseDto> getLikeImages(User user, Pageable pageable);

    /**
     * 업로드한 짤 조회
     */
    List<AwsS3ResponseDto> getUploadImages(User user, Pageable pageable);
}
