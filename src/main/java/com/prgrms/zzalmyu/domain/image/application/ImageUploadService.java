package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageUploadService {

    /**
     * 짤 업로드 인자 : imageUrl, List<Tag>
     */
    AwsS3ResponseDto uploadImage(User user, MultipartFile multipartFile, List<Long> tagIdList) throws IOException;
}
